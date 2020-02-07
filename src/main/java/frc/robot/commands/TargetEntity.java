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
import frc.robot.lib.RioLoggerThread;
import frc.robot.subsystems.Limelight.camMode;
import frc.robot.subsystems.Limelight.ledMode;

public class TargetEntity extends CommandBase {
	private static final double Kp = -0.1f; // Proportional control constant
	private static final double X_OFFSET = 0.05; // The number of degrees camera is off center
	private static final double Speed = 0.75; // A decimal representing the % speed that the turret should turn at

	private boolean hasValidTarget; // Updated by the LimeLight camera
	private double power;
	private double turretPower; // Updated by the LimeLight camera

	private Log log; // This is updated by the state of the Command

	public TargetEntity() {
		super();
		hasValidTarget = false;
		power = 0;
		turretPower = 0;
		log = new Log();
		RioLogger.log("Tracking started");
	}

	@Override
	public void execute() {
		OI.limelight.setLedMode(ledMode.ON); // Turn on the LED's if they haven't been turned on before
		OI.limelight.setCamMode(camMode.VISION); // Turn on vision mode if it wasn't turned on before

		// Driving
		Update_Limelight_Tracking();

		OI.turret.setTurretSpeed(turretPower);
		SmartDashboard.putBoolean("Limelight.TargetIdentified", hasValidTarget);
		SmartDashboard.putNumber("LimeLight.TurretPower", turretPower);

		log.turretPower = turretPower;
		RioLoggerThread.log(log.logLine());
	}

	/**
	 * This function implements a simple method of generating driving and steering
	 * commands based on the tracking data from a limelight camera.
	 */
	public void Update_Limelight_Tracking() {
		hasValidTarget = OI.limelight.hasTargets();
		if (!hasValidTarget) {
			power = 0.0;
			return;
		}

		double tx = OI.limelight.x();
		double heading_error = -tx;

		// Instead of waiting for the target to go off the screen, center the target
		if (tx > 1.0) {
			power = Kp * heading_error - X_OFFSET;
		} else if (tx < 1.0) {
			power = Kp * heading_error + X_OFFSET;
		}

		turretPower = (-(OI.gamePad.getX(Hand.kLeft)) + power) * Speed;
		SmartDashboard.putNumber("Limelight.TurretPower", turretPower);
		log.tx = tx;
		log.strCmd = power;
	}

	class Log {
		double tx = 0.0;
		double strCmd = 0.0;
		double turretPower = 0.0;

		public String logLine() {
			return String.format("%6.4f %6.4f %6.4f", tx, strCmd, turretPower);
		}
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
