package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous
public class BluePark_2ndtileOnly extends H2OLooAuto {

    public void opModeInit() {

//        while (opModeIsActive()) cameraControl.telemetryAprilTag();

    }

    public void opModePeriodic() {

        driveTrain.EncoderAutoMecanumDrive(26, 0, 0, 0.5, 3);
        attachmentControl.purplePixelSpit();
        driveTrain.EncoderAutoMecanumDrive(-2, 0, 0, 0.5, 1);
        driveTrain.EncoderAutoMecanumDrive(0, 18, 0, 0.5, 2);
        driveTrain.EncoderAutoMecanumDrive(21, 0, 0, 0.5, 2);
        driveTrain.EncoderAutoMecanumDrive(0, 0, -88, 0.5, 5);
        driveTrain.EncoderAutoMecanumDrive(0, 2, 0, 0.5, 2);
        driveTrain.EncoderAutoMecanumDrive(90, 0, 0, 0.5, 8);
        driveTrain.EncoderAutoMecanumDrive(0, -25, 0, 0.5, 3);
        sleep(2000);
        attachmentControl.intakeAuto();
//        driveTrain.EncoderAutoMecanumDrive(-6, 0, 0, 0.75, 1);
//        sleep(1000);
//        driveTrain.EncoderAutoMecanumDrive(6, 0, 0, 0.75, 1);
        sleep(10000);

    }

}
