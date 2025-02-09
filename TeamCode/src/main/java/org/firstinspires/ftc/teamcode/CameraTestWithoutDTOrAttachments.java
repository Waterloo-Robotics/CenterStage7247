package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.CameraControl;
import com.ftc.waterloo.h2oloobots.TelemetryControl;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class CameraTestWithoutDTOrAttachments extends OpMode {

    TelemetryControl telemetryControl;
    CameraControl cameraControl;

    public void init() {

        telemetryControl = new TelemetryControl(telemetry);
        cameraControl = new CameraControl(hardwareMap, telemetryControl, CameraControl.Alliance.BLUE, gamepad1, gamepad2);

    }

    public void loop() {

        cameraControl.stream();
        telemetryControl.addData("Camera Location", cameraControl.getLocation());
        telemetryControl.update();

    }

}
