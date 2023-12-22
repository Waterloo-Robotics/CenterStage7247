package com.ftc.waterloo.h2oloobots;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
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
    DcMotor liftLeft, liftRight;
    MotorControlGroup liftGroup;
    Servo boxServoLeft, boxServoRight;
    Servo boxDoorServo;
    boolean lastRightBumper = false;
    boolean lastLeftBumper = false;
    DcMotor hangMotor;
    Servo hangServo;
    boolean isGP2APressed = false;

    public AttachmentControl(HardwareMap hardwareMap, TelemetryControl telemetryControl, Gamepad gamepad1, Gamepad gamepad2) {

        this.telemetryControl = telemetryControl;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

//        droneServo = hardwareMap.servo.get("droneServo");
        rollerCRServo = hardwareMap.crservo.get("rollerCRServo");
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");

        liftLeft = hardwareMap.dcMotor.get("liftLeft");
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight = hardwareMap.dcMotor.get("liftRight");
        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftGroup = new MotorControlGroup(liftLeft, liftRight);
        liftGroup.setDirection(DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE);

        boxServoLeft = hardwareMap.servo.get("boxServoLeft");
        boxServoRight = hardwareMap.servo.get("boxServoRight");
        boxDoorServo = hardwareMap.servo.get("boxDoorServo");

        hangMotor = hardwareMap.dcMotor.get("hangMotor");
        hangMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hangServo = hardwareMap.servo.get("hangServo");
        hangServo.scaleRange(0.16, 0.517);

    }

    public void boxManual() {

        if (gamepad2.y) {

            boxServoRight.setPosition(boxServoRight.getPosition() + 0.003);

        } else if (gamepad2.a) {

            boxServoRight.setPosition(boxServoRight.getPosition() - 0.003);

        }

        if (gamepad2.dpad_up) {

            boxServoLeft.setPosition(boxServoLeft.getPosition() + 0.003);

        } else if (gamepad2.dpad_down) {

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

        liftGroup.setPower(gamepad2.left_stick_y);
        telemetryControl.addData("Lift Group Position", liftGroup.getCurrentPosition());

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

            if (!lastRightBumper && intakeMotor.getPower() < 0.45) {

                intakeMotor.setPower(0.75);
                rollerCRServo.setPower(-1);

            } else if (!lastRightBumper) {

                intakeMotor.setPower(0);
                rollerCRServo.setPower(0);

            }

            lastRightBumper = true;

        } else {

            lastRightBumper = false;

        }

        if (gamepad1.left_bumper) {

            if (!lastLeftBumper && intakeMotor.getPower() > -0.3) {

                intakeMotor.setPower(-0.35);
                rollerCRServo.setPower(1);

            } else if (!lastLeftBumper) {

                intakeMotor.setPower(0);
                rollerCRServo.setPower(0);

            }

            lastLeftBumper = true;

        } else {

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

        if (gamepad1.a) {

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
