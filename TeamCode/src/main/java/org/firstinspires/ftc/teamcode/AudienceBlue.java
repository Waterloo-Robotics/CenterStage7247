package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.*;
import com.ftc.waterloo.h2oloobots.AttachmentControl;
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

                driveTrain.EncoderAutoMecanumDrive(-26, 0, 45, 0.75, 3);
                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(-25.5, -6, 0, 0.5, 1.75);
                break;

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(-18, -14, 0, 0.5, 3);
                break;
        }
        attachmentControl = new AttachmentControl(hardwareMap, telemetryControl, gamepad1, gamepad2);
        switch (location) {

            case LEFT:
                driveTrain.EncoderAutoMecanumDrive(4, 0, 38, 0.5, 1.5);
                driveTrain.EncoderAutoMecanumDrive(0, -28, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(-72, 0, 0, 0.5, 3);
                driveTrain.EncoderAutoMecanumDrive(0, 31, 0, 0.5, 2);
                break;

            default:
            case CENTER:
                driveTrain.EncoderAutoMecanumDrive(0, -14, 0, 0.5, 1.5);
                driveTrain.EncoderAutoMecanumDrive(-26, 0, 0, 0.5, 1.75);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 83, 0.5, 1.5);
                driveTrain.EncoderAutoMecanumDrive(-84, 0, 0, 0.5, 3);
                driveTrain.EncoderAutoMecanumDrive(0, 28, 0, 0.5, 2);
                break;

            case RIGHT:
                driveTrain.EncoderAutoMecanumDrive(1, 0, 0, 0.5, 0.25);
                driveTrain.EncoderAutoMecanumDrive(0, 13, 0, 0.5, 1.5);
                driveTrain.EncoderAutoMecanumDrive(-36, 0, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 83, 0.5, 1.5);
                driveTrain.EncoderAutoMecanumDrive(-72, 0, 0, 0.5, 3);
                driveTrain.EncoderAutoMecanumDrive(0, 25, 0, 0.5, 2);
                break;

        }
        attachmentControl.score();
        driveTrain.EncoderAutoMecanumDrive(-24, 0, 0, 0.5, 1);
        driveTrain.fl.setPower(0.5);
        driveTrain.fr.setPower(0.5);
        driveTrain.bl.setPower(0.5);
        driveTrain.br.setPower(0.5);
        sleep(500);
        attachmentControl.boxDoorServo.setPosition(0);
        sleep(2000);
        driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.75, 1);
        attachmentControl.compact();
        driveTrain.EncoderAutoMecanumDrive(0, -30, 0, 0.5, 3);
        while (attachmentControl.liftGroup.isBusy());


    }
}
