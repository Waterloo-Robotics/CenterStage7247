package com.ftc.waterloo.h2oloobots;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    DcMotorEx liftRight, liftLeft;

    Servo droneServo;

    CRServo rollerCRServo;

    DcMotor intakeMotor;
    boolean lastRightBumper = false;
    boolean lastLeftBumper = false;

    public AttachmentControl(HardwareMap hardwareMap, TelemetryControl telemetryControl, Gamepad gamepad1, Gamepad gamepad2) {

        this.telemetryControl = telemetryControl;
//        this.liftRight = (DcMotorEx) hardwareMap.dcMotor.get("liftRight");
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        droneServo = hardwareMap.servo.get("droneServo");
        rollerCRServo = hardwareMap.crservo.get("rollerCRServo");
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");

    }

    public void intakeTeleOp() {

        if (gamepad1.right_bumper) {

            if (!lastRightBumper && intakeMotor.getPower() < 0.85) {

                intakeMotor.setPower(1);
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

            if (!lastLeftBumper && intakeMotor.getPower() > -0.85) {

                intakeMotor.setPower(-1);
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

        ElapsedTime time = new ElapsedTime();

        time.reset();
        while (time.seconds() < 5) {

            intakeMotor.setPower(-1);
            rollerCRServo.setPower(1);

        }

    }

    public void droneTeleOp() {

        if (gamepad1.a) {

            droneServo.setPosition(0.5);

        } else {

            droneServo.setPosition(0);

        }

    }

    public void liftManual(double power) {

        liftRight.setPower(power);

    }

    public void liftTeleOp() {



    }

}
