package org.firstinspires.ftc.teamcode.tests;

import com.ftc.waterloo.h2oloobots.*;
import com.ftc.waterloo.h2oloobots.TelemetryControl;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class IMUTest extends OpMode {

    TelemetryControl telemetryControl;
    DriveTrain driveTrain;

    public void init() {

        telemetryControl = new TelemetryControl(telemetry);
        driveTrain = new DriveTrain(hardwareMap, telemetryControl, DcMotor.ZeroPowerBehavior.FLOAT);

    }

    public void loop() {

        driveTrain.imuTelemetry();
        telemetryControl.update();

    }

}
