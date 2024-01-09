package org.firstinspires.ftc.teamcode;

import com.ftc.waterloo.h2oloobots.*;
import com.ftc.waterloo.h2oloobots.H2OLooTeleOp;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "7247 TeleOp", group = "!")
public class TeleOp7247 extends H2OLooTeleOp {

    @Override
    public void opModeInit() {

        // Setting our drivetrain type to mecanum
        driveTrain.setDriveTrainType(DriveTrain.DriveTrainType.MECANUM);
        cameraControl.close();
    }

    @Override
    public void opModePeriodic() {

        /* Controlling our actual drivetrain. We pass in attachmentControl to slow down the robot
        *  when the servo is extended*/
        driveTrain.teleOpDrive(
                gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,
                attachmentControl
        );

        /* Here we call control for our drone, our intake, our hanging motor, and our hanging servo.
        *  The attachmentControl variable has our desired controls nested in it, so we don't pass in
        *  any buttons.*/
        attachmentControl.liftTeleOp();
//        attachmentControl.droneTeleOp(); // Controller 1 A button launches the drone
        attachmentControl.intakeTeleOp(); /* Controller 1 right bumper toggles the intake,
                                          *  Controller 1 left bumper controls outtake.*/
        attachmentControl.hangMotorManual(); // Controller 2 left joystick controls our winch
        attachmentControl.hangServoTeleOp(); /* Controller 2 A button toggles our servo for
                                            *  deploying the hook between upright, fully extended,
                                             *  and fully retracted positions. */
        attachmentControl.droneTeleOp();
    }

}
