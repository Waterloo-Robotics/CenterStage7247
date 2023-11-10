package org.firstinspires.ftc.teamcode.tests;

import com.ftc.waterloo.h2oloobots.AttachmentControl;
import com.ftc.waterloo.h2oloobots.BlueTeamPropPipeline;
import com.ftc.waterloo.h2oloobots.RedTeamPropPipeline;
import com.ftc.waterloo.h2oloobots.DriveTrain;
import com.ftc.waterloo.h2oloobots.RedTeamPropPipeline;
import com.ftc.waterloo.h2oloobots.TelemetryControl;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@TeleOp
public class PropTest extends LinearOpMode {

    OpenCvCamera camera;
    BlueTeamPropPipeline pipeline;
    TelemetryControl telemetryControl;
    DriveTrain driveTrain;
    AttachmentControl attachmentControl;

    public void runOpMode() {

        telemetryControl = new TelemetryControl(telemetry);
        attachmentControl = new AttachmentControl(hardwareMap, telemetryControl, gamepad1, gamepad2);
        driveTrain = new DriveTrain(hardwareMap, telemetryControl, attachmentControl);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new BlueTeamPropPipeline();

        camera.setPipeline(pipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        waitForStart();

        while (opModeIsActive()) {

            telemetryControl.startCameraStream(camera, 60);

            telemetryControl.addData("Analysis", RedTeamPropPipeline.propPosition);
            telemetryControl.update();

            driveTrain.teleOpDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        }

    }

}
