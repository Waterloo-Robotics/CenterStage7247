package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.Encoder;
import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class Auto1 extends H2OLooAuto {

    public void opModeInit() {

//        while (opModeIsActive()) cameraControl.telemetryAprilTag();

    }

    public void opModePeriodic() {

        driveTrain.EncoderAutoMecanumDrive(10, 0, 0, 0.75, 3);
//        sleep(500);
//        driveTrain.EncoderAutoMecanumDrive(0, 0, -90, 0.75, 5);
//        sleep(500);
//        driveTrain.EncoderAutoMecanumDrive(72, 0, 0, 0.75, 8);

//        odometryControl.strafeLeft(24, 0.5);

//        while (opModeIsActive()) {
//
//            odometryControl.odoTelemetry(Encoder.MeasurementUnit.PULSES);
//            cameraControl.telemetryAprilTag();
//            telemetryControl.update();
//        }

    }

}
