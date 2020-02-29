package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class Turret extends SubsystemBase {

    private double stick;
    private double velocity;
    private double distance;

    /**
     * This class contains the methods for rotating the turret and moving Limelight around.
     */
    public Turret() {
        // Initialize Turret
        stick = 0.0;
        velocity = 0.0;
        distance = 0.0;

        OI.turretTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        OI.turretTalon.setSelectedSensorPosition(0);
        OI.turretTalon.setSensorPhase(false);

        RioLogger.log("Turret created");
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getStick() {
        return stick;
    }

    /**
     * Sets the speed of the turret talon to the turret stick value.
     * (Divided by 2 so it doesn't go too fast.)
     */
    public void updateTalonSpeed() {
        setSpeed(stick * 0.5);
    }

    /**
     * Sets the speed for the turret to a custom value
     * @param value must be between -1 and 1, inclusive.
     * 
     * <p>Negative values make the turret spin counterclockwise,
     * Positive values make it spin clockwise.
     */
    public void setSpeed(double value) {
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