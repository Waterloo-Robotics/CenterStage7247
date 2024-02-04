package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.AttachmentControl;
import com.ftc.waterloo.h2oloobots.CameraControl;
import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Stage Blue")
public class StageBlue extends H2OLooAuto {

    ElapsedTime time = new ElapsedTime();
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

                driveTrain.EncoderAutoMecanumDrive(-20, 8, 0, 0.75, 1.5);
                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(-26, 0, 0, 0.5, 1.25);
                break;

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(-24, 0, -55, 0.75, 1.5);
                break;
        }

        attachmentControl = new AttachmentControl(hardwareMap, telemetryControl, gamepad1, gamepad2);

        switch (this.location) {

            case LEFT:

                driveTrain.EncoderAutoMecanumDrive(0, 22, 0, 0.5, 2.5);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 83, 0.5, 1.5);
                driveTrain.EncoderAutoMecanumDrive(0, -2, 0, 0.5, 1.5);
                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(2, 0, 0, 0.5, 0.25);
                driveTrain.EncoderAutoMecanumDrive(0, 22, 0, 0.5, 2.5);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 83, 0.5, 2.5);
                break;

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.5, 0.5);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 145, 0.5, 3);
                driveTrain.EncoderAutoMecanumDrive(-18.5, -15, -1, 0.5, 1.5);
                break;
        }

        attachmentControl.score();
        sleep(1500);

        switch (location) {

            case LEFT:
                driveTrain.EncoderAutoMecanumDrive(-15, 0, 0, 0.5, 1);
                break;

            case CENTER:
                driveTrain.EncoderAutoMecanumDrive(-15, 0, 0, 0.5, 1);
                break;

            case RIGHT:
                driveTrain.EncoderAutoMecanumDrive(-15, 0, 0, 0.5, 1);
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
        driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.75, 1);

        switch (location) {

            case LEFT:
                driveTrain.EncoderAutoMecanumDrive(0, 16, 0, 0.5, 2);
                break;

            case CENTER:
                driveTrain.EncoderAutoMecanumDrive(0, 24, 0, 0.5, 2);
                break;

            case RIGHT:
                driveTrain.EncoderAutoMecanumDrive(0, 33, 0, 0.5, 2);
                break;

        }
        attachmentControl.compact();

    }
}
