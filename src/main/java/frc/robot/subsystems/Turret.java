package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class Turret extends SubsystemBase {
    private final WPI_TalonSRX talon = OI.turretTalon;

    public double stick = 0.0;
    public double velocity = 0.0;
    public double distance = 0.0;

    public boolean canZeroTurret = (distance < -500) || (distance > 500);

    public Turret() {
        // Initialize Turret
        RioLogger.log("Turret() created.");

        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
        talon.setSelectedSensorPosition(0, 0, 0);
        talon.setSensorPhase(false);
    }

    public void zeroDrive() {
        talon.setSelectedSensorPosition(0);
        distance = 0.0;
    }

    /** Moves the turret back to the defined zero position. */
    public void zeroTurret() {
        if (distance > -500) {
            talon.set(ControlMode.PercentOutput, -(Math.abs(stick)));
        } else {
            talon.set(ControlMode.PercentOutput, Math.abs(stick));
        }
    }

    /** Updates the turret.stick, turret.distance, and turret.velocity values */
    public void update() {
        stick = -(OI.gamePad.getX(Hand.kLeft));
        distance = talon.getSelectedSensorPosition(0);
        velocity = talon.getSelectedSensorVelocity(0);
    }

    /** Sets the speed of the turret talon to the turret stick value */
    public void updateTalon() {
        talon.set(ControlMode.PercentOutput, stick);
    }
}