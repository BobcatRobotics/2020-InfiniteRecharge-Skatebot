package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;

public class DriveTrain extends SubsystemBase {
    private double leftStick = 0.0;
    private double rightStick = 0.0;

    private double leftSpeed = 0.0;
    private double rightSpeed = 0.0;

    /**
     * This class contains the methods for driving Skatebot.
     */
    public DriveTrain() {
        // Initialize Drive Train
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
        OI.rightTalon.set(right);
        OI.leftTalon.set(left);
    }

    /**
     * Stop the robot from driving
     */
    public void stop() {
        drive(0.0, 0.0);
    }

    public void update() {
        leftSpeed = OI.leftTalon.getSelectedSensorVelocity(0);
        rightSpeed = OI.rightTalon.getSelectedSensorVelocity(0);

        leftStick = OI.gamePad.getY(Hand.kLeft);
        rightStick = OI.gamePad.getY(Hand.kRight);

        SmartDashboard.putNumber("left stick:", leftStick);
        SmartDashboard.putNumber("right stick:", rightStick);
        SmartDashboard.putNumber("left speed:", leftSpeed);
        SmartDashboard.putNumber("right soeed:", rightSpeed);
    }
}
