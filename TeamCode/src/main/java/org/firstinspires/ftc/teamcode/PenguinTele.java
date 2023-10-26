package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.DriveTrain;
import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;



@TeleOp(name = "Madagas Tele", group = "!")
public class PenguinTele extends H2OLooTeleOp {

    Servo servo1;

    @Override
    public void opModeInit() {

        servo1 = hardwareMap.get(Servo.class, "servo");
    }

    @Override
    public void opModePeriodic() {


        if (gamepad1.a){
            servo1.setPosition(1);
        }
        if (!gamepad1.a) {
            servo1.setPosition(0.5);
        }

    }

}
