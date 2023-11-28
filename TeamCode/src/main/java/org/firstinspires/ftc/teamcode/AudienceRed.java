package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.CameraControl;
import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Audience Red")
public class AudienceRed extends H2OLooAuto {

    CameraControl.PropLocation location;

    @Override
    public void opModeInit() {

        initCamera(CameraControl.Alliance.RED);

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

                driveTrain.EncoderAutoMecanumDrive(20, -10, 0, 0.5, 3);
                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(26, 0, 0, 0.5, 3);
                break;

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(26, 0, 0, 0.5, 3);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 90, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.5, 2);
                break;
        }
        attachmentControl.purplePixelSpit();
        switch (this.location) {

            case LEFT:

                driveTrain.EncoderAutoMecanumDrive(-5, 0, 0, 0.5, 3);
                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(-2, 0, 0, 0.5, 1);
                driveTrain.EncoderAutoMecanumDrive(0, -18, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(23, 0, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 90, 0.5, 5);
                driveTrain.EncoderAutoMecanumDrive(93, 0, 0, 0.5, 8);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 45, 0.5, 2);
                attachmentControl.intakeAuto();
                driveTrain.EncoderAutoMecanumDrive(-4, 0, 0, 0.5, 1);
                driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.5, 1);
                break;

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(-4, 0, 0, 0.5, 3);
                driveTrain.EncoderAutoMecanumDrive(0, -29, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(82, 0, 0, 0.5, 8);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 45, 0.5, 2);
                attachmentControl.intakeAuto();
                driveTrain.EncoderAutoMecanumDrive(-4, 0, 0, 0.5, 1);
                driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.5, 1);
                break;
        }

        while (opModeIsActive());

    }
}
