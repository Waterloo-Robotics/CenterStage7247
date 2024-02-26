package org.firstinspires.ftc.teamcode.tests;

import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
@TeleOp
public class TPSTest extends H2OLooTeleOp {

    int ticks = 0;
    ElapsedTime time = new ElapsedTime();

    public void opModeInit() {



    }

    public void opModePeriodic() {

        time.reset();
        if (time.seconds() < 1) ticks++;

        while (opModeIsActive()) {

            telemetry.addData("ticks", ticks);
            telemetry.update();

        }

    }

}
