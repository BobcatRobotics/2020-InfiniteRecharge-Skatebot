package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class DriveTrain extends SubsystemBase {
	private final WPI_TalonSRX leftTalon = OI.leftTalon;
    private final WPI_TalonSRX rightTalon = OI.rightTalon;

    private final DifferentialDrive m_robotDrive = OI.differentialDrive;
    private final Joystick l_stick = OI.l_stick;
    private final Joystick r_stick = OI.r_stick;

    public double leftStick = 0.0;
    public double rightStick = 0.0;

    public double leftVelocity = 0.0;
    public double rightVelocity = 0.0;

    public double leftDistance = 0.0;
    public double rightDistance = 0.0;

	public DriveTrain() {
		// Initialize Drive Train
        RioLogger.log("DriveTrain() created.");

        leftTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
        leftTalon.setSelectedSensorPosition(0, 0, 0);
        leftTalon.setSensorPhase(true);

        rightTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
        rightTalon.setSelectedSensorPosition(0, 0, 0);
        rightTalon.setSensorPhase(false);
    }
    
    /** Drive with default values (leftStick, rightStick) */
    public void drive() {
        m_robotDrive.tankDrive(leftStick, rightStick);
    }

    /** Drive with custom values */
	public void drive(double leftSpeed, double rightSpeed) {
		m_robotDrive.tankDrive(leftSpeed, rightSpeed);
	}

	public void stop() {
		drive(0.0, 0.0);
	}
    
    /** Updates distance, velocity, and stick values */
    public void update() {
        leftDistance = leftTalon.getSelectedSensorPosition(0);
        rightDistance = rightTalon.getSelectedSensorPosition(0);

        leftVelocity = leftTalon.getSelectedSensorVelocity(0);
        rightVelocity = rightTalon.getSelectedSensorVelocity(0);
    
        leftStick = l_stick.getRawAxis(Joystick.AxisType.kY.value);
        rightStick = r_stick.getRawAxis(Joystick.AxisType.kY.value);
    }
}