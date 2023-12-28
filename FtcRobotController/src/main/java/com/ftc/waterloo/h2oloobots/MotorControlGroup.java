package com.ftc.waterloo.h2oloobots;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**This class allows you to define a group of two motors as one motor group. You can then do almost
 * anything you can do to one motor, to two.*/
public class MotorControlGroup {

    private DcMotor motor1, motor2;

    /**The constructor of the class. This defines the motors as local variables.
     * @param motor1 the motor to be globally set to motor #1 in this instance of the class.
     * @param motor2 the motor to be globally set to motor #2 in this instance of the class.*/
    public MotorControlGroup(DcMotor motor1, DcMotor motor2) {

        this.motor1 = motor1;
        this.motor2 = motor2;

    }

    /**Sets the direction of both motors.
     * @param direction the direction to be set to both motors.*/
    void setDirection(DcMotorSimple.Direction direction) {

        motor1.setDirection(direction);
        motor2.setDirection(direction);

    }

    /**Sets the direction of both motors separately.
     * @param motor1Direction the direction set to motor #1 (as defined in the constructor).
     * @param motor2Direction the direction set to motor #2 (as defined in the constructor).
     */
    void setDirection(DcMotorSimple.Direction motor1Direction,
                             DcMotorSimple.Direction motor2Direction) {

        motor1.setDirection(motor1Direction);
        motor2.setDirection(motor2Direction);

    }

    /**Returns an array of the motors' directions. if the directions of the two motors are
     * the same, then it will return an array with one element. If not, the value in index
     * zero will be the direction from motor #1 (as defined by the constructor),
     * and the value in index one will be the direction of motor #2 (as defined by the
     * constructor).*/
    DcMotorSimple.Direction[] getDirection() {

        if (motor1.getDirection() == motor2.getDirection()) {

            return new DcMotorSimple.Direction[]{motor1.getDirection()};

        } else {

            return new DcMotorSimple.Direction[]{motor1.getDirection(), motor2.getDirection()};

        }

    }

    /**Sets a power to the two motors.
     * @param power the power level to be sent to the motors (range: 0-1)*/
    void setPower(double power) {

        motor1.setPower(power);
        motor2.setPower(power);

    }

    /**Returns the power level of the two motors.*/
    double getPower() {

        return motor1.getPower();

    }

    /** Sets the ZeroPowerBehavior of the two motors.*/
    void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {

        motor1.setZeroPowerBehavior(zeroPowerBehavior);
        motor2.setZeroPowerBehavior(zeroPowerBehavior);

    }

    /**Returns the ZeroPowerBehavior of the two motors.*/
    DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {

        return motor1.getZeroPowerBehavior();

    }

    /**Sets the target position of the two motors.
     * @param position the position to set the motors' target positions to.*/
    void setTargetPosition(int position) {

        motor1.setTargetPosition(position);
        motor2.setTargetPosition(position);

    }

    /**Returns the target position of the two motors as an int.*/
    int getTargetPosition() {

        return motor1.getTargetPosition();

    }

    /**Returns whether or not either of the motors are currently being commanded
     * to a position as a boolean.*/
    public boolean isBusy() {

        return motor1.isBusy() || motor2.isBusy();

    }

    /**Returns the average of the two motors' current positions as a double.*/
    double getCurrentPosition() {

        return (motor1.getCurrentPosition() + motor2.getCurrentPosition()) / 2;

    }

    /**Sets the modes of the two motors.
     * @param runMode the run mode to be set to the motors.*/
    void setMode(DcMotor.RunMode runMode) {

        motor1.setMode(runMode);
        motor2.setMode(runMode);

    }

}
