package com.ftc.waterloo.h2oloobots;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


/**This is for 3 wheel odometry, typically mecanum but could be for any 4 wheel drive.*/
@Config
public class OdometryControl {

    public DcMotorEx fl, fr, bl, br;

    public Encoder leftEncoder, rightEncoder, horizEncoder;


    /** We use a proportional gain controller for our odometry, these are just different
     * controller's gain levels.*/
    public static double P_gen_forwardback = 0.02;
    public static double P_gen_strafe = 0.02;
    public static double P_gen_turn = 0.03;
    public static double P_differential = 0.2;
    public static double P_diff_strafe = 0.0075;

    /** These variables store the position at the start of the move.
     * They are necessary to maintain knowledge of where we are on the field. */
    double leftPositionAtStart = 0;
    double rightPositionAtStart = 0;
    double horizPositionAtStart = 0;

    /** Range for all of these: 0-1
     * @MAX_POWER - Maximum power the motors can be set to
     *  @MIN_POWER_STRAIGHT - Minimum power possible to be set for forward or back movements.
     *  @MIN_POWER_TURNSTRAFE - Minimum power possible to be set for turning or strafing.
     *                          This is typically higher than the straight minimum power.
     */
    final double MAX_POWER = 0.85;
    final double MIN_POWER_STRAIGHT = 0.3;
    final double MIN_POWER_TURNSTRAFE = 0.55;

    /**Encoder Definitions*/
    double countsPerRevolution = 8192; // counts per revolution on external encoder
    public final double deadWheelDiameter = 35.0; // measured in mm
    public final double deadWheelCircumference = deadWheelDiameter * Math.PI; // still mm

    public final double gearRatio = 1.0; // leave at 1 unless you are gearing your encoder
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
    double trackWidth = 1.0;
    // fullRotation: the amount the wheels travel in a full rotation
    final double fullRotation = trackWidth * Math.PI;
    // countsPerRobotRev: the number of encoder counts that are recorded in each complete robot revolution.
    final double countsPerRobotRev = fullRotation * (countsPerRevolution / deadWheelCircumference);
    final double countsPerDegree = countsPerRobotRev / 360;
    final double inchesPerDegree = toInches(countsPerDegree);

    TelemetryControl telemetryControl;

    ElapsedTime time = new ElapsedTime();

    public OdometryControl(HardwareMap hardwareMap, TelemetryControl telemetryControl) {

        fl = (DcMotorEx) hardwareMap.dcMotor.get("fl");
        fr = (DcMotorEx) hardwareMap.dcMotor.get("fr");
        bl = (DcMotorEx) hardwareMap.dcMotor.get("bl");
        br = (DcMotorEx) hardwareMap.dcMotor.get("br");

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // use any of the following if motors need reversed
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
//        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
//        br.setDirection(DcMotorSimple.Direction.REVERSE);

        // The device name should match the device name of the motor it is plugged in to.
        leftEncoder = new Encoder(
                hardwareMap,
                "fl",
                8192,
                deadWheelDiameter,
                Encoder.MeasurementUnit.MM,
                gearRatio
        );
        rightEncoder = new Encoder(
                hardwareMap,
                "bl",
                8192,
                deadWheelDiameter,
                Encoder.MeasurementUnit.MM,
                gearRatio
        );
        horizEncoder = new Encoder(
                hardwareMap,
                "fr",
                8192,
                deadWheelDiameter,
                Encoder.MeasurementUnit.MM,
                gearRatio
        );

        // if any encoders need reversed, do so here.
//        leftEncoder.setInverted(true);
        rightEncoder.setInverted(true);
//        horizEncoder.setInverted(true);

        leftEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        horizEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // defines the local telemetryControl value to reflect that of the parent class.
        this.telemetryControl = telemetryControl;

    }

    /**Returns the current distance of each encoder in the default Measurement Unit*/
    public void odoTelemetry() {

        telemetryControl.addData("Left Enc", leftEncoder.getDistance());
        telemetryControl.addData("Right Enc", rightEncoder.getDistance());
        telemetryControl.addData("Horiz Enc", horizEncoder.getDistance());

    }

    /**Returns the current distance of each encoder in the specified Measurement Unit
     * @param measurementUnit the Measurement Unit to use.*/
    public void odoTelemetry(Encoder.MeasurementUnit measurementUnit) {

        telemetryControl.addData("Left Enc", leftEncoder.getDistance(measurementUnit));
        telemetryControl.addData("Right Enc", rightEncoder.getDistance(measurementUnit));
        telemetryControl.addData("Horiz Enc", horizEncoder.getDistance(measurementUnit));

    }

    /** Updates the positionAtStart values to the current position. */
    public void zeroEncoders() {

        leftPositionAtStart = leftEncoder.getDistance(Encoder.MeasurementUnit.IN);
        rightPositionAtStart = rightEncoder.getDistance(Encoder.MeasurementUnit.IN);
        horizPositionAtStart = horizEncoder.getDistance(Encoder.MeasurementUnit.IN);

    }


    /**Drive forward
     * @param INCHES amount of inches to go forward
     * @param initialDelay A timeout to wait before starting the movement. This is useful for
     *              using consecutive movements, when different movements can bleed into
     *              each other without a proper delay between movements.
     */
    public void forward(double INCHES, double initialDelay) {

        double rightOffset = 0, leftOffset = 0, genPower = 0, rightPower = 0, leftPower = 0;

        double distanceTravelled = 0;
        double error = 0;
        double differentialError = 0;

        double rightTravelled, leftTravelled;

        boolean destinationReached = false;

        double seconds = 0;
        zeroEncoders(); // resets encoders

        boolean waited = false;

        time.reset();
        // while loop that only ends once distance is reached or the command hits the timeout (10 seconds)
        while (!destinationReached && time.seconds() < 10 + initialDelay) {

            rightTravelled = rightEncoder.getDistance(Encoder.MeasurementUnit.IN) - rightPositionAtStart;
            leftTravelled = -(leftEncoder.getDistance(Encoder.MeasurementUnit.IN) - leftPositionAtStart);

            distanceTravelled = ((leftTravelled + rightTravelled) / 2.0);

            error = distanceTravelled - INCHES;

            differentialError = leftTravelled - rightTravelled;

            if (INCHES > 10) { // speeds up 5x if distance is less than 10 inches

                genPower = (genPower + (error * P_gen_forwardback)) / 2.0;

            } else {

                genPower = (genPower + (error * P_gen_forwardback * 5)) / 2.0;

            }

            if (differentialError > 0) { //Turning left, right faster
                rightOffset = (rightOffset + ((leftTravelled - rightTravelled) * P_differential)) / 2.0;
                leftOffset = 0;
            } else {
                rightOffset = 0;
                leftOffset = (leftOffset + ((rightTravelled - leftTravelled) * P_differential)) / 2.0;
            }

            telemetryControl.addData("distanceTravelled", (distanceTravelled));
            telemetryControl.addData("Right Offset", (rightOffset));
            telemetryControl.addData("Left Offset", (leftOffset));
            telemetryControl.addData("Seconds", (seconds));

            telemetryControl.addData("Diff Error", (differentialError));
            telemetryControl.addData("GenPower", (genPower));
            telemetryControl.addData("fl", fl.getPower());
            telemetryControl.addData("fr", fr.getPower());
            telemetryControl.addData("bl", bl.getPower());
            telemetryControl.addData("br", br.getPower());
            telemetryControl.update();


            //Calc side powers
            rightPower = genPower + rightOffset * genPower;
            leftPower = genPower + leftOffset * genPower;

            //Cap +/- at MAX_POWER
            if (rightPower > MAX_POWER) {
                rightPower = MAX_POWER;
            } else if (rightPower < -MAX_POWER) {
                rightPower = -MAX_POWER;
            }

            if (leftPower > MAX_POWER) {
                leftPower = MAX_POWER;
            } else if (leftPower < -MAX_POWER) {
                leftPower = -MAX_POWER;
            }

            //Cap +/- at MIN_POWER
            if (rightPower < MIN_POWER_STRAIGHT && rightPower > 0) {
                rightPower = MIN_POWER_STRAIGHT;
            } else if (rightPower > -MIN_POWER_STRAIGHT && rightPower < 0) {
                rightPower = -MIN_POWER_STRAIGHT;
            }

            if (leftPower < MIN_POWER_STRAIGHT && leftPower > 0) {
                leftPower = MIN_POWER_STRAIGHT;
            } else if (leftPower > -MIN_POWER_STRAIGHT && leftPower < 0) {
                leftPower = -MIN_POWER_STRAIGHT;
            }

            if (!waited) {
                time.reset();

                while (time.seconds() < initialDelay);
                waited = true;
            }

            //Set motor powers
            fr.setPower(rightPower);
            br.setPower(rightPower);
            fl.setPower(leftPower);
            bl.setPower(leftPower);

            if (distanceTravelled > INCHES) {
                destinationReached = true;
            }

        }

        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);

    }

    /**Drive backward
     * @param INCHES amount of inches to go forward
     * @param initialDelay A timeout to wait before starting the movement. This is useful for
     *              using consecutive movements, when different movements can bleed into
     *              each other without a proper delay between movements.
     */
    public void back(double INCHES, double initialDelay) {

        double rightOffset = 0, leftOffset = 0, genPower = 0, rightPower = 0, leftPower = 0;

        double distanceTravelled = 0;
        double error = 0;
        double rightTravelled, leftTravelled;
        double differentialError = 0;

        boolean destinationReached = false;

        double seconds = 0;
        zeroEncoders();

        boolean waited = false;

        time.reset();
        while (!destinationReached && time.seconds() < 10 + initialDelay) {

            rightTravelled = -(rightEncoder.getDistance(Encoder.MeasurementUnit.IN) - rightPositionAtStart);
            leftTravelled = leftEncoder.getDistance(Encoder.MeasurementUnit.IN) - leftPositionAtStart;

            distanceTravelled = ((leftTravelled + rightTravelled) / 2.0);

            error = distanceTravelled - INCHES;

            differentialError = rightTravelled - leftTravelled;

            if (INCHES > 10) {

                genPower = (genPower + (error * P_gen_forwardback)) / 2.0;

            } else {

                genPower = (genPower + (error * P_gen_forwardback * 5)) / 2.0;

            }

            if (differentialError > 0) { //Turning left, right faster
                rightOffset = (rightOffset + ( (leftTravelled - rightTravelled) * P_differential) )/ 2.0;
                leftOffset = 0;
            } else {
                rightOffset = 0;
                leftOffset = (leftOffset + ( (rightTravelled - leftTravelled) * P_differential) ) / 2.0;
            }

            telemetryControl.addData("distanceTravelled", (distanceTravelled));
            telemetryControl.addData("Right Offset", (rightOffset));
            telemetryControl.addData("Left Offset", (leftOffset));
            telemetryControl.addData("Seconds", (seconds));

            telemetryControl.addData("Diff Error", (differentialError));
            telemetryControl.addData("GenPower", (genPower));
            telemetryControl.addData("fl", fl.getPower());
            telemetryControl.addData("fr", fr.getPower());
            telemetryControl.addData("bl", bl.getPower());
            telemetryControl.addData("br", br.getPower());
            telemetryControl.update();


            //Calc side powers
            rightPower = genPower + rightOffset * genPower;
            leftPower = genPower + leftOffset * genPower;

            //Cap +/- at MAX_POWER
            if (rightPower > MAX_POWER) {
                rightPower = MAX_POWER;
            } else if (rightPower < -MAX_POWER) {
                rightPower = -MAX_POWER;
            }

            if (leftPower > MAX_POWER) {
                leftPower = MAX_POWER;
            } else if (leftPower < -MAX_POWER) {
                leftPower = -MAX_POWER;
            }

            //Cap +/- at MIN_POWER
            if (rightPower < MIN_POWER_STRAIGHT && rightPower > 0) {
                rightPower = MIN_POWER_STRAIGHT;
            } else if (rightPower > -MIN_POWER_STRAIGHT && rightPower < 0) {
                rightPower = -MIN_POWER_STRAIGHT;
            }

            if (leftPower < MIN_POWER_STRAIGHT && leftPower > 0) {
                leftPower = MIN_POWER_STRAIGHT;
            } else if (leftPower > -MIN_POWER_STRAIGHT && leftPower < 0) {
                leftPower = -MIN_POWER_STRAIGHT;
            }

            if (!waited) {
                time.reset();

                while (time.seconds() < initialDelay);
                waited = true;
            }

            //Set motor powers
            fr.setPower(-rightPower);
            br.setPower(-rightPower);
            fl.setPower(-leftPower);
            bl.setPower(-leftPower);

            if (distanceTravelled > INCHES) {
                destinationReached = true;
            }

        }

        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);

    }

    /**Strafe Left
     * @param INCHES amount of inches to go forward
     * @param initialDelay A timeout to wait before starting the movement. This is useful for
     *              using consecutive movements, when different movements can bleed into
     *              each other without a proper delay between movements.
     */
    public void strafeLeft(double INCHES, double initialDelay) {

        double rightOffset = 0, leftOffset = 0, genPower = 0, rightPower = 0, leftPower = 0;

        double distanceTravelled = 0;
        double distanceTravelledForward = 0;
        double error = 0;
        double headingError = 0;

        double rightTravelled = 0, leftTravelled = 0, horizTravelled = 0;

        boolean destinationReached = false;

        double seconds = 0;
        zeroEncoders();

        boolean waited = false;

        time.reset();
        while (!destinationReached && time.seconds() < 6 + initialDelay /*||
                (Math.abs(rightTravelled) > 0.5 && Math.abs(leftTravelled) > 0.5)*/) {

            rightTravelled = rightEncoder.getDistance(Encoder.MeasurementUnit.IN) - rightPositionAtStart;
            leftTravelled = -(leftEncoder.getDistance(Encoder.MeasurementUnit.IN) - leftPositionAtStart);
            horizTravelled = -(horizEncoder.getDistance(Encoder.MeasurementUnit.IN) - horizPositionAtStart);

            distanceTravelledForward = ((leftTravelled + rightTravelled) / 2.0);
            distanceTravelled = horizTravelled;

            error = distanceTravelled - INCHES;

            headingError = rightTravelled - leftTravelled;

            genPower = (genPower + (error * P_gen_strafe)) / 2.0;

            telemetryControl.addData("distanceTravelledForward", distanceTravelledForward);
            telemetryControl.addData("Right Offset", rightOffset);
            telemetryControl.addData("Left Offset", leftOffset);
            telemetryControl.addData("Seconds", seconds);

            telemetryControl.addData("Diff Error", (headingError));
            telemetryControl.addData("GenPower", (genPower));
            telemetryControl.addData("fl", fl.getPower());
            telemetryControl.addData("fr", fr.getPower());
            telemetryControl.addData("bl", bl.getPower());
            telemetryControl.addData("br", br.getPower());
            telemetryControl.update();

            rightOffset = rightOffset + (leftTravelled * P_diff_strafe);
            leftOffset = leftOffset + (rightTravelled * P_diff_strafe);

            // Calc side powers
            // This isn't terribly useful at the moment due to a lack of offsets. However, it's possible
            // we implement this in the future. If you want to try to implement it then go for it.
            rightPower = genPower + rightOffset;
            leftPower = genPower + leftOffset;

            // Cap +/- at MAX_POWER
            if (rightPower > MAX_POWER) {
                rightPower = MAX_POWER;
            } else if (rightPower < -MAX_POWER) {
                rightPower = -MAX_POWER;
            }

            if (leftPower > MAX_POWER) {
                leftPower = MAX_POWER;
            } else if (leftPower < -MAX_POWER) {
                leftPower = -MAX_POWER;
            }

            // Cap +/- at MIN_POWER
            if (rightPower < MIN_POWER_TURNSTRAFE && rightPower > 0) {
                rightPower = MIN_POWER_TURNSTRAFE;
            } else if (rightPower > -MIN_POWER_TURNSTRAFE && rightPower < 0) {
                rightPower = -MIN_POWER_TURNSTRAFE;
            }

            if (leftPower < MIN_POWER_TURNSTRAFE && leftPower > 0) {
                leftPower = MIN_POWER_TURNSTRAFE;
            } else if (leftPower > -MIN_POWER_TURNSTRAFE && leftPower < 0) {
                leftPower = -MIN_POWER_TURNSTRAFE;
            }

            if (!waited) {
                time.reset();

                while (time.seconds() < initialDelay);
                waited = true;
            }

            // Set motor powers
            fr.setPower(rightPower);
            br.setPower(-rightPower);
            fl.setPower(-leftPower);
            bl.setPower(leftPower);

            if (distanceTravelled > INCHES) {
                destinationReached = true;
            }

        }

        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);

    }

    /** Strafe Right
     * @param INCHES amount of inches to go forward
     * @param initialDelay A timeout to wait before starting the movement. This is useful for
     *              using consecutive movements, when different movements can bleed into
     *              each other without a proper delay between movements.
     */
    public void strafeRight(double INCHES, double initialDelay) {

        double rightOffset = 0, leftOffset = 0, genPower = 0, rightPower = 0, leftPower = 0;

        double distanceTravelled = 0;
        double distanceTravelledForward = 0;
        double error = 0;
        double headingError = 0;

        double rightTravelled, leftTravelled, horizTravelled;

        boolean destinationReached = false;

        double seconds = 0;
        zeroEncoders();

        boolean waited = false;

        time.reset();
        while (!destinationReached && time.seconds() < 6 + initialDelay) {

            rightTravelled = rightEncoder.getDistance(Encoder.MeasurementUnit.IN) - rightPositionAtStart;
            leftTravelled = -(leftEncoder.getDistance(Encoder.MeasurementUnit.IN) - leftPositionAtStart);
            horizTravelled = horizEncoder.getDistance(Encoder.MeasurementUnit.IN) - horizPositionAtStart;

            distanceTravelledForward = ((leftTravelled + rightTravelled) / 2.0);
            distanceTravelled = horizTravelled;

            error = distanceTravelled - INCHES;

            headingError = rightTravelled - leftTravelled;

            genPower = (genPower + (error * P_gen_strafe)) / 2.0;

            telemetryControl.addData("distanceTravelledForward", (distanceTravelledForward));
            telemetryControl.addData("Right Offset", (rightOffset));
            telemetryControl.addData("Left Offset", (leftOffset));
            telemetryControl.addData("Seconds", (seconds));

            telemetryControl.addData("Diff Error", (headingError));
            telemetryControl.addData("GenPower", (genPower));
            telemetryControl.addData("fl", fl.getPower());
            telemetryControl.addData("fr", fr.getPower());
            telemetryControl.addData("bl", bl.getPower());
            telemetryControl.addData("br", br.getPower());
            telemetryControl.update();


            //Calc side powers
            // This isn't terribly useful at the moment due to a lack of offsets. However, it's possible
            // we implement this in the future. If you want to try to implement it then go for it.
            rightPower = genPower + rightOffset;
            leftPower = genPower + leftOffset;

            //Cap +/- at MAX_POWER
            if (rightPower > MAX_POWER) {
                rightPower = MAX_POWER;
            } else if (rightPower < -MAX_POWER) {
                rightPower = -MAX_POWER;
            }

            if (leftPower > MAX_POWER) {
                leftPower = MAX_POWER;
            } else if (leftPower < -MAX_POWER) {
                leftPower = -MAX_POWER;
            }

            //Cap +/- at MIN_POWER
            if (rightPower < MIN_POWER_TURNSTRAFE && rightPower > 0) {
                rightPower = MIN_POWER_TURNSTRAFE;
            } else if (rightPower > -MIN_POWER_TURNSTRAFE && rightPower < 0) {
                rightPower = -MIN_POWER_TURNSTRAFE;
            }

            if (leftPower < MIN_POWER_TURNSTRAFE && leftPower > 0) {
                leftPower = MIN_POWER_TURNSTRAFE;
            } else if (leftPower > -MIN_POWER_TURNSTRAFE && leftPower < 0) {
                leftPower = -MIN_POWER_TURNSTRAFE;
            }

            if (!waited) {
                time.reset();

                while (time.seconds() < initialDelay);
                waited = true;
            }

            //Set motor powers
            fr.setPower(-rightPower);
            br.setPower(rightPower);
            fl.setPower(leftPower);
            bl.setPower(-leftPower);

            if (distanceTravelled > INCHES) {
                destinationReached = true;
            }

        }

        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);

    }

    /**Turn Right
     * @param DEGREES amount of inches to go forward
     * @param initialDelay A timeout to wait before starting the movement. This is useful for
     *              using consecutive movements, when different movements can bleed into
     *              each other without a proper delay between movements.
     */
    public void turnRight(double DEGREES, double initialDelay) {

        double rightOffset = 0, leftOffset = 0, genPower = 0, rightPower = 0, leftPower = 0;

        double distanceTravelled = 0;
        double error = 0;
        double differentialError = 0;

        double rightTravelled, leftTravelled;

        boolean destinationReached = false;

        double seconds = 0;
        zeroEncoders();

        boolean waited = false;

        time.reset();
        while (!destinationReached && time.seconds() < 10 + initialDelay) {

            rightTravelled = -(rightEncoder.getDistance(Encoder.MeasurementUnit.IN) - rightPositionAtStart) * inchesPerDegree;
            leftTravelled = -(leftEncoder.getDistance(Encoder.MeasurementUnit.IN) - leftPositionAtStart) * inchesPerDegree;

            distanceTravelled = ((leftTravelled + rightTravelled) / 2.0);

            error = distanceTravelled - DEGREES;

            genPower = (genPower + (error * P_gen_turn)) / 2.0;

            telemetryControl.addData("distanceTravelled", (distanceTravelled));
            telemetryControl.addData("Right Offset", (rightOffset));
            telemetryControl.addData("Left Offset", (leftOffset));
            telemetryControl.addData("Seconds", (seconds));

            telemetryControl.addData("Diff Error", (differentialError));
            telemetryControl.addData("GenPower", (genPower));
            telemetryControl.addData("fl", fl.getPower());
            telemetryControl.addData("fr", fr.getPower());
            telemetryControl.addData("bl", bl.getPower());
            telemetryControl.addData("br", br.getPower());
            telemetryControl.update();


            //Calc side powers
            rightPower = -genPower;
            leftPower = genPower;

            //Cap +/- at MAX_POWER
            if (rightPower > MAX_POWER) {
                rightPower = MAX_POWER;
            } else if (rightPower < -MAX_POWER) {
                rightPower = -MAX_POWER;
            }

            if (leftPower > MAX_POWER) {
                leftPower = MAX_POWER;
            } else if (leftPower < -MAX_POWER) {
                leftPower = -MAX_POWER;
            }

            //Cap +/- at MIN_POWER
            if (rightPower < MIN_POWER_TURNSTRAFE && rightPower > 0) {
                rightPower = MIN_POWER_TURNSTRAFE;
            } else if (rightPower > -MIN_POWER_TURNSTRAFE && rightPower < 0) {
                rightPower = -MIN_POWER_TURNSTRAFE;
            }

            if (leftPower < MIN_POWER_TURNSTRAFE && leftPower > 0) {
                leftPower = MIN_POWER_TURNSTRAFE;
            } else if (leftPower > -MIN_POWER_TURNSTRAFE && leftPower < 0) {
                leftPower = -MIN_POWER_TURNSTRAFE;
            }

            if (!waited) {
                time.reset();

                while (time.seconds() < initialDelay);
                waited = true;
            }

            //Set motor powers
            fr.setPower(rightPower);
            br.setPower(rightPower);
            fl.setPower(leftPower);
            bl.setPower(leftPower);

            if (distanceTravelled > DEGREES) {
                destinationReached = true;
            }

        }

        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);

    }

    /**Turn Left
     * @param DEGREES amount of inches to go forward
     * @param initialDelay A timeout to wait before starting the movement. This is useful for
     *              using consecutive movements, when different movements can bleed into
     *              each other without a proper delay between movements.
     */
    public void turnLeft(double DEGREES, double initialDelay) {

        double rightOffset = 0, leftOffset = 0, genPower = 0, rightPower = 0, leftPower = 0;

        double distanceTravelled = 0;
        double error = 0;
        double differentialError = 0;

        double rightTravelled, leftTravelled;

        boolean destinationReached = false;

        double seconds = 0;
        zeroEncoders();

        boolean waited = false;

        while (!destinationReached && time.seconds() < 10 + initialDelay) {

            rightTravelled = (rightEncoder.getDistance(Encoder.MeasurementUnit.IN) - rightPositionAtStart) * inchesPerDegree;
            leftTravelled = (leftEncoder.getDistance(Encoder.MeasurementUnit.IN) - leftPositionAtStart) * inchesPerDegree;

            distanceTravelled = ((leftTravelled + rightTravelled) / 2.0);

            error = distanceTravelled - DEGREES;

            genPower = (genPower + (error * P_gen_turn)) / 2.0;

            telemetryControl.addData("distanceTravelled", (distanceTravelled));
            telemetryControl.addData("Right Offset", (rightOffset));
            telemetryControl.addData("Left Offset", (leftOffset));
            telemetryControl.addData("Seconds", (seconds));

            telemetryControl.addData("Diff Error", (differentialError));
            telemetryControl.addData("GenPower", (genPower));
            telemetryControl.addData("fl", fl.getPower());
            telemetryControl.addData("fr", fr.getPower());
            telemetryControl.addData("bl", bl.getPower());
            telemetryControl.addData("br", br.getPower());
            telemetryControl.update();


            //Calc side powers
            rightPower = genPower;
            leftPower = -genPower;

            //Cap +/- at MAX_POWER
            if (rightPower > MAX_POWER) {
                rightPower = MAX_POWER;
            } else if (rightPower < -MAX_POWER) {
                rightPower = -MAX_POWER;
            }

            if (leftPower > MAX_POWER) {
                leftPower = MAX_POWER;
            } else if (leftPower < -MAX_POWER) {
                leftPower = -MAX_POWER;
            }

            //Cap +/- at MIN_POWER
            if (rightPower < MIN_POWER_TURNSTRAFE && rightPower > 0) {
                rightPower = MIN_POWER_TURNSTRAFE;
            } else if (rightPower > -MIN_POWER_TURNSTRAFE && rightPower < 0) {
                rightPower = -MIN_POWER_TURNSTRAFE;
            }

            if (leftPower < MIN_POWER_TURNSTRAFE && leftPower > 0) {
                leftPower = MIN_POWER_TURNSTRAFE;
            } else if (leftPower > -MIN_POWER_TURNSTRAFE && leftPower < 0) {
                leftPower = -MIN_POWER_TURNSTRAFE;
            }

            if (!waited) {
                time.reset();

                while (time.seconds() < initialDelay);
                waited = true;
            }

            //Set motor powers
            fr.setPower(rightPower);
            br.setPower(rightPower);
            fl.setPower(leftPower);
            bl.setPower(leftPower);

            if (distanceTravelled > DEGREES) {
                destinationReached = true;
            }

        }

        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);

    }

    /**Converts encoder counts to inches, using the counts per revolution, gear ratio, and wheel
     * diameter specified earlier.
     * @param encoderCount the encoder count to be converted into inches.*/
    private double toInches(double encoderCount) {

        return (encoderCount / this.countsPerRevolution) * gearRatio * deadWheelCircumference / 25.4;

    }

}
