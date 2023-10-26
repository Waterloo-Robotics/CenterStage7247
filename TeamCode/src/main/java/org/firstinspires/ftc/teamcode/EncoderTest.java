package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class EncoderTest extends H2OLooTeleOp {

    @Override
    public void opModeInit() {

    }

    @Override
    public void opModePeriodic() {

        driveTrain.driveEncoderRawTelemetry();

    }

}
