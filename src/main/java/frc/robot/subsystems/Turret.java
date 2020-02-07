package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class Turret extends SubsystemBase {
    private final WPI_TalonSRX talon = OI.turretTalon;

    private double stick = 0.0;
    private double velocity = 0.0;
    private double distance = 0.0;

    public Turret() {
        // Initialize Turret
        RioLogger.log("Turret() created");

        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
        talon.setSelectedSensorPosition(0, 0, 0);
        talon.setSensorPhase(false);
    }

    /**
     * Sets the turret.distance to zero. When using the zeroTurret()
     * method, it will send the turret back to this position.
     */
    public void zeroDrive() {
        talon.setSelectedSensorPosition(0);
        distance = 0.0;
    }

    /**
     * Boolean value representing if the turret is in a position
     * that can use the zeroTurret() method. It checks if it is within
     * 500 distance of the zero position.
     */
    public boolean canZeroTurret() {
        return (distance < -500) || (distance > 500);
    }

    /** 
     * Moves the turret back to the defined zero position.
     */
    public void zeroTurret() {
        if (distance > -500) {
            talon.set(ControlMode.PercentOutput, -(Math.abs(stick)*0.5));
        } else {
            talon.set(ControlMode.PercentOutput, Math.abs(stick)*0.5);
        }
    }

    /** 
     * Updates the turret.stick, turret.distance, and turret.velocity values.
     */
    public void updateAndShowValues() {
        stick = -(OI.gamePad.getX(Hand.kLeft));
        distance = talon.getSelectedSensorPosition(0);
        velocity = talon.getSelectedSensorVelocity(0);

        SmartDashboard.putNumber("turret stick:", stick);
        SmartDashboard.putNumber("turret distance:", distance);
        SmartDashboard.putNumber("turret velocity:", velocity);
    }

    /** 
     * Sets the speed of the turret talon to the turret stick value.
     */
    public void updateTalonSpeed() {
        talon.set(ControlMode.PercentOutput, stick*0.5);
    }

    /**
     * Sets the speed for the turret to a custom value
     */
    public void setTurretSpeed(double value) {
        talon.set(ControlMode.PercentOutput, value);
    }
}