package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;

public class OI {
    public static final WPI_TalonSRX leftTalon = new WPI_TalonSRX(RobotMap.leftTalon);
    public static final WPI_TalonSRX rightTalon = new WPI_TalonSRX(RobotMap.rightTalon);
    public static final WPI_TalonSRX turretTalon = new WPI_TalonSRX(RobotMap.turretTalon);

    public static final Joystick leftJoystick = new Joystick(RobotMap.leftJoystick);
    public static final Joystick rightJoystick = new Joystick(RobotMap.rightJoystick);
    public static final XboxController gamePad = new XboxController(RobotMap.gamePad);

    public static final FeedbackDevice magEncoder = FeedbackDevice.CTRE_MagEncoder_Absolute;
    
    public static final DriveTrain driveTrain = new DriveTrain();
    public static final Limelight limelight = new Limelight();
    public static final Turret turret = new Turret();

    public static Button targetEntity = new JoystickButton(gamePad, RobotMap.padA);
}