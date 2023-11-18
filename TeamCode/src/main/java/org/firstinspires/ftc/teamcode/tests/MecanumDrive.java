package org.firstinspires.ftc.teamcode.tests;

import com.ftc.waterloo.h2oloobots.AttachmentControl;
import com.ftc.waterloo.h2oloobots.DriveTrain;
import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.ftc.waterloo.h2oloobots.TelemetryControl;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "Mecanum TeleOp", group = "H2OLoo Samples")
public class MecanumDrive extends LinearOpMode {

    TelemetryControl telemetryControl;
    DriveTrain driveTrain;

    @Override
    public void runOpMode() {

        telemetryControl = new TelemetryControl(telemetry);
        driveTrain = new DriveTrain(hardwareMap, telemetryControl, null);
        driveTrain.setDriveTrainType(DriveTrain.DriveTrainType.MECANUM);

        waitForStart();

        while (opModeIsActive()) {

            driveTrain.teleOpDrive(
                    gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x,
                    new AttachmentControl(hardwareMap, telemetryControl, gamepad1, gamepad2)
            );

        }

    }

}
