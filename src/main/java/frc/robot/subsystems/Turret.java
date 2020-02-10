package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class Turret extends SubsystemBase {
    // This value represents the minimum distance from the defined 
    // zero position to move the turret back to it
    private static final int zeroThreshold = 250;

    private double stick;
    private double velocity;
    private double distance;

    /**
     * This class contains the methods for rotating the turret and moving Limelight around.
     */
    public Turret() {
        // Initialize Turret
        super();
        stick = 0.0;
        velocity = 0.0;
        distance = 0.0;

        OI.turretTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
        OI.turretTalon.setSelectedSensorPosition(0, 0, 0);
        OI.turretTalon.setSensorPhase(false);

        RioLogger.log("Turret created");
    }

    /**
     * Sets the turret.distance to zero. When using the zeroTurret() method, it will
     * send the turret back to this position.
     */
    public void zeroDrive() {
        OI.turretTalon.setSelectedSensorPosition(0);
        distance = 0.0;
    }

    /**
     * @return Boolean value representing if the turret is in a position that can use the
     * zeroTurret() method. It checks if it is within 250 distance of the zero position.
     */
    public boolean canZeroTurret() {
        return (Math.abs(distance) > zeroThreshold);
    }

    /**
     * Moves the turret back to the defined zero position set by the zeroDrive() method.
     */
    public void zeroTurret() {
        if (distance > -(zeroThreshold)) {
            setTurretSpeed(-(Math.abs(stick) * 0.5) + 0.1);
        } else {
            setTurretSpeed(Math.abs(stick) * 0.5 + 0.1);
        }
    }

    /**
     * Sets the speed of the turret talon to the turret stick value.
     * (Divided by 2 so it doesn't go too fast.)
     */
    public void updateTalonSpeed() {
        setTurretSpeed(stick * 0.5);
    }

    /**
     * Sets the speed for the turret to a custom value
     * @param value must be between -1 and 1, inclusive.
     * 
     * <p>Negative values make the turret spin counterclockwise,
     * Positive values make it spin clockwise.
     */
    public void setTurretSpeed(double value) {
        OI.turretTalon.set(value);
    }

    /**
     * This method is called periodically by the CommandScheduler.
     * Updates the turret.stick, turret.distance, and turret.velocity values.
     */
    @Override
    public void periodic() {
        stick = -(OI.gamePad.getX(Hand.kLeft));
        distance = OI.turretTalon.getSelectedSensorPosition(0);
        velocity = OI.turretTalon.getSelectedSensorVelocity(0);

        SmartDashboard.putNumber("turret stick:", stick);
        SmartDashboard.putNumber("turret distance:", distance);
        SmartDashboard.putNumber("turret velocity:", velocity);
    }
}