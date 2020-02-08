package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class DriveTrain extends SubsystemBase {
    private final WPI_TalonSRX leftTalon = OI.leftTalon;
    private final WPI_TalonSRX rightTalon = OI.rightTalon;

    private double leftStick = 0.0;
    private double rightStick = 0.0;

    private double rightSpeed = 0.0;
    private double leftSpeed = 0.0;
    private boolean sqrtSpeeds = true;

    private double leftVelocity = 0.0;
    private double rightVelocity = 0.0;

    private double leftDistance = 0.0;
    private double rightDistance = 0.0;

    public DriveTrain() {
        // Initialize Drive Train
        RioLogger.log("DriveTrain created");

        leftTalon.configSelectedFeedbackSensor(OI.magEncoder, 0, 0);
        leftTalon.setSelectedSensorPosition(0, 0, 0);
        leftTalon.setSensorPhase(true);

        rightTalon.configSelectedFeedbackSensor(OI.magEncoder, 0, 0);
        rightTalon.setSelectedSensorPosition(0, 0, 0);
        rightTalon.setSensorPhase(true);
    }

    /**
     * Drive with default values (the leftStick and rightStick values from the Joysticks).
     */
    public void drive() {
        drive(leftStick, rightStick);
    }

    /**
     * Drive with custom values.
     * @param left is the speed sent to the left motor
     * @param right is the speed sent to the right motor
     */
    public void drive(double left, double right) {
        if (sqrtSpeeds) {
            rightSpeed = setSQRTPower(right);
            leftSpeed = setSQRTPower(left);
        } else {
            rightSpeed = right;
            leftSpeed = left;
        }

        rightTalon.set(rightSpeed);
        leftTalon.set(leftSpeed);
    }

    /**
     * @param speed is the speed to be square rooted
     * @return The square root of the speed, with the according sign.
     */
    public double setSQRTPower(double speed) {
        if (speed < 0) {
            return -(Math.sqrt(-speed));
        } else {
            return Math.sqrt(speed);
        }
    }

    /**
     * Changes whether the speed of the drive train is square rooted from true to false and vice versa.
     */
    public void toggleSqrt() {
        sqrtSpeeds = !sqrtSpeeds;
    }

    /**
     * Stop the robot from driving
     */
    public void stop() {
        drive(0.0, 0.0);
    }

    /**
     * Updates distance, velocity, and stick values and puts them on the SmartDashboard.
     */
    public void updateAndShowValues() {
        leftDistance = leftTalon.getSelectedSensorPosition(0);
        rightDistance = rightTalon.getSelectedSensorPosition(0);

        leftVelocity = leftTalon.getSelectedSensorVelocity(0);
        rightVelocity = rightTalon.getSelectedSensorVelocity(0);

        leftStick = OI.leftJoystick.getRawAxis(Joystick.AxisType.kY.value);
        rightStick = OI.rightJoystick.getRawAxis(Joystick.AxisType.kY.value);

        SmartDashboard.putNumber("left stick:", leftStick);
        SmartDashboard.putNumber("right stick:", rightStick);
        SmartDashboard.putNumber("left distance:", leftDistance);
        SmartDashboard.putNumber("left velocity:", leftVelocity);
        SmartDashboard.putNumber("right distance:", rightDistance);
        SmartDashboard.putNumber("right velocity:", rightVelocity);
    }
}