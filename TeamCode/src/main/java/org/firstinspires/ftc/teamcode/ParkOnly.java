package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.DriveTrain;
import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class ParkOnly extends H2OLooAuto {

    @Override
    public void opModeInit() {

        driveTrain.setDriveTrainType(DriveTrain.DriveTrainType.MECANUM);

    }

    @Override
    public void opModePeriodic() {

        driveTrain.EncoderAutoMecanumDrive(36, 0, 0, 0.75, 3);
        sleep(2000);
        attachmentControl.intakeAuto();
        driveTrain.EncoderAutoMecanumDrive(-6, 0, 0, 0.75, 1);
        sleep(1000);
        driveTrain.EncoderAutoMecanumDrive(6, 0, 0, 0.75, 1);
        sleep(10000);

    }

}
