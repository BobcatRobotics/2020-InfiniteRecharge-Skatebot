package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.lib.RioLogger;
import frc.robot.subsystems.Limelight.camMode;
import frc.robot.subsystems.Limelight.ledMode;

public class TargetEntity extends CommandBase {
	private static final double Kp = 0.025; // Proportional control constant to determine the power

	private boolean hasValidTarget; // Updated by the LimeLight camera; whether it has a target
	private double power; // Updated by the Limelight camera;
	private double additionalPower; // Updated by the right Joystick on the gamepad 
	private double turretPower; // Updated by the LimeLight camera; equal to power + additionalPower

	public TargetEntity() {
		// Intitialize Target Entity
		super();
		hasValidTarget = false;
		power = 0;
		additionalPower = 0;
		turretPower = 0;
		addRequirements(OI.limelight);
		addRequirements(OI.turret);
		RioLogger.log("Tracking started");
	}

	/**
	 * Called repeatedly while the command is scheduled.
	 */
	@Override
	public void execute() {
		OI.limelight.setLedMode(ledMode.ON); // Turn on the LED's if they haven't been turned on before
		OI.limelight.setCamMode(camMode.VISION); // Turn on vision mode if it wasn't turned on before

		// Whether Limelight detects a target
		hasValidTarget = OI.limelight.hasTargets();
		if (!hasValidTarget) {
			// If Limelight does not detect a target then it sets the turret power to 0.
			power = 0.0;
			additionalPower = 0.0;
			turretPower = 0.0;
		} else {
			// The number of degrees the target is off center horizontally
			double x = OI.limelight.tx();
			// The number of degrees Limelight needs to shift by to be centered
			double error = -x;

			// Instead of waiting for the target to go off the screen, center the target
			// This value will be negative if it needs to go right and positive if it needs to go left
			// We multiply by a constant because the motor doesn't need that much power
			// and so it doesn't go way too fast
			power = Kp * error;
			// If the power is any less than this minimumPower the turret may not actually move
			double minimumPower = 0.05;
			// The threshold in which the turret actually needs to move
			double threshold = 0.15;
			// If the power is too small and tx is within the threshold,
			// increase it to +/- 0.05 so the turret actually moves
			if (x > threshold && power > -minimumPower) {
				power = -minimumPower;
			} else if (x < -threshold && power < minimumPower) {
				power = minimumPower;
			} else if (x < threshold && x > -threshold) {
				// If the target is not off center by that much, we don't need to change the position
				power = 0.0;
			}

			// To increase the speed of the turret, you can push the Right Joystick on
			// the gamepad to add additional power
			additionalPower = Math.abs(OI.gamePad.getY(Hand.kRight)) * Math.signum(power);
			// Total power of the turret
			turretPower = power + additionalPower;
			// Makes sure the power does not get higher than 1 or less than -1
			if (turretPower > 1) {
				turretPower = 1;
			} else if (turretPower < -1) {
				turretPower = -1;
			}
		}

		OI.turret.setTurretSpeed(turretPower);
		// Puts values on the Smart Dashboard
		SmartDashboard.putBoolean("Target.TargetIdentified", hasValidTarget);
		SmartDashboard.putNumber("Target.Power", power);
		SmartDashboard.putNumber("Target.AddPower", additionalPower);
		SmartDashboard.putNumber("Target.TurretPower", turretPower);
	}

	/**
	 * Once a command finishes, the scheduler will call its end() method and un-schedule it.
	 */
	@Override
	public boolean isFinished() {
		// Press the Down arrow on the D-pad to end the command
		if (OI.gamePad.getPOV(RobotMap.pov) == RobotMap.povDown) {
			return true;
		}
		return false;
	}

	/**
	 * Called when either the command finishes normally, or when it gets interrupted/canceled.
	 */
	@Override
	public void end(boolean failed) {
		OI.limelight.setLedMode(ledMode.OFF);
		if (failed) {
			RioLogger.log("Tracking was interupted, press Up Arrow to start again");
		} else {
			RioLogger.log("Tracking finished, press Up Arrow to start again");
		}
	}
}
