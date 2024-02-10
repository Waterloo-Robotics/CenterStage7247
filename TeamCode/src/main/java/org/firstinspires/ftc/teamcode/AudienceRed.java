package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.*;
import com.ftc.waterloo.h2oloobots.AttachmentControl;
import com.ftc.waterloo.h2oloobots.CameraControl;
import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Audience Red")
public class AudienceRed extends H2OLooAuto {

    ElapsedTime time = new ElapsedTime();
    CameraControl.PropLocation location;

    @Override
    public void opModeInit() {

        initCamera(CameraControl.Alliance.RED);

        while (opModeInInit()) {
            location = cameraControl.getLocation();
            telemetryControl.addData("Prop Location", location);
            driveTrain.imuTelemetry();
            telemetryControl.update();
        }

    }

    @Override
    public void opModePeriodic() {

        cameraControl.close();

        switch (this.location) {

            case LEFT:

                driveTrain.EncoderAutoMecanumDrive(-20, 11, 0, 0.75, 1.5);
                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(-25, 0, 0, 0.5, 1.25);
                break;

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(-26, 0, -60, 0.75, 1.5);
                break;
        }

        attachmentControl = new AttachmentControl(hardwareMap, telemetryControl, gamepad1, gamepad2);

        switch (this.location) {

            case LEFT:

                driveTrain.EncoderAutoMecanumDrive(2, 0, 0, 0.5, 0.5);
                driveTrain.EncoderAutoMecanumDrive(0, -11, 0, 0.5, 1.5);
                driveTrain.EncoderAutoMecanumDrive(-32, 0, 0, 0.5, 1.75);
                driveTrain.EncoderAutoMecanumDrive(0, 0, -83, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(-72, 0, 0, 0.5, 3);
                driveTrain.EncoderAutoMecanumDrive(0, -26, 0, 0.5, 2);
                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(2, 0, 0, 0.5, 0.75);
                driveTrain.EncoderAutoMecanumDrive(0, 18, 0, 0.5, 1.5);
                driveTrain.EncoderAutoMecanumDrive(-29, 0, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(0, 0, -87, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(-92, 0, 0, 0.5, 3.75);
                driveTrain.EncoderAutoMecanumDrive(0, -28.5, 0, 0.5, 3.75);
                break;

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.5, 0.5);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 70, 0.5, 1.25);
                driveTrain.EncoderAutoMecanumDrive(-30, 0, 0, 0.5, 1.25);
                driveTrain.EncoderAutoMecanumDrive(0, 0, -90, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(-78, 0, 0, 0.5, 3);
                break;
        }

        attachmentControl.score();
        sleep(1500);

        switch (location) {

            case LEFT:
                driveTrain.EncoderAutoMecanumDrive(-15, 0, 0, 0.5, 1);
                break;

            case CENTER:
                driveTrain.EncoderAutoMecanumDrive(-13, 0, 3, 0.5, 2);
                break;

            case RIGHT:
                driveTrain.EncoderAutoMecanumDrive(-15, -33, 0, 0.5, 1);
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
                driveTrain.EncoderAutoMecanumDrive(0, 18, 0, 0.5, 2);
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
