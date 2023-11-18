package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.CameraControl;
import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Audience Blue")
public class AudienceBlue extends H2OLooAuto {

    CameraControl.PropLocation location;

    @Override
    public void opModeInit() {

        initCamera(CameraControl.Alliance.BLUE);

        while (opModeInInit()) {
            location = cameraControl.getLocation();
            telemetryControl.addData("Prop Location", location);
            telemetryControl.update();
        }

    }

    @Override
    public void opModePeriodic() {

        cameraControl.close();

        switch (this.location) {

            case LEFT:

                driveTrain.EncoderAutoMecanumDrive(26, 0, 0, 0.5, 3);
                driveTrain.EncoderAutoMecanumDrive(0, 0, -90, 0.5, 2);
                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(24, 0, 0, 0.5, 3);
                break;

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(18, 14, 0, 0.5, 3);
                break;
        }
        attachmentControl.purplePixelSpit();
        switch (this.location) {

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(-5, 0, 0, 0.5, 3);

                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(-2, 0, 0, 0.5, 1);
                driveTrain.EncoderAutoMecanumDrive(0, 18, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(22.5, 0, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(0, 0, -88, 0.5, 5);
                driveTrain.EncoderAutoMecanumDrive(90, 0, 0, 0.5, 8);
                driveTrain.EncoderAutoMecanumDrive(0, 0, -45, 0.5, 2);
                attachmentControl.intakeAuto();
                driveTrain.EncoderAutoMecanumDrive(-4, 0, 0, 0.5, 1);
                driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.5, 1);
                while (opModeIsActive());
                break;

            case LEFT:

                driveTrain.EncoderAutoMecanumDrive(-4, 0, 0, 0.5, 3);
                driveTrain.EncoderAutoMecanumDrive(0, 27, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(82, 0, 0, 0.5, 8);
                driveTrain.EncoderAutoMecanumDrive(0, 0, -45, 0.5, 2);
                attachmentControl.intakeAuto();
                driveTrain.EncoderAutoMecanumDrive(-4, 0, 0, 0.5, 1);
                driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.5, 1);
                while (opModeIsActive());
                break;
        }


    }
}
