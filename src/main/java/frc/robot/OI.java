package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.PerpetualCommand;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

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
    
    // Subsystems
    private static final DriveTrain drivetrain = new DriveTrain();
    private static final Limelight limelight = new Limelight();
    private static final Turret turret = new Turret();

    public OI() {
        // Starts targeting when the up arrow on the D-pad is pressed
        new POVButton(gamePad, RobotMap.povUp).whenPressed(new TargetEntity(limelight, turret));
        // Ends targeting when the down arrow on the D-pad is pressed
        new POVButton(gamePad, RobotMap.povDown).cancelWhenPressed(new TargetEntity(limelight, turret));
        // Driving
        new PerpetualCommand(new DriveTele(drivetrain, limelight, turret)).schedule();
    }
}