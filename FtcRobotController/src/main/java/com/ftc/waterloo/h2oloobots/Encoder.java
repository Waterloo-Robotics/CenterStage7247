package com.ftc.waterloo.h2oloobots;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**This class just makes it easier to visualise the encoders as seperate objects
 *  from the motors their ports are associated with.*/
public class Encoder {

    DcMotorEx encoder;

    double countsPerRevolution;
    double wheelDiameter;
    double wheelCircumference;
    double gearRatio;
    int inverted = 1;

    public enum MeasurementUnit {

        IN,
        MM,
        REV,
        PULSES

    }

    MeasurementUnit measurementUnit;

    /**This class creates a separate class for dead wheel encoders to make it easier to visualise
     * and use.
     * @param hardwareMap the HardwareMap from the OpMode. This just makes it possible to initialise
     * the variable from within the class.
     * @param deviceName the device name of the encoder. This should be the same as the motor that
     * is plugged into the same port.
     * @param countsPerRevolution the counts per revolution of the encoder. For the REV Through
     * Bore Encoder, this is 8192.
     * @param wheelDiameter the dead wheel diameter in millimeters or inches.
     * @param measurementUnit the measurement unit, can be millimeters or inches.
     * @param gearRatio if you gear your encoders, put the gear ratio here.*/
    public Encoder(HardwareMap hardwareMap,
                   String deviceName,
                   double countsPerRevolution,
                   double wheelDiameter,
                   MeasurementUnit measurementUnit,
                   double gearRatio) {

        this.encoder = (DcMotorEx) hardwareMap.dcMotor.get(deviceName);
        this.encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.countsPerRevolution = countsPerRevolution;
        this.wheelDiameter = wheelDiameter;
        this.wheelCircumference = wheelDiameter * Math.PI;
        this.measurementUnit = measurementUnit;
        this.gearRatio = gearRatio;

    }

    /**Sets the direction of the encoder. This can be helpful if the encoder isn't giving an output
     * on the side of zero you would like it to.
     * @param inverted whether or not the encoder is inverted.*/
    void setInverted(boolean inverted) {

        if (inverted) this.inverted = -1;
        else this.inverted = 1;

    }

    /**Sets the runmode of the encoder.
     * @param mode the run mode to set to the encoder. I would recommend either
     *             STOP_AND_RESET_ENCODER or RUN_WITHOUT_ENCODER. Setting it to reset the encoder
     *             to set it to 0, and then setting it to run without encoder to get encoder counts.
     *             Using RUN_WITH_ENCODER is also supported, but I wouldn't recommend it because
     *             it offers no extra functionality and it's prone to cause weird issues with
     *             setting power. I'm not sure how setting to RUN_TO_POSITION will work to be frank.
     *             */
    void setMode(DcMotor.RunMode mode) {

        encoder.setMode(mode);

    }

    /**Sets the target position of the encoder.
     * @param position the target position to set (in encoder counts)*/
    void setTargetPosition(int position) {

        encoder.setTargetPosition(position);

    }

    /**Sets the target position tolerance
     * @param tolerance sets the tolerance of the encoder. This means in RUN_TO_POSITION mode,
     * if the encoder returns a position within one tolerance of the target position, it will
     * return isBusy() as false.*/
    void setTargetPositionTolerance(int tolerance) {

        encoder.setTargetPositionTolerance(tolerance);

    }

    /**Returns the current read position, in encoder counts, as an int.*/
    public int getCurrentPosition() {

        return this.inverted * encoder.getCurrentPosition();

    }

    /**Returns the current read amount of rotations, based on the counts per revolution
     * from the constructor.*/
    public double getRotations() {

        return this.getCurrentPosition() / countsPerRevolution * gearRatio;

    }

    /**Returns the current read distance, based on the counts per revolution and the wheel diameter
     * from the constructor. Will return in the Measurement Unit defined in the constructor.*/
    public double getDistance() {

        return this.getRotations() * this.wheelCircumference;

    }

    /**Returns the current read distance, based on the counts per revolution and the wheel diameter
     * from the constructor.
     * @param measurementUnit the measurement unit to return the distance in.*/
    public double getDistance(MeasurementUnit measurementUnit) {

        double distance = 0;

        if (measurementUnit != this.measurementUnit) {

            switch (measurementUnit) {

                case IN:
                    distance = this.getDistance() / 25.4;
                    break;

                case MM:
                    distance = this.getDistance() * 25.4;
                    break;

                case REV:
                    distance = this.getRotations();
                    break;

                case PULSES:
                    distance = this.getCurrentPosition();
                    break;
            }

        } else {

            distance = this.getDistance();

        }

        return distance;

    }

    /**In the RUN_TO_POSITION mode, this will return whether or not the encoder is actively working
     * towards running to the position as a boolean.*/
    boolean isBusy() {

        return encoder.isBusy();

    }

}
