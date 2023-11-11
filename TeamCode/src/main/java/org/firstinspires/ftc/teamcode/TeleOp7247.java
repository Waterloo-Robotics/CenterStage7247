package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.DriveTrain;
import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "7247 TeleOp", group = "!")
public class TeleOp7247 extends H2OLooTeleOp {

    @Override
    public void opModeInit() {

        driveTrain.setDriveTrainType(DriveTrain.DriveTrainType.MECANUM);

    }

    @Override
    public void opModePeriodic() {

        driveTrain.teleOpDrive(
                gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,
                attachmentControl
        );

        attachmentControl.droneTeleOp();
        attachmentControl.intakeTeleOp();
        attachmentControl.hangMotorManual();
        attachmentControl.hangServoTeleOp();
    }

}
