package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.DriveTrain;
import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;



@TeleOp(name = "Mecanum TeleOp", group = "!")
public class DriveTele extends H2OLooTeleOp {

    final int MAX = 1;
    final double MIN = 0.5;

    final int REV = 0;
    Servo servo1;
    Servo servo_cont;
    DcMotor spinner;
    boolean lastB;
    boolean lastY;
    int state;
    int state2;
    @Override
    public void opModeInit() {
        servo_cont = hardwareMap.get(Servo.class, "servo_cont");
        spinner = hardwareMap.get(DcMotor.class, "spinner");
        servo1 = hardwareMap.get(Servo.class, "servo");

        driveTrain.setDriveTrainType(DriveTrain.DriveTrainType.MECANUM);

        state = 0;
    }

    @Override
    public void opModePeriodic() {

            driveTrain.teleOpDrive(
                    gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x
            );

            if (gamepad1.b && (lastB != gamepad1.b) && (state == 0)) {
                spinner.setPower(1);
                servo_cont.setPosition(REV);
                state = 1;
            }
            else if (gamepad1.b && (lastB != gamepad1.b) && (state == 1)){
                spinner.setPower(0);
                servo_cont.setPosition(MIN);
                state = 0;
            }
            if (gamepad1.y && (lastY != gamepad1.y) && (state2 == 0)){
                spinner.setPower(-1);
                servo_cont.setPosition(MAX);
                state2 = 1;
                state = 1;
            }
            else if (gamepad1.y && (lastY != gamepad1.y) && (state2 == 1)){
                spinner.setPower(0);
                servo_cont.setPosition(MIN);
                state2 = 0;
                state = 0;
            }

            if (gamepad1.a){
                servo1.setPosition(MIN);
            }
            if(!gamepad1.a){
                servo1.setPosition(REV);
            }
            lastB = gamepad1.b;
            lastY = gamepad1.y;
    }

}
