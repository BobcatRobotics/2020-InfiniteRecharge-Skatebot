package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;/*
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.TargetEntity;*/
import frc.robot.subsystems.*;

public class OI {
    public static final WPI_TalonSRX leftTalon = new WPI_TalonSRX(7);
    public static final WPI_TalonSRX rightTalon = new WPI_TalonSRX(8);
    public static final WPI_TalonSRX turretTalon = new WPI_TalonSRX(10);
    public static final DifferentialDrive differentialDrive = new DifferentialDrive(leftTalon, rightTalon);

    public static final Joystick l_stick = new Joystick(0);
    public static final Joystick r_stick = new Joystick(1);
    public static final XboxController gamePad = new XboxController(2);

    public static final DriveTrain driveTrain = new DriveTrain();
    public static final Limelight limelight = new Limelight();
    public static final Turret turret = new Turret();

    //public static Button btnTargetEntity = new JoystickButton(gamePad, 1);

    /*static {
        btnTargetEntity.whenPressed(new TargetEntity());
    }*/
}