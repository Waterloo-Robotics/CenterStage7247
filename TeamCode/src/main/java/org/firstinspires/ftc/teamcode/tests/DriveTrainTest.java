package org.firstinspires.ftc.teamcode.tests;

import com.ftc.waterloo.h2oloobots.AttachmentControl;
import com.ftc.waterloo.h2oloobots.CameraControl;
import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
@Autonomous(name = "Drivetrain Test")
public class DriveTrainTest extends H2OLooAuto {

    @Override
    public void opModeInit() {

    }

    @Override
    public void opModePeriodic() {
        driveTrain.EncoderAutoMecanumDrive(-26, 0, 0, 0.5, 1.25);
    }
}
