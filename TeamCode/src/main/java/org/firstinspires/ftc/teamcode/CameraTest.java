package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.CameraControl;
import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp
public class CameraTest extends H2OLooTeleOp {

    @Override
    public void opModeInit() {
        setAlliance(CameraControl.Alliance.BLUE);
    }

    @Override
    public void opModePeriodic() {
        telemetryControl.addData("Prop Location", cameraControl.getLocation());
    }
}
