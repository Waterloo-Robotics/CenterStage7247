package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.Encoder;
import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class Auto1 extends H2OLooAuto {

    public void opModeInit() {

        while (opModeIsActive()) cameraControl.telemetryAprilTag();

    }

    public void opModePeriodic() {

        odometryControl.strafeLeft(24, 0.5);

//        while (opModeIsActive()) {
//
//            odometryControl.odoTelemetry(Encoder.MeasurementUnit.PULSES);
//            cameraControl.telemetryAprilTag();
//            telemetryControl.update();
//        }

    }

}
