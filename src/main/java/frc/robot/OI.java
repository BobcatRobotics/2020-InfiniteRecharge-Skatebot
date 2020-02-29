package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.*;

public class OI {

    //
    public static final double INVERT_MOTOR = -1.0;

    // Speed controllers
    public static final WPI_TalonSRX leftTalon = new WPI_TalonSRX(RobotMap.leftTalon);
    public static final WPI_TalonSRX rightTalon = new WPI_TalonSRX(RobotMap.rightTalon);
    public static final WPI_TalonSRX turretTalon = new WPI_TalonSRX(RobotMap.turretTalon);

    // Input devices
    public static final XboxController gamePad = new XboxController(RobotMap.gamePad);
    
    // Subsystems
    public static final DriveTrain driveTrain = new DriveTrain();

    // Gyro
    public static NavxGyro gyro;
}