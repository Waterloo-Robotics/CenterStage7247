package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.DriveTrain;
import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Mecanum TeleOp", group = "!")
public class DriveTele extends H2OLooTeleOp {

    @Override
    public void opModeInit() {}

    @Override
    public void opModePeriodic() {

            driveTrain.teleOpDrive(
                    gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x
            );

    }

}
