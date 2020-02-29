package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class DriveTrain extends SubsystemBase {
    private double leftStick = 0.0;
    private double rightStick = 0.0;

    private double leftSpeed = 0.0;
    private double rightSpeed = 0.0;
    // Whether or not to square root the speed of the drive train
    private boolean sqrtSpeeds;

    /**
     * This class contains the methods for driving Skatebot.
     */
    public DriveTrain() {
        // Initialize Drive Train
        super();
        sqrtSpeeds = false;
        RioLogger.log("DriveTrain created");

        OI.leftTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        OI.leftTalon.setSelectedSensorPosition(0);
        OI.leftTalon.setSensorPhase(true);

        OI.rightTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        OI.rightTalon.setSelectedSensorPosition(0);
        OI.rightTalon.setSensorPhase(true);
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

        OI.rightTalon.set(rightSpeed);
        OI.leftTalon.set(leftSpeed);
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
     * This method is called periodically by the CommandScheduler.
     * Updates speed and stick values and puts them on the SmartDashboard.
     */
    @Override
    public void periodic() {
        leftSpeed = OI.leftTalon.getSelectedSensorVelocity();
        rightSpeed = OI.rightTalon.getSelectedSensorVelocity();

        leftStick = OI.gamePad.getY(Hand.kLeft);
        rightStick = OI.gamePad.getY(Hand.kRight)*-1;

        SmartDashboard.putNumber("left stick:", leftStick);
        SmartDashboard.putNumber("right stick:", rightStick);
        SmartDashboard.putNumber("left speed:", leftSpeed);
        SmartDashboard.putNumber("right speed:", rightSpeed);
    }
}
