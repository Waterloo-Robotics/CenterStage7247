package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.config.Config;
import com.ftc.waterloo.h2oloobots.*;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@Disabled
@Config
@TeleOp(name = "Attachment Test", group = "a")
public class AttachmentTest extends H2OLooTeleOp {

    public static int DESIRED_TAG_ID = 5;

    @Override
    public void opModeInit() {
        cameraControl.initAprilTag(hardwareMap);
    }

    @Override
    public void opModePeriodic() {

        cameraControl.setDesiredTagId(DESIRED_TAG_ID);
//        cameraControl.followAprilTagTeleOp(
//                driveTrain,
//                attachmentControl
//        );
        driveTrain.teleOpDrive(
                gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,
                attachmentControl
        );

//            attachmentControl.liftTeleOp();

        attachmentControl.touchSensorTelemetry();

//        attachmentControl.hangMotorManual();

//        attachmentControl.intakeTeleOp();
        attachmentControl.liftManual();
        attachmentControl.boxManual();
        attachmentControl.extManual();
//        attachmentControl.hangServoTeleOp();

    }

}
