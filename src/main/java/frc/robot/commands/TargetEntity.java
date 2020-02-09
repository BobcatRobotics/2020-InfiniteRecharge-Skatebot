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
	private static final double minimumPower = 0.05; // If the power is any less than this minimumPower the turret may not actually move
	private static final double threshold = 0.15; // The threshold in which the turret actually needs to move

	private boolean hasValidTarget; // Updated by the LimeLight camera; whether it has a target
	private double power; // Updated by the Limelight camera;
	private double additionalPower; // Updated by the right Joystick on the gamepad 
	private double turretPower; // Updated by the LimeLight camera; equal to power + additionalPower

	/**
	 * Initalize the TargetEntity command
	 */
	public TargetEntity() {
		super();
		hasValidTarget = false;
		power = 0;
		additionalPower = 0;
		turretPower = 0;
		addRequirements(OI.limelight);
		addRequirements(OI.turret);
		RioLogger.log("Tracking started");
	}

	/*
	1. Turns on LimeLight's LED's and starts it's targeting
	2. If LimeLight doesn't have a target, then set power, additionalPower, and turretPower to 0.0
	3. If LimeLight does have a target, then:
		1. Get the tx value from NetworkTables.
		2. Turn the value into a speed by:
			a. Inverting the X value (because if it's negative, then get a positive value which will make the turret turn right towards the center)
			b. Multiplying it by the proportional control constant
		3. If the x value is not within the threshold and the power is less than the minimum power, set the power to the minimum power
		4. If the x value is within the threshold, set the power to 0
		5. Get the additional power from the game pad's left joystick's Y value
	 6. If the total power is greater than 1, set it to 1
	 7. If the total power is less than -1, set it to -1
	4. Pass the power to the motor
	5. Print the values to SmartDashboard
	*/

	/**
	 * Execute one iteration of the TargetEntity command (For multiple iterations, call multiple times)
	 */
	@Override
	public void execute() {
		OI.limelight.setLedMode(ledMode.ON); // Turn on the LED's if they haven't been turned on before
		OI.limelight.setCamMode(camMode.VISION); // Turn on vision mode if it wasn't turned on before

		// Whether Limelight detects a target
		hasValidTarget = OI.limelight.hasTargets();
		if (hasValidTarget) {
			// The number of degrees the target is off center horizontally
			double x = OI.limelight.tx();
			// The number of degrees Limelight needs to shift by to be centered
			double degreesToCenter = -x;

			// Instead of waiting for the target to go off the screen, center the target
			// This value will be negative if it needs to go right and positive if it needs to go left
			// We multiply by a constant because the motor doesn't need that much power so it doesn't go way too fast
			power = Kp * degreesToCenter;
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
			additionalPower = Math.abs(OI.gamePad.getY(Hand.kLeft)) * Math.signum(power);
			// Total power of the turret
			turretPower = power + additionalPower;
			// Makes sure the power does not get higher than 1 or less than -1
			if (turretPower > 1) {
				turretPower = 1;
			} else if (turretPower < -1) {
				turretPower = -1;
			}
		} else {
			// If Limelight does not detect a target then it sets the turret power to 0.
			power = 0.0;
			additionalPower = 0.0;
			turretPower = 0.0;
		}

		OI.turret.setTurretSpeed(turretPower);
		// Puts values on the Smart Dashboard
		SmartDashboard.putBoolean("Target.TargetIdentified", hasValidTarget);
		SmartDashboard.putNumber("Target.Power", power);
		SmartDashboard.putNumber("Target.AddPower", additionalPower);
		SmartDashboard.putNumber("Target.TurretPower", turretPower);
		SmartDashboard.putNumber("GamePad.dpad", OI.gamePad.getPOV(RobotMap.dpad));
	}

	/**
	 * If the bottom button on the POV is pressed, then the command is done
	 */
	@Override
	public boolean isFinished() {
		// Press the Down arrow on the D-pad to end the command
		if (OI.gamePad.getPOV(RobotMap.dpad) == RobotMap.dpadDown) {
			return true;
		}
		return false;
	}

	/**
	 * Turn off the LED's so people don't get blinded
	 * Print whether the command was interupted or not
	 */
	@Override
	public void end(boolean failed) {
		OI.limelight.setLedMode(ledMode.OFF);
		if (failed) {
			RioLogger.log("Tracking was interupted, press the up button on the POV to start again");
		} else {
			RioLogger.log("Tracking finished, press the up button on the POV to start again");
		}
	}
}
