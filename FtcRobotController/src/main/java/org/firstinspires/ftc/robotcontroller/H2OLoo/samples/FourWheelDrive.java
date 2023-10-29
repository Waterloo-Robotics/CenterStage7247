package org.firstinspires.ftc.robotcontroller.H2OLoo.samples;

import com.ftc.waterloo.h2oloobots.DriveTrain;
import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "Four Wheel Drive TeleOp", group = "H2OLoo Samples")
public class FourWheelDrive extends H2OLooTeleOp {

    @Override
    public void opModeInit() {

        driveTrain.setDriveTrainType(DriveTrain.DriveTrainType.FOUR_WHEEL_TANK);

    }

    @Override
    public void opModePeriodic() {

        while (opModeIsActive()) {

            /*driveTrain.teleOpDrive(
                    gamepad1.left_stick_y,
                    gamepad1.left_stick_x
            );*/

        }

    }

}
