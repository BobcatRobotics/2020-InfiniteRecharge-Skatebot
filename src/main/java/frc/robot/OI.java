package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.XboxController;

public class OI {
    public class TurretConstants {
        // Distnace from the cetner that will be considered as the center
        public static final int zeroThreshold = 250;
    }

    // Speed controllers
    public static final WPI_TalonSRX leftTalon = new WPI_TalonSRX(RobotMap.leftTalon);
    public static final WPI_TalonSRX rightTalon = new WPI_TalonSRX(RobotMap.rightTalon);
    public static final WPI_TalonSRX turretTalon = new WPI_TalonSRX(RobotMap.turretTalon);

    // Input devices
    public static final XboxController gamePad = new XboxController(RobotMap.gamePad);
}