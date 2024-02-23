package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@TeleOp
public class IntakeTest extends LinearOpMode {

    DcMotor intakeMotor;
    CRServo rollerCRServo;

    public void runOpMode() {

        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
        rollerCRServo = hardwareMap.crservo.get("rollerCRServo");

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.right_bumper) {

                intakeMotor.setPower(1);
                rollerCRServo.setPower(-1);

            } else if (gamepad1.left_bumper) {

                intakeMotor.setPower(-0.5);
                rollerCRServo.setPower(1);

            } else {

                intakeMotor.setPower(0);
                rollerCRServo.setPower(0);

            }

        }

    }

}
