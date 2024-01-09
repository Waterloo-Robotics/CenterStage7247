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

    CRServo rollerCRServo;

    DcMotor intakeMotor;
    public DcMotorEx liftLeft, liftRight;
    public MotorControlGroupEx liftGroup;
    Servo boxServoLeft, boxServoRight;
    public Servo boxDoorServo;
    boolean lastRightBumper = false;
    boolean lastLeftBumper = false;
    boolean isGoingUp = false;
    boolean isGoingUpUp = false;
    ElapsedTime upTime = new ElapsedTime();
    ElapsedTime upDoublePressTime = new ElapsedTime();
    boolean isUpPressedOnce = true;
    DcMotor hangMotor;
    Servo hangServo;
    boolean isGP2APressed = false;
    boolean isBPressed = false;
    boolean isDpadUpPressed = false;
    TouchSensor leftTouch, rightTouch;


    public AttachmentControl(HardwareMap hardwareMap, TelemetryControl telemetryControl, Gamepad gamepad1, Gamepad gamepad2) {

        this.telemetryControl = telemetryControl;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        droneServo = hardwareMap.servo.get("droneServo");
        rollerCRServo = hardwareMap.crservo.get("rollerCRServo");
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");

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
        liftGroup.setTargetPosition(0);
        liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftGroup.setTargetPositionTolerance(80);

        boxServoLeft = hardwareMap.servo.get("boxServoLeft");
        boxServoLeft.setDirection(Servo.Direction.REVERSE);
//        boxServoLeft.scaleRange(0, 0.686);
        boxServoLeft.setPosition(0.686);
        boxServoRight = hardwareMap.servo.get("boxServoRight");
//        boxServoRight.scaleRange(0, 0.686);
        boxServoRight.setPosition(0.686);
        boxDoorServo = hardwareMap.servo.get("boxDoorServo");
        boxDoorServo.scaleRange(0.585, 1);

        hangMotor = hardwareMap.dcMotor.get("hangMotor");
        hangMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hangServo = hardwareMap.servo.get("hangServo");
        hangServo.scaleRange(0.402, 0.483);

        leftTouch = hardwareMap.touchSensor.get("leftTouch");
        rightTouch = hardwareMap.touchSensor.get("rightTouch");

    }

    public void touchSensorTelemetry() {

        telemetryControl.addData("Left Touch", leftTouch.isPressed());
        telemetryControl.addData("Right Touch", rightTouch.isPressed());

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

            boxDoorServo.setPosition(boxDoorServo.getPosition() + 0.003);

        } else if (gamepad2.right_bumper) {

            boxDoorServo.setPosition(boxDoorServo.getPosition() - 0.003);

        }

        telemetryControl.addData("Box Servo Right Position", boxServoRight.getPosition());
        telemetryControl.addData("Box Servo Left Position", boxServoLeft.getPosition());
        telemetryControl.addData("Box Door Servo Position", boxDoorServo.getPosition());

    }

    public void liftManual() {
        liftGroup.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftGroup.setPower(gamepad2.left_stick_y);
        telemetryControl.addData("Lift Group Position", liftGroup.getCurrentPosition());

    }

    public void liftTeleOp() {

        if (gamepad2.dpad_down || gamepad1.dpad_down) {

            liftGroup.setTargetPosition(0);
            hangServo.setPosition(0);
            boxServoLeft.setPosition(0.686);
            boxServoRight.setPosition(0.686);
            boxDoorServo.setPosition(0);
            isGoingUp = false;
            isGoingUpUp = false;

        } else if (gamepad2.dpad_up || gamepad1.dpad_up) {
            upTime.reset();

            if (!isDpadUpPressed) {

                if (!isUpPressedOnce || upDoublePressTime.seconds() > 0.5) {

                    isUpPressedOnce = true;
                    upDoublePressTime.reset();
                    isGoingUp = true;
                    isGoingUpUp = false;

                } else if (isUpPressedOnce && upDoublePressTime.seconds() <= 0.5) {

                    isGoingUpUp = true;
                    isGoingUp = false;
                    isUpPressedOnce = false;

                } else {

                    isUpPressedOnce = false;

                }

            }

            isDpadUpPressed = true;
            boxDoorServo.setPosition(1);

        }
        isDpadUpPressed = gamepad1.dpad_up || gamepad2.dpad_up;
        if (isGoingUp && upTime.seconds() > 0.5) {

            liftGroup.setTargetPosition(-1200);
            liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hangServo.setPosition(0.84);
            boxServoLeft.setPosition(0.287);
            boxServoRight.setPosition(0.356);
            isGoingUp = false;
            isGoingUpUp = false;

        } else if (isGoingUpUp && upTime.seconds() > 0.5) {

            liftGroup.setTargetPosition(-1800);
            liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hangServo.setPosition(0.84);
            boxServoLeft.setPosition(0.287);
            boxServoRight.setPosition(0.356);
            isGoingUpUp = false;
            isGoingUp = false;

        }

        if (gamepad1.dpad_right) {

            boxServoLeft.setPosition(0.686);
            boxServoRight.setPosition(0.686);
            boxDoorServo.setPosition(0);

        }

        if (gamepad1.b) {

            if (!isBPressed) {

                if (boxDoorServo.getPosition() == 1) {

                    boxDoorServo.setPosition(0);

                } else {

                    boxDoorServo.setPosition(1);

                }

            }

            isBPressed = true;

        } else {

            isBPressed = false;

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

    }

    public void score() {

        boxDoorServo.setPosition(1);
        liftGroup.setTargetPosition(-1200);
        liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hangServo.setPosition(0.84);
        boxServoLeft.setPosition(0.287);
        boxServoRight.setPosition(0.356);
        liftGroup.setPower(0.5);

    }

    public void compact() {

        liftGroup.setTargetPosition(0);
        liftGroup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hangServo.setPosition(0);
        boxServoLeft.setPosition(0.686);
        boxServoRight.setPosition(0.686);
        boxDoorServo.setPosition(0);
        liftGroup.setPower(1);

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

        if (gamepad2.a) {

            if (!isGP2APressed) {

                if (hangServo.getPosition() < 0.05) {

                    hangServo.setPosition(0.845);

                } else if (hangServo.getPosition() < 0.95) {

                    hangServo.setPosition(1);

                } else {

                    hangServo.setPosition(0);

                }


            }

            isGP2APressed = true;

        } else {

            isGP2APressed = false;

        }

    }

    public void intakeTeleOp() {

        if (gamepad1.right_bumper) {

            intakeMotor.setPower(1);
            rollerCRServo.setPower(-1);
            lastRightBumper = true;

        } else if (gamepad1.left_bumper) {

            lastLeftBumper = true;
            intakeMotor.setPower(-0.5);
            rollerCRServo.setPower(1);

        } else {

            intakeMotor.setPower(0);
            rollerCRServo.setPower(0);
            lastLeftBumper = false;

        }

    }

    public void intakeAuto() {

        intakeMotor.setPower(-0.35);
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

        if (gamepad2.b) {

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
