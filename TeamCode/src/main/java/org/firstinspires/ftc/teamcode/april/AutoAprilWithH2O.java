package org.firstinspires.ftc.teamcode.april;

import com.acmerobotics.dashboard.config.Config;
import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Config
@TeleOp
public class AutoAprilWithH2O extends H2OLooTeleOp {

    public static int DESIRED_TAG_ID = 5;

    @Override
    public void opModeInit() {
        cameraControl.initAprilTag(hardwareMap);
//        cameraControl.setDesiredTagId(10);
    }

    @Override
    public void opModePeriodic() {

        cameraControl.setDesiredTagId(DESIRED_TAG_ID);
        cameraControl.followAprilTagTeleOp(driveTrain, attachmentControl);

    }

}
