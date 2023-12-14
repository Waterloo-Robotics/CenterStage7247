package org.firstinspires.ftc.teamcode.april;

import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class AutoAprilWithH2O extends H2OLooTeleOp {

    @Override
    public void opModeInit() {
        cameraControl.initAprilTag(hardwareMap);
        cameraControl.setDesiredTagId(10);
    }

    @Override
    public void opModePeriodic() {

        cameraControl.followAprilTag(driveTrain, attachmentControl);

    }

}
