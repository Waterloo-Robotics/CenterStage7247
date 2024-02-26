package com.ftc.waterloo.h2oloobots;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**This AttachmentControl class just offers a global area to store any non-drivebase commands and
 * devices to be used as chosen. Most imports should be added already, but if you need other
 * imports Android Studio is pretty good at auto importing.*/
public class AttachmentControl {

    /** This declares a global telemetryControl variable to make our lives easier, we set these when
     * we initialise the file. You can use this telemetryControl variable to add telemetry without
     * having to route it from the opMode to this class.
    */
    TelemetryControl telemetryControl;
    Gamepad gamepad1, gamepad2;

    Servo droneServo;

    CRServo rollerCRServo, boxWheel;

    DcMotor intakeMotor;
    public DcMotorEx liftLeft, liftRight;
    public MotorControlGroupEx liftGroup;
    Servo boxServoLeft, boxServoRight, extLeft, extRight;
    public Servo boxDoorServo;
    boolean lastRightBumper = false;
    boolean lastLeftBumper = false;
    ElapsedTime upTime = new ElapsedTime();
    ElapsedTime downTime = new ElapsedTime();
    ElapsedTime upDoublePressTime = new ElapsedTime();
    boolean isUpPressedOnce = true;
    public DcMotor hangMotor;
    Servo hangServo;
    boolean isBPressed = false;
    TouchSensor leftTouch, rightTouch;
    ElapsedTime boxWheelTime = new ElapsedTime();
    boolean boxWheelStarted = false;
    ElapsedTime gp2ATime = new ElapsedTime();
    boolean isGP2APressed = false;

    boolean isManualShutStarted = false;
    ElapsedTime manualShutTime = new ElapsedTime();
    boolean isManualOpenStarted = false;
    ElapsedTime manualOpenTime = new ElapsedTime();

    public enum ArmState {

        INTAKE,
        SCORE_MED,
        SCORE_LOW,
        SCORE_HIGH,
        MANUAL

    }

    ArmState armState = ArmState.INTAKE;


    public AttachmentControl(HardwareMap hardwareMap, TelemetryControl telemetryControl, Gamepad gamepad1, Gamepad gamepad2) {

        this.telemetryControl = telemetryControl;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        boxWheel = hardwareMap.crservo.get("boxServoCenter");
        droneServo = hardwareMap.servo.get("droneServo");
        rollerCRServo = hardwareMap.crservo.get("rollerCRServo");

        boxServoLeft = hardwareMap.servo.get("boxServoLeft");
        boxServoLeft.setDirection(Servo.Direction.REVERSE);
//        boxServoLeft.scaleRange(0, 0.686);
        boxServoLeft.setPosition(0.827);
        boxServoRight = hardwareMap.servo.get("boxServoRight");
//        boxServoRight.scaleRange(0, 0.686);
        boxServoRight.setPosition(0.827);
        boxDoorServo = hardwareMap.servo.get("boxDoorServo");
        boxDoorServo.scaleRange(0.08, 0.39);
        boxDoorServo.setPosition(0);

        extLeft = hardwareMap.servo.get("extLeft");
        extLeft.scaleRange(0.23, 0.82);
        extLeft.setPosition(0);
        extRight = hardwareMap.servo.get("extRight");
        extRight.scaleRange(0.23, 0.82);
        extRight.setPosition(0);

        hangMotor = hardwareMap.dcMotor.get("hangMotor");
        hangMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hangServo = hardwareMap.servo.get("hangServo");
        hangServo.scaleRange(0.402, 0.483);
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");

        leftTouch = hardwareMap.touchSensor.get("leftTouch");
        rightTouch = hardwareMap.touchSensor.get("rightTouch");

        liftLeft = (DcMotorEx) hardwareMap.dcMotor.get("liftLeft");
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight = (DcMotorEx) hardwareMap.dcMotor.get("liftRight");
        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftGroup = new MotorControlGroupEx(liftLeft, liftRight);
        liftGroup.setDirection(DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE);
        liftGroup = new MotorControlGroupEx(liftLeft, liftRight);
        liftGroup.setDirection(DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE);
        while (!leftTouch.isPressed() && !rightTouch.isPressed()) {

            if (leftTouch.isPressed()) {

                liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftLeft.setPower(0);

            } else liftLeft.setPower(0.5);

            if (rightTouch.isPressed()) {

                liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftRight.setPower(0);

            } else liftRight.setPower(0.5);

        }
        liftGroup.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftGroup.setTargetPosition(0);
        liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftGroup.setTargetPositionTolerance(80);

    }

    public void touchSensorTelemetry() {

        telemetryControl.addData("Left Touch", leftTouch.isPressed());
        telemetryControl.addData("Right Touch", rightTouch.isPressed());

    }

    public void extManual() {

        if (gamepad1.a) {

//            extRight.setPosition(extRight.getPosition() + 1.0);
//            extLeft.setPosition(extLeft.getPosition() + 1.0);
            extRight.setPosition(1);
            extLeft.setPosition(1);

        } else if (gamepad1.b) {

//            extRight.setPosition(extRight.getPosition() - 0.5);
//            extLeft.setPosition(extLeft.getPosition() -0.5);
            extRight.setPosition(0);
            extLeft.setPosition(0);

        }

        telemetryControl.addData("ExtLeft Servo Position", extLeft.getPosition());
        telemetryControl.addData("ExtRight Servo Position", extRight.getPosition());

    }

    public void boxManual() {

        if (gamepad2.y) {

            boxServoRight.setPosition(boxServoRight.getPosition() + 0.003);
            boxServoLeft.setPosition(boxServoLeft.getPosition() + 0.003);

        } else if (gamepad2.a) {

            boxServoRight.setPosition(boxServoRight.getPosition() - 0.003);
            boxServoLeft.setPosition(boxServoLeft.getPosition() - 0.003);

        }

        if (gamepad2.left_bumper) {

            boxDoorServo.setPosition(boxDoorServo.getPosition() + 0.01);

        } else if (gamepad2.right_bumper) {

            boxDoorServo.setPosition(boxDoorServo.getPosition() - 0.01);

        }

        telemetryControl.addData("Box Servo Right Position", boxServoRight.getPosition());
        telemetryControl.addData("Box Servo Left Position", boxServoLeft.getPosition());
        telemetryControl.addData("Box Door Servo Position", boxDoorServo.getPosition());

    }

    public void liftManual() {
        liftGroup.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftGroup.setPower(gamepad2.left_stick_y);
        liftGroup.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        telemetryControl.addData("Lift Group Position", liftGroup.getCurrentPosition());

    }

    public void liftTeleOp() {

        int liftTargetPosition = liftGroup.getTargetPosition();

        if (gamepad2.dpad_down || gamepad1.dpad_down) {

            liftGroup.setTargetPosition(0);
            hangServo.setPosition(0);
            boxServoLeft.setPosition(0.69);
            boxServoRight.setPosition(0.69);
            extLeft.setPosition(0);
            extRight.setPosition(0);
            armState = ArmState.INTAKE;
            downTime.reset();
            isManualShutStarted = true;
            isManualOpenStarted = false;

        } else if (gamepad2.dpad_up || gamepad1.dpad_up) {

            upTime.reset();
            armState = ArmState.SCORE_LOW;
            liftGroup.setTargetPosition(-750);
            liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hangServo.setPosition(0.84);
            boxServoLeft.setPosition(0.51);
            boxServoRight.setPosition(0.51);
            isManualShutStarted = false;
            isManualOpenStarted = true;

        } else if (gamepad2.dpad_right || gamepad1.dpad_right) {

            if (armState == ArmState.INTAKE) upTime.reset();
            armState = ArmState.SCORE_MED;
            liftGroup.setTargetPosition(-1200);
            liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hangServo.setPosition(0.84);
            boxServoLeft.setPosition(0.51);
            boxServoRight.setPosition(0.51);
            isManualShutStarted = false;
            isManualOpenStarted = true;

        } else if (gamepad2.dpad_left || gamepad1.dpad_left) {

            if (armState == ArmState.INTAKE) upTime.reset();
            armState = ArmState.SCORE_HIGH;
            liftGroup.setTargetPosition(-1950);
            liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hangServo.setPosition(0.84);
            boxServoLeft.setPosition(0.51);
            boxServoRight.setPosition(0.51);
            isManualShutStarted = false;
            isManualOpenStarted = true;

        } else if (Math.abs(gamepad2.right_stick_y) > 0.1) {

            armState = ArmState.MANUAL;
            liftTargetPosition = (int) liftGroup.getCurrentPosition();

        }

        if (upTime.seconds() > 0.5) {

            if (armState == ArmState.SCORE_LOW) {

                extLeft.setPosition(1);
                extRight.setPosition(1);

            } else if (armState == ArmState.SCORE_HIGH) {

                extLeft.setPosition(1);
                extRight.setPosition(1);

            } else if (armState == ArmState.SCORE_MED) {

                extLeft.setPosition(1);
                extRight.setPosition(1);

            }

        }

        if (armState == ArmState.INTAKE && downTime.seconds() > 2.0) {

            boxServoLeft.setPosition(0.827);
            boxServoRight.setPosition(0.827);

        }

        if (armState == ArmState.MANUAL) {

            if (gamepad2.right_stick_y > 0) {

                liftTargetPosition += (int) (gamepad2.right_stick_y * 150);

            } else {

                liftTargetPosition += (int) (gamepad2.right_stick_y * 150);

            }
            if (liftTargetPosition < -1950) liftTargetPosition = -1950;
            if (liftTargetPosition > 0) liftTargetPosition = 0;

            liftGroup.setTargetPosition(liftTargetPosition);

            if (liftTargetPosition < -100) hangServo.setPosition(0.84);
            else hangServo.setPosition(0);

            if (liftTargetPosition < -100 && !isManualOpenStarted) {

                isManualOpenStarted = true;
                isManualShutStarted = false;
                manualOpenTime.reset();
                boxServoLeft.setPosition(0.69);
                boxServoRight.setPosition(0.69);

            } else if (liftTargetPosition >= -100 && !isManualShutStarted) {

                isManualOpenStarted = false;
                isManualShutStarted = true;
                manualShutTime.reset();
                boxServoLeft.setPosition(0.69);
                boxServoRight.setPosition(0.69);
                extLeft.setPosition(0);
                extRight.setPosition(0);

            }

            if (isManualOpenStarted && manualOpenTime.seconds() > 0.5) {

                extLeft.setPosition(1);
                extRight.setPosition(1);
                boxServoLeft.setPosition(0.51);
                boxServoRight.setPosition(0.51);

            }

            if (isManualShutStarted && manualShutTime.seconds() > 2.0) {

                boxServoLeft.setPosition(0.827);
                boxServoRight.setPosition(0.827);

            }


        }

        if ((leftTouch.isPressed() || rightTouch.isPressed()) && liftGroup.getTargetPosition() == 0) {

            liftGroup.setPower(0);
            liftGroup.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            liftGroup.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        } else {

            liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if (liftGroup.getCurrentPosition() > -200 || liftGroup.getCurrentPosition() < -1600) liftGroup.setPower(0.5);
            else liftGroup.setPower(1);

        }

        telemetryControl.addData("Arm State", armState);
        telemetryControl.addData("Lift Target Position", liftTargetPosition);

    }

    public void score() {

        liftGroup.setTargetPosition(0);
        liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hangServo.setPosition(0);
        boxServoLeft.setPosition(0.51);
        boxServoRight.setPosition(0.51);
        liftGroup.setPower(0.5);

    }

    public void scoreAudience() {

        liftGroup.setTargetPosition(0);
        liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hangServo.setPosition(0);
        boxServoLeft.setPosition(0.69);
        boxServoRight.setPosition(0.69);
        liftGroup.setPower(0.5);

        upTime.reset();
        while (upTime.seconds() < 0.5);
        extLeft.setPosition(1);
        extRight.setPosition(1);

    }

    public void scoreAudience2() {

        liftGroup.setTargetPosition(-333);
        liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hangServo.setPosition(0);
        boxServoLeft.setPosition(0.517);
        boxServoRight.setPosition(0.517);
        liftGroup.setPower(0.5);

        upTime.reset();
        while (upTime.seconds() < 0.5);
        extLeft.setPosition(1);
        extRight.setPosition(1);

    }

    public void score2() {

        extLeft.setPosition(1);
        extRight.setPosition(1);

    }

    public void drop() {

        boxWheel.setPower(-1);

    }

    public void lift() {

        boxServoLeft.setPosition(0.43);
        boxServoRight.setPosition(0.43);

    }

    public void compact() {

        liftGroup.setTargetPosition(0);
        liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hangServo.setPosition(0);
        boxServoLeft.setPosition(0.69);
        boxServoRight.setPosition(0.69);
        liftGroup.setPower(1);
        extLeft.setPosition(0);
        extRight.setPosition(0);

        upTime.reset();
        while (upTime.seconds() < 2.0);
        boxServoLeft.setPosition(0.877);
        boxServoRight.setPosition(0.877);

    }

    public void compactAudience() {

        liftGroup.setTargetPosition(0);
        liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hangServo.setPosition(0);
        boxServoLeft.setPosition(0.69);
        boxServoRight.setPosition(0.69);
        liftGroup.setPower(1);
        extLeft.setPosition(0);
        extRight.setPosition(0);

    }

    public void hangMotorManual() {

        hangMotor.setPower(-gamepad2.left_stick_y);

    }

    public void hangServoManual() {

        if (gamepad1.dpad_down) {
            hangServo.setPosition(hangServo.getPosition() + 0.001);
        } else if (gamepad1.dpad_right) {
            hangServo.setPosition(hangServo.getPosition() - 0.001);
        }

        telemetryControl.addData("Hang Servo Position", hangServo.getPosition());


    }

    public void hangServoTeleOp() {

        if (gamepad2.a && !isGP2APressed) {

            gp2ATime.reset();

        }

        isGP2APressed = gamepad2.a;

        if (isGP2APressed && gp2ATime.seconds() > 1) {

            boxDoorServo.setPosition(1);

        }

    }

    public void intakeTeleOp() {

        if (gamepad1.right_bumper) {

            boxWheel.setPower(1);
            intakeMotor.setPower(1);
            rollerCRServo.setPower(-1);
            lastRightBumper = true;

        } else if (gamepad1.left_bumper) {


            lastLeftBumper = true;
            intakeMotor.setPower(-0.5);
            rollerCRServo.setPower(1);

        } else {
            boxWheel.setPower(0);
            intakeMotor.setPower(0);
            rollerCRServo.setPower(0);
            lastLeftBumper = false;

        }



        if (gamepad1.b) {

            if (!isBPressed) {

                boxWheelTime.reset();
                boxWheelStarted = true;

            }

            isBPressed = true;

        } else {

            isBPressed = false;

        }

        if (boxWheelStarted && boxWheelTime.seconds() < 0.25) {

            boxWheel.setPower(-0.5);

        } else if (gamepad1.right_bumper) {

            boxWheel.setPower(1);

        } else {

            boxWheel.setPower(0);

        }

    }

    public void intakeAuto() {

        intakeMotor.setPower(1);
        rollerCRServo.setPower(-1);

    }

    public void intakeStop() {

        intakeMotor.setPower(0);
        rollerCRServo.setPower(0);

    }

    public void intakeSpit() {

        intakeMotor.setPower(-0.5);
        rollerCRServo.setPower(1);

    }

    public void purplePixelSpit() {

        ElapsedTime time = new ElapsedTime();
        time.reset();

        while (time.seconds() < 1.25) {

            intakeMotor.setPower(-0.15);
        }

        intakeMotor.setPower(0);

    }

    public void droneTeleOp() {

        if (gamepad2.y) {

            droneServo.setPosition(0.5);

        } else {

            droneServo.setPosition(0);

        }

    }

    public void droneManual() {

        if (gamepad1.a) {
            droneServo.setPosition(droneServo.getPosition() + 0.03);
        } else if (gamepad1.b) {
            droneServo.setPosition(droneServo.getPosition() - 0.03);
        }

        telemetryControl.addData("Drone Servo Position", droneServo.getPosition());


    }

}
