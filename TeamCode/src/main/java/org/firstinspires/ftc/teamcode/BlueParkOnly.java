package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous
public class BlueParkOnly extends H2OLooAuto {

    public void opModeInit() {

//        while (opModeIsActive()) cameraControl.telemetryAprilTag();

    }

    public void opModePeriodic() {

        driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.75, 3);
        driveTrain.EncoderAutoMecanumDrive(0, 0, -90, 0.75, 5);
        driveTrain.EncoderAutoMecanumDrive(84, 0, 0, 0.75, 8);
        sleep(2000);
        attachmentControl.intakeAuto();
        driveTrain.EncoderAutoMecanumDrive(-6, 0, 0, 0.75, 1);
        sleep(1000);
        driveTrain.EncoderAutoMecanumDrive(6, 0, 0, 0.75, 1);
        sleep(10000);

    }

}
