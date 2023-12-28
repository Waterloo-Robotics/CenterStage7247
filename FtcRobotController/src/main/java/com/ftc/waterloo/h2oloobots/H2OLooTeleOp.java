package com.ftc.waterloo.h2oloobots;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.ftc.waterloo.h2oloobots.*;

public abstract class H2OLooTeleOp extends LinearOpMode {

    CameraControl.Alliance alliance = CameraControl.Alliance.RED;

    public H2OLooTeleOp() {}
    public TelemetryControl telemetryControl;

    public DriveTrain driveTrain;
    public AttachmentControl attachmentControl;

    public CameraControl cameraControl;

    public void runOpMode() {

        telemetryControl = new TelemetryControl(telemetry);
        driveTrain = new DriveTrain(hardwareMap, telemetryControl, attachmentControl);
        attachmentControl = new AttachmentControl(hardwareMap, telemetryControl, gamepad1, gamepad2);
        cameraControl = new CameraControl(hardwareMap, telemetryControl, alliance, gamepad1, gamepad2);

        this.opModeInit();
        cameraControl.stream();

        waitForStart();

        while (opModeIsActive()) {
            this.opModePeriodic();
            cameraControl.stream();
            telemetryControl.update();
        }

    }

    public void initCamera(CameraControl.Alliance alliance) {

        this.alliance = alliance;
        this.cameraControl = new CameraControl(hardwareMap, telemetryControl, this.alliance, gamepad1, gamepad2);

    }

    abstract public void opModeInit();

    abstract public void opModePeriodic();

}
