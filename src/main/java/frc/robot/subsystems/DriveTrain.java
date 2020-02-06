package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class DriveTrain extends SubsystemBase {
	private final WPI_TalonSRX leftTalon = OI.leftTalon;
    private final WPI_TalonSRX rightTalon = OI.rightTalon;

    private final DifferentialDrive diffDrive = OI.differentialDrive;

    private double leftStick = 0.0;
    private double rightStick = 0.0;

    private double leftVelocity = 0.0;
    private double rightVelocity = 0.0;

    private double leftDistance = 0.0;
    private double rightDistance = 0.0;

	public DriveTrain() {
		// Initialize Drive Train
        RioLogger.log("DriveTrain() created.");

        leftTalon.configSelectedFeedbackSensor(OI.magEncoder, 0, 0);
        leftTalon.setSelectedSensorPosition(0, 0, 0);
        leftTalon.setSensorPhase(true); 

        rightTalon.configSelectedFeedbackSensor(OI.magEncoder, 0, 0);
        rightTalon.setSelectedSensorPosition(0, 0, 0);
        rightTalon.setSensorPhase(false);
    }
    
    /**
     * Drive with default values (leftStick, rightStick).
     */
    public void drive() {
        diffDrive.tankDrive(leftStick, rightStick);
    }

    /**
     * Drive with custom values.
     */
	public void drive(double leftSpeed, double rightSpeed) {
		diffDrive.tankDrive(leftSpeed, rightSpeed);
    }
    
    /**
     * Stop the robot from driving
     */
    public void stop() {
        drive(0.0, 0.0);
    }
    
    /** 
     * Updates distance, velocity, and stick values.
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