package org.firstinspires.ftc.teamcode.tests;

import com.ftc.waterloo.h2oloobots.AttachmentControl;
import com.ftc.waterloo.h2oloobots.TelemetryControl;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Attachment Test Without DriveTrain")
public class AttachmentTestWithoutDrive extends OpMode {

    TelemetryControl telemetryControl;
    AttachmentControl attachmentControl;

    public void init() {

        telemetryControl = new TelemetryControl(telemetry);
        attachmentControl = new AttachmentControl(hardwareMap, telemetryControl, gamepad1, gamepad2);

    }

    public void loop() {

//        attachmentControl.intakeTeleOp();
        attachmentControl.boxManual();
        attachmentControl.hangServoManual();
//        attachmentControl.droneManual();
        telemetryControl.update();

    }

}
