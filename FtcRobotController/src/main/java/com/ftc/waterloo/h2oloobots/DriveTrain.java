package com.ftc.waterloo.h2oloobots;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Config
public class DriveTrain {


    public enum DriveTrainType {

        TWO_WHEEL_DRIVE,
        FOUR_WHEEL_TANK,
        MECANUM

    }

    public enum DriveDirection {

        FORWARD,
        BACKWARD,
        STRAFE_LEFT,
        STRAFE_RIGHT,
        TURN_CLOCKWISE,
        TURN_COUNTERCLOCKWISE,
        DIAGONAL_FORWARD_RIGHT,
        DIAGONAL_FORWARD_LEFT,
        DIAGONAL_BACKWARD_RIGHT,
        DIAGONAL_BACKWARD_LEFT,

    }

    DriveTrainType driveTrainType = DriveTrainType.MECANUM;

    // defines drive motors for a 4 wheel drive
    public DcMotor fl, fr, bl, br;

    // defines drive motors for a two wheel drive
    public DcMotor left, right;

    /* counts per revolution of the drive motors. None of this is necessary if you aren't using
     * the encoder drive in this file. */
    double countsPerRevolution = 384.5;

    // diameter of your drive wheels
    double wheelDiameter = 96 / 25.4; // inches
    double wheelCircumference = wheelDiameter * Math.PI;

    BHI260IMU imu;

    /*
     * Track Width is the distance between the two sets of wheels (defined by the line of x below).
     *
     * TODO this will need to be updated for turns to work. it might not be the actual value
     *  that is physically measured.
     *
     *          FRONT
     *  O--------------------O
     *  |                    |
     *  |                    |
     *  |                    |
     *  |xxxxxxxxxxxxxxxxxxxx|
     *  |                    |
     *  |                    |
     *  |                    |
     *  O--------------------O
     */
    public static double trackWidth = 28.75;
    // how many inches the wheels travel in a full rotation of the robot
    final double fullRotation = trackWidth * Math.PI;

    // calculates how many encoder counts are in an inch and in a degree
    final double COUNTS_PER_INCH = countsPerRevolution / wheelCircumference;
    final double COUNTS_PER_DEGREE = fullRotation / wheelCircumference * countsPerRevolution / 360.0;

    // defines local HardwareMap and TelemetryControl variables.
    HardwareMap hardwareMap;
    TelemetryControl telemetryControl;
    AttachmentControl attachmentControl;

    /**Initialises the drivetrain variable.
     * @param hardwareMap the local HardwareMap variable from in the runOpMode() void.
     * @param telemetryControl the TelemetryControl variable initialized in the runOpMode() void.*/
    public DriveTrain(HardwareMap hardwareMap,
                      TelemetryControl telemetryControl
    ) {

        this.hardwareMap = hardwareMap;
        this.telemetryControl = telemetryControl;
        this.FourMotorInit();

    }

    /**Initialises the drivetrain variable.
     * @param hardwareMap the local HardwareMap variable from in the runOpMode() void.
     * @param telemetryControl the TelemetryControl variable initialized in the runOpMode() void.
     * @param zeroPowerBehavior the zero power behavior to be set to the drive motors.*/
    public DriveTrain(
            HardwareMap hardwareMap,
            TelemetryControl telemetryControl,
            DcMotor.ZeroPowerBehavior zeroPowerBehavior
    ) {

        this.hardwareMap = hardwareMap;
        this.telemetryControl = telemetryControl;
        this.FourMotorInit(zeroPowerBehavior);

    }

    /**Sets the drivetrain type after the constructor.
     * @param driveTrainType the DriveTrain type to be set*/
    public void setDriveTrainType(@NonNull DriveTrainType driveTrainType) {

        this.driveTrainType = driveTrainType;

        switch (driveTrainType) {

            case FOUR_WHEEL_TANK:
            case MECANUM:
                this.FourMotorInit();
                break;

            default:
                this.FourMotorInit();
                break;

        }

    }

    /**Sets the drivetrain type after the constructor.
     * @param driveTrainType the DriveTrain type to be set
     * @param zeroPowerBehavior the behavior of the motors when they are not receiving power*/
    public void setDriveTrainType(@NonNull DriveTrainType driveTrainType, @NonNull DcMotor.ZeroPowerBehavior zeroPowerBehavior) {

        this.driveTrainType = driveTrainType;

        switch (driveTrainType) {

            case FOUR_WHEEL_TANK:
            case MECANUM:
                this.FourMotorInit(zeroPowerBehavior);
                break;

            default:
                this.FourMotorInit(zeroPowerBehavior);
                break;

        }

    }

    /**Four motor initialization command for any four motor drive base with two motors on either
     * side. Could be mecanum or tank.*/
    void FourMotorInit() {

        this.FourMotorInit(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    /**Four motor initialization command for any four motor drive base with two motors on either
     * side. Could be mecanum or tank.
     * @param zeroPowerBehavior the zero power behavior to set to the motors.*/
    void FourMotorInit(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {

        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");

        fl.setZeroPowerBehavior(zeroPowerBehavior);
        fr.setZeroPowerBehavior(zeroPowerBehavior);
        bl.setZeroPowerBehavior(zeroPowerBehavior);
        br.setZeroPowerBehavior(zeroPowerBehavior);

        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
//        bl.setDirection(DcMotorSimple.Direction.REVERSE);
//        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = hardwareMap.get(BHI260IMU.class, "imu");

        BHI260IMU.Parameters parameters = new BHI260IMU.Parameters(
                new RevHubOrientationOnRobot(
                                             RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                                             RevHubOrientationOnRobot.UsbFacingDirection.UP)
        );

        imu.initialize(parameters);

    }

    public void imuTelemetry() {

        telemetryControl.addData("IMU Pitch", imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES));
        telemetryControl.addData("IMU Yaw", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        telemetryControl.addData("IMU Roll", imu.getRobotYawPitchRollAngles().getRoll(AngleUnit.DEGREES));

    }

    /**TeleOp code for a mecanum drive.
     * @param FBInput forward and back input, range -1 to 1
     * @param LRInput left and right strafing input, range -1 to 1
     * @param pivotInput pivot input, range -1 to 1*/
    public void teleOpDrive(double FBInput, double LRInput, double pivotInput, AttachmentControl attachmentControl) {

        double speedMul = 1;
        switch (driveTrainType) {

            case TWO_WHEEL_DRIVE:
            case FOUR_WHEEL_TANK:
                RobotLog.setGlobalErrorMsg("Passing in a LRInput is unacceptable. Please use the " +
                        "version of this function that uses 'teleOpDrive(FBInput," +
                        " pivotInput)'.");
                break;

            case MECANUM:

                if (attachmentControl.hangServo.getPosition() > 0.75) {

                    speedMul = 0.5;

                } else speedMul = 1;
                this.MecanumTeleOp(FBInput * speedMul, LRInput, pivotInput);
                break;

        }

    }

    /**Simple Mecanum drive TeleOp.
     * @param FBInput input used for forward and back movements.
     * @param LRInput input used for strafing left and right.
     * @param PivotInput input used for turning.*/
    public void MecanumTeleOp(double FBInput, double LRInput, double PivotInput) {

        double frPower = FBInput + LRInput + (PivotInput);
        double brPower = FBInput - LRInput + (PivotInput);
        double flPower = FBInput - LRInput - (PivotInput);
        double blPower = FBInput + LRInput - (PivotInput);

        fr.setPower(frPower);
        br.setPower(brPower);
        fl.setPower(flPower);
        bl.setPower(blPower);

        telemetryControl.motorTelemetryUpdate(
                fl.getPower(),
                fr.getPower(),
                bl.getPower(),
                br.getPower()
        );

    }

    public void pushOnBackdrop() {

        fl.setPower(1);
        fr.setPower(1);
        bl.setPower(1);
        br.setPower(1);

    }

    /**Simple Autonomous code to drive at set powers for a set time.
     * @param FRPower The power to be set to the front right motor.
     * @param FLPower The power to be set to the front left motor.
     * @param BRPower The power to be set to the back right motor.
     * @param BLPower The power to be set to the back left motor.
     * @param SECONDS The time for the movement to occur over.*/
    public void timeAutoDrive(double FRPower, double FLPower, double BRPower, double BLPower, double SECONDS) {

        if (this.driveTrainType == DriveTrainType.TWO_WHEEL_DRIVE) {

            RobotLog.setGlobalErrorMsg("You must use only two power arguments for a 2 wheel drive.");

        } else {

            this.timeAutoFourWheelDrive(FRPower, FLPower, BRPower, BLPower, SECONDS);

        }

    }

    /**Simple Four Wheel Drive Autonomous code to drive at set powers for a set time.
     * @param FRPower The power to be set to the front right motor.
     * @param FLPower The power to be set to the front left motor.
     * @param BRPower The power to be set to the back right motor.
     * @param BLPower The power to be set to the back left motor.
     * @param SECONDS The time for the movement to occur over.*/
    public void timeAutoFourWheelDrive(double FRPower, double FLPower, double BRPower, double BLPower, double SECONDS) {

        ElapsedTime time = new ElapsedTime();

        time.reset();

        while (time.seconds() < SECONDS) {

            fr.setPower(FRPower);
            fl.setPower(FLPower);
            br.setPower(BRPower);
            bl.setPower(BLPower);

        }

        fr.setPower(0);
        fl.setPower(0);
        br.setPower(0);
        bl.setPower(0);

    }

    /**Four Wheel Autonomous for a Mecanum Drivetrain using inches and degrees
     * @param INCHES_FB The inches forwards/backwards to drive (positive is forwards,
     *        negative is backwards)
     * @param INCHES_LR The inches left/right to drive (positive is right,
     *        negative is left)
     * @param DEGREES_TURN The degrees to turn (positive is clockwise,
     *        negative is counterclockwise)*/
    public void EncoderAutoMecanumDrive(double INCHES_FB, double INCHES_LR, double DEGREES_TURN, double SPEED, double time) {

        ElapsedTime timer = new ElapsedTime();

        if (DEGREES_TURN > 180) {

            DEGREES_TURN -= 360;

        }

        int frTargetPosition = fr.getCurrentPosition()
                - (int) (this.COUNTS_PER_INCH * INCHES_FB)
                + (int) (this.COUNTS_PER_INCH * INCHES_LR)
                - (int) (this.COUNTS_PER_DEGREE * DEGREES_TURN);
        int brTargetPosition = br.getCurrentPosition()
                - (int) (this.COUNTS_PER_INCH * INCHES_FB)
                - (int) (this.COUNTS_PER_INCH * INCHES_LR)
                - (int) (this.COUNTS_PER_DEGREE * DEGREES_TURN);
        int flTargetPosition = fl.getCurrentPosition()
                - (int) (this.COUNTS_PER_INCH * INCHES_FB)
                - (int) (this.COUNTS_PER_INCH * INCHES_LR)
                + (int) (this.COUNTS_PER_DEGREE * DEGREES_TURN);
        int blTargetPosition = bl.getCurrentPosition()
                - (int) (this.COUNTS_PER_INCH * INCHES_FB)
                + (int) (this.COUNTS_PER_INCH * INCHES_LR)
                + (int) (this.COUNTS_PER_DEGREE * DEGREES_TURN);

        fr.setTargetPosition(frTargetPosition);
        br.setTargetPosition(brTargetPosition);
        fl.setTargetPosition(flTargetPosition);
        bl.setTargetPosition(blTargetPosition);

        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        timer.reset();

        while ((fr.isBusy() ||
                br.isBusy() ||
                fl.isBusy() ||
                bl.isBusy()) &&
                timer.seconds() <= time) {

            fr.setPower(SPEED);
            br.setPower(SPEED);
            fl.setPower(SPEED);
            bl.setPower(SPEED);

            this.imuTelemetry();
            telemetryControl.update();

        }

        fr.setPower(0);
        br.setPower(0);
        fl.setPower(0);
        bl.setPower(0);

        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ElapsedTime waitTimer = new ElapsedTime();
        waitTimer.reset();
        while (waitTimer.seconds() <= 0.25);

    }

    public void driveEncoderRawTelemetry() {

        telemetryControl.addData("Front Left Position", fl.getCurrentPosition());
        telemetryControl.addData("Front Right Position", fr.getCurrentPosition());
        telemetryControl.addData("Back Left Position", bl.getCurrentPosition());
        telemetryControl.addData("Back Right Position", br.getCurrentPosition());

    }

}
