package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.AttachmentControl;
import com.ftc.waterloo.h2oloobots.CameraControl;
import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Stage Red")
public class StageRed extends H2OLooAuto {

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

                driveTrain.EncoderAutoMecanumDrive(-26, 0, 45, 0.75, 1.75);
                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(-25, -6, 0, 0.5, 1.75);
                break;

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(-18, -14, 0, 0.5, 3);
                break;
        }
        attachmentControl = new AttachmentControl(hardwareMap, telemetryControl, gamepad1, gamepad2);
        attachmentControl.score();
        switch (location) {

            case LEFT:

                driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.5, 0.5);
                driveTrain.EncoderAutoMecanumDrive(0, 0, -130, 0.5, 2);
                attachmentControl.score2();
                driveTrain.EncoderAutoMecanumDrive(-19, 8.5, 0, 0.5, 1.5);
                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(2, 0, 0, 0.5, 0.5);
                driveTrain.EncoderAutoMecanumDrive(0, -16, 0, 0.5, 1.5);
                attachmentControl.score2();
                driveTrain.EncoderAutoMecanumDrive(0, 0, -83, 0.5, 1.75);
                break;

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(2, 0, 0, 0.5, 0.5);
                driveTrain.EncoderAutoMecanumDrive(0, -12, 0, 0.5, 3);
                attachmentControl.score2();
                driveTrain.EncoderAutoMecanumDrive(0, 0, -83, 0.5, 3);
                break;

        }

        switch (location) {

            case LEFT:
                driveTrain.EncoderAutoMecanumDrive(-15, 0, 0, 0.5, 1);
                break;

            case CENTER:
                driveTrain.EncoderAutoMecanumDrive(-19, 4, 3, 0.5, 2);
                break;

            case RIGHT:
                driveTrain.EncoderAutoMecanumDrive(-15, 6, -3, 0.5, 1);
                break;

        }
        driveTrain.fl.setPower(0.35);
        driveTrain.fr.setPower(0.35);
        driveTrain.bl.setPower(0.35);
        driveTrain.br.setPower(0.35);
        sleep(500);
        attachmentControl.drop();
        sleep(500);
        attachmentControl.lift();
        sleep(500);
        driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.75, 1);
        while (attachmentControl.liftGroup.isBusy());

        switch (location) {

            case LEFT:
                driveTrain.EncoderAutoMecanumDrive(0, -36, 0, 0.5, 2);
                break;

            case CENTER:
                driveTrain.EncoderAutoMecanumDrive(0, -27, 0, 0.5, 2);
                break;

            case RIGHT:
                driveTrain.EncoderAutoMecanumDrive(0, -16, 0, 0.5, 2);
                break;

        }

        attachmentControl.compact();

    }
}
