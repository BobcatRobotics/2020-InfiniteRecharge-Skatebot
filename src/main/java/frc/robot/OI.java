package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.XboxController;

public class OI {
    public class TurretConstants {
        // Distnace from the cetner that will be considered as the center
        public static final int zeroThreshold = 250;
        
        // Minimum power is reach at (1.351...) degrees from the center
        // degreesFromCenter = minimumPower / k
        public static final double k = 0.037037; // Power constant (27 degrees = Power of 1)
        public static final double minimumPower = 0.05; // Minimal power to send
        public static final double threshold = 0.15; // The threshold in degrees where the turret won't move
    }

    // Speed controllers
    public static final WPI_TalonSRX leftTalon = new WPI_TalonSRX(RobotMap.leftTalon);
    public static final WPI_TalonSRX rightTalon = new WPI_TalonSRX(RobotMap.rightTalon);
    public static final WPI_TalonSRX turretTalon = new WPI_TalonSRX(RobotMap.turretTalon);

    // Input devices
    public static final XboxController gamePad = new XboxController(RobotMap.gamePad);
}