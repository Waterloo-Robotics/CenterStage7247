package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.*;
import com.ftc.waterloo.h2oloobots.AttachmentControl;
import com.ftc.waterloo.h2oloobots.CameraControl;
import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Audience Blue")
public class AudienceBlue extends H2OLooAuto {

    ElapsedTime time = new ElapsedTime();
    CameraControl.PropLocation location;

    @Override
    public void opModeInit() {

        initCamera(CameraControl.Alliance.BLUE);

        while (opModeInInit()) {
            cameraControl.stream();
            location = cameraControl.getLocation();
            telemetryControl.addData("Prop Location", location);
            telemetryControl.update();
        }

    }

    @Override
    public void opModePeriodic() {

        time.reset();
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
                driveTrain.EncoderAutoMecanumDrive(4, 0, 39, 0.5, 1.5);
                driveTrain.EncoderAutoMecanumDrive(0, -31, 0, 0.5, 2);
//                driveTrain.imuAbsTurn(90, 2);
                attachmentControl.scoreAudience();
                sleep(6000);
                driveTrain.EncoderAutoMecanumDrive(-78, 0, 0, 0.5, 3);
//                driveTrain.imuAbsTurn(90, 2);
                attachmentControl.scoreAudience2();
                driveTrain.EncoderAutoMecanumDrive(0, 38, 0, 0.5, 2);
                break;

            default:
            case CENTER:
                driveTrain.EncoderAutoMecanumDrive(3, 0, 0, 0.5, 1);
                driveTrain.EncoderAutoMecanumDrive(0, -14, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(-25, 0, 0, 0.5, 4);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 83, 0.5, 1.5);
//                driveTrain.imuAbsTurn(90, 2);
                attachmentControl.scoreAudience();
                sleep(7000);
                driveTrain.EncoderAutoMecanumDrive(-92, 0, 0, 0.5, 3);
//                driveTrain.imuAbsTurn(90, 2);
                attachmentControl.scoreAudience2();
                driveTrain.EncoderAutoMecanumDrive(0, 27, 0, 0.5, 2);
                break;

            case RIGHT:
                driveTrain.EncoderAutoMecanumDrive(2, 0, 0, 0.5, 0.75);
                driveTrain.EncoderAutoMecanumDrive(0, 13, 0, 0.5, 1.5);
                driveTrain.EncoderAutoMecanumDrive(-36, 0, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 83, 0.5, 1.5);
//                driveTrain.imuAbsTurn(90, 3);
                attachmentControl.scoreAudience();
                sleep(6000);
                driveTrain.EncoderAutoMecanumDrive(-76, 0, 0, 0.5, 3);
//                driveTrain.imuAbsTurn(90, 2);
                attachmentControl.scoreAudience2();
                driveTrain.EncoderAutoMecanumDrive(0, 24.5, 0, 0.5, 2);
                break;

        }


        switch (location) {

            case LEFT:
                driveTrain.EncoderAutoMecanumDrive(-7, 0, 3, 0.5, 1);
                break;

            case CENTER:
                driveTrain.EncoderAutoMecanumDrive(-6, 0, 0, 0.5, 1);
                break;

            case RIGHT:
                driveTrain.EncoderAutoMecanumDrive(-6, 0, 0, 0.5, 1);
                break;

        }

        driveTrain.fl.setPower(0.35);
        driveTrain.fr.setPower(0.35);
        driveTrain.bl.setPower(0.35);
        driveTrain.br.setPower(0.35);
        sleep(500);
        attachmentControl.drop();
        sleep(1000);
        attachmentControl.lift();
        sleep(500);

        switch (location) {

            case LEFT:
                driveTrain.EncoderAutoMecanumDrive(8, 0, 0, 0.75, 1);
                driveTrain.EncoderAutoMecanumDrive(0, -36, 0, 0.5, (28.0 - time.seconds()));
                break;

            case CENTER:
                driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.75, 1);
                driveTrain.EncoderAutoMecanumDrive(0, -24, 0, 0.5, (28.0 - time.seconds()));
                break;

            case RIGHT:
                driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.75, 1);
                driveTrain.EncoderAutoMecanumDrive(0, -20, 0, 0.5, (28.0 - time.seconds()));
                break;

        }
        attachmentControl.compactAudience();
        while (!isStopRequested());


    }
}
