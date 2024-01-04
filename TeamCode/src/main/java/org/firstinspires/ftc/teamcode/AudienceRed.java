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

                driveTrain.EncoderAutoMecanumDrive(-24, 0, 0, 0.5, 1.25);
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
                driveTrain.EncoderAutoMecanumDrive(-30, 0, 0, 0.5, 1.75);
                driveTrain.EncoderAutoMecanumDrive(0, 0, -83, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(-72, 0, 0, 0.5, 3);
                driveTrain.EncoderAutoMecanumDrive(0, -32, 0, 0.5, 2);
                break;

            default:
            case CENTER:

                driveTrain.EncoderAutoMecanumDrive(2, 0, 0, 0.5, 0.75);
                driveTrain.EncoderAutoMecanumDrive(0, 18, 0, 0.5, 1.5);
                driveTrain.EncoderAutoMecanumDrive(-29, 0, 0, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(0, 0, -85, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(-84, 0, 0, 0.5, 6);
                driveTrain.EncoderAutoMecanumDrive(0, -33, 0, 0.5, 2);
                break;

            case RIGHT:

                driveTrain.EncoderAutoMecanumDrive(4, 0, 0, 0.5, 0.5);
                driveTrain.EncoderAutoMecanumDrive(0, 0, 70, 0.5, 1.25);
                driveTrain.EncoderAutoMecanumDrive(-30, 0, 0, 0.5, 1.25);
                driveTrain.EncoderAutoMecanumDrive(0, 0, -90, 0.5, 2);
                driveTrain.EncoderAutoMecanumDrive(-78, 0, 0, 0.5, 3);
                driveTrain.EncoderAutoMecanumDrive(0, -35, 0, 0.5, 2);
                break;
        }

//        cameraControl.initAprilTag(hardwareMap);

//        time.reset();
//        while (time.seconds() < 1.5) {
//            cameraControl.followAprilTagAuto(driveTrain);
//        }
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
        driveTrain.EncoderAutoMecanumDrive(0, 26, 0, 0.5, 3);
        while (attachmentControl.liftGroup.isBusy());

    }
}
