package org.firstinspires.ftc.teamcode.tests;

import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name = "Turn Test")
public class TurnTest extends H2OLooAuto {

    @Override
    public void opModeInit() {

    }

    @Override
    public void opModePeriodic() {
        driveTrain.imuTurn(90, 1000);
    }
}
