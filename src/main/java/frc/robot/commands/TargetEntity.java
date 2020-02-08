package frc.robot.commands;

import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.lib.RioLogger;
import frc.robot.subsystems.Limelight.camMode;
import frc.robot.subsystems.Limelight.ledMode;

public class TargetEntity extends CommandBase {
	private static final double Kp = -0.1f; // Proportional control constant
	private static final double X_OFFSET = 0.05; // The number of degrees camera is off center
	private static final double Speed = 0.75; // A decimal representing the % speed that the turret should turn at

	private boolean hasValidTarget; // Updated by the LimeLight camera
	private double power;
	private double turretPower; // Updated by the LimeLight camera

	public TargetEntity() {
		super();
		hasValidTarget = false;
		power = 0;
		turretPower = 0;
		RioLogger.log("Tracking started");
	}

	@Override
	public void execute() {
		OI.limelight.setLedMode(ledMode.ON); // Turn on the LED's if they haven't been turned on before
		OI.limelight.setCamMode(camMode.VISION); // Turn on vision mode if it wasn't turned on before

		// Driving
		hasValidTarget = OI.limelight.hasTargets();
		if (!hasValidTarget) {
			power = 0.0;
			turretPower = 0.0;
		} else {
			double x = OI.limelight.tx();
			double error = -x;

			// Instead of waiting for the target to go off the screen, center the target
			if (x > 1.0) {
				power = Kp * error - X_OFFSET;
			} else if (x < 1.0) {
				power = Kp * error + X_OFFSET;
			}

			turretPower = (-(OI.gamePad.getX(Hand.kLeft)) + power) * Speed;
		}

		OI.turret.setTurretSpeed(turretPower);
		SmartDashboard.putBoolean("Target.TargetIdentified", hasValidTarget);
		SmartDashboard.putNumber("Target.Power", power);
		SmartDashboard.putNumber("Target.TurretPower", turretPower);
	}

	@Override
	public boolean isFinished() {
		if (OI.gamePad.getRawButtonPressed(RobotMap.padA)) {
			return true;
		}
		return false;
	}

	@Override
	public void end(boolean failed) {
		OI.limelight.setLedMode(ledMode.OFF);
		if (failed) {
			RioLogger.log("Tracking was interupted, press Y to start again");
		} else {
			RioLogger.log("Tracking finished, press Y to start again");
		}
	}

	@Override
	public Set<Subsystem> getRequirements() {
		Set<Subsystem> r = new HashSet<Subsystem>();
		r.add(OI.limelight);
		r.add(OI.turret);
		return r;
	}
}
