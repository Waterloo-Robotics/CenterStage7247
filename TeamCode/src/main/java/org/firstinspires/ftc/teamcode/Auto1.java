package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class Auto1 extends H2OLooAuto {

    public void opModeInit() {

        while (opModeIsActive()) cameraControl.telemetryAprilTag();

    }

    public void opModePeriodic() {

        while (opModeIsActive()) {
            cameraControl.telemetryAprilTag();
            telemetryControl.update();
        }

    }

}
