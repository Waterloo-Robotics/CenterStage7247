package org.firstinspires.ftc.teamcode.tests;

import com.ftc.waterloo.h2oloobots.AttachmentControl;
import com.ftc.waterloo.h2oloobots.H2OLooAuto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous
public class PickupInAutoTest extends H2OLooAuto {

    public void opModeInit() {

    }

    public void opModePeriodic() {
        attachmentControl = new AttachmentControl(hardwareMap, telemetryControl, gamepad1, gamepad2);

        attachmentControl.intakeAuto();
        driveTrain.EncoderAutoMecanumDrive(24, 0, 0, 0.375, 1.25);
        driveTrain.fl.setPower(-0.35);
        driveTrain.fr.setPower(-0.35);
        driveTrain.bl.setPower(-0.35);
        driveTrain.br.setPower(-0.35);
        sleep(1500);
//        attachmentControl.intakeStop();
        driveTrain.EncoderAutoMecanumDrive(-12, 0, 0, 0.75, 3);
        attachmentControl.intakeSpit();
        sleep(3000);


    }

}
