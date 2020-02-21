package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.*;

public class OI {
    // Speed controllers
    public static final WPI_TalonSRX leftTalon = new WPI_TalonSRX(RobotMap.leftTalon);
    public static final WPI_TalonSRX rightTalon = new WPI_TalonSRX(RobotMap.rightTalon);

    // Input devices
    public static final Joystick leftJoystick = new Joystick(RobotMap.leftJoystick);
    public static final Joystick rightJoystick = new Joystick(RobotMap.rightJoystick);
    public static final XboxController gamePad = new XboxController(RobotMap.gamePad);
    
    // Subsystems
    public static final DriveTrain driveTrain = new DriveTrain();

    // Gyro
    public static NavxGyro gyro;
}