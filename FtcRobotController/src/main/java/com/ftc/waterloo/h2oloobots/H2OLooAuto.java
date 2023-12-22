package com.ftc.waterloo.h2oloobots;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.ftc.waterloo.h2oloobots.*;

public abstract class H2OLooAuto extends LinearOpMode {

    public CameraControl.Alliance alliance;
    public H2OLooAuto() {}
    public TelemetryControl telemetryControl;

    public OdometryControl odometryControl;
    public DriveTrain driveTrain;
    public AttachmentControl attachmentControl;

    public CameraControl cameraControl;

    public void runOpMode() {

        telemetryControl = new TelemetryControl(telemetry);
//        odometryControl = new OdometryControl(hardwareMap, telemetryControl);
        attachmentControl = new AttachmentControl(hardwareMap, telemetryControl, gamepad1, gamepad2);
        driveTrain = new DriveTrain(hardwareMap, telemetryControl, attachmentControl, DcMotor.ZeroPowerBehavior.BRAKE);

        this.opModeInit();

        waitForStart();

        this.opModePeriodic();

    }

    public void initCamera(CameraControl.Alliance alliance) {

        this.alliance = alliance;
        this.cameraControl = new CameraControl(hardwareMap, telemetryControl, this.alliance, gamepad1, gamepad2);

    }

    abstract public void opModeInit();

    abstract public void opModePeriodic();

}
