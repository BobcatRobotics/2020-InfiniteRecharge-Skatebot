/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.lib.LimelightLogger;
import frc.robot.lib.RioLogger;
import frc.robot.lib.RioLoggerThread;

public class TargetBot extends Command {
	private ShuffleboardTab tab = Shuffleboard.getTab("TargetTuning");
    private NetworkTableEntry tgt_area = tab.add("Target Area", 15.0).getEntry();
	private NetworkTableEntry drive_k = tab.add("Drive K", 0.035).getEntry();
	private NetworkTableEntry steer_k = tab.add("Steer K", 0.015).getEntry();
	private NetworkTableEntry x_offset = tab.add("X Offset", 0.0).getEntry();
 
	private double DESIRED_TARGET_AREA;  // Area of the target when the robot reaches the wall
	private double DRIVE_K; // how hard to drive fwd toward the target
	private double STEER_K; // how hard to turn toward the target
	private double X_OFFSET;  // The number of degrees camera is off center

	// The following fields are updated by the LimeLight Camera
	private boolean hasValidTarget = false;
	private double driveCommand = 0.0;
	private double steerCommand = 0.0;

	// The following fields are updated by the state of the Command
	private boolean ledsON = false;
	private boolean isTargeting = false;
	private LimelightLogger log = new LimelightLogger();

	public TargetBot() {
		super();
		requires(OI.driveTrain);
		requires(OI.limelight);
		initializeCommand();
		RioLogger.errorLog("TargetSkatebot Command Initialized");
	}

	@Override
	protected void execute() {
		// Turn on the LED's if they haven't been turned on before
		if (!ledsON) {
			OI.limelight.turnOnLED();
			ledsON = true;
			isTargeting = false;
			RioLogger.log("TargetSkateBot.execute() LED turned on");
			DESIRED_TARGET_AREA  = tgt_area.getDouble(0.0); 
			DRIVE_K = drive_k.getDouble(0.0); 
			STEER_K = steer_k.getDouble(0.0);
			X_OFFSET = x_offset.getDouble(0.0);  
			RioLogger.errorLog("TargetSkateBot.execute() tgt_area " + DESIRED_TARGET_AREA);
			RioLogger.errorLog("TargetSkateBot.execute() drive k " + DRIVE_K);
			RioLogger.errorLog("TargetSkateBot.execute() steer k" + STEER_K);
			RioLogger.errorLog("TargetSkateBot.execute() x offset " + X_OFFSET);
			RioLoggerThread.log(log.logHeader());
		}

		// Driving
		Update_Limelight_Tracking();
		double leftPwr = (driveCommand - steerCommand) * -1.0;
		double rightPwr = (driveCommand + steerCommand) * -1.0;
		// if (Math.abs(ta0 - ta1) > 0.05) {
		// 	if (ta0 > ta1)
		// 		rightPwr += 0.25;
		// 	else
		// 		leftPwr += 0.25;
		// }
	
		OI.driveTrain.setLeftPower(leftPwr);
		OI.driveTrain.setRightPower(rightPwr);
		OI.driveTrain.drive();
		SmartDashboard.putBoolean("Limelight.TargetIdentified", hasValidTarget);
		SmartDashboard.putNumber("LimeLight.RightPower", rightPwr);
		SmartDashboard.putNumber("LimeLight.LeftPower", leftPwr);

		log.leftPwr = leftPwr;
		log.rightPwr = rightPwr;
		log.logCurrent();
		RioLoggerThread.log(log.logLine());
	}

	@Override
	protected boolean isFinished() {
		boolean stop = false;
		// if (isTargeting) {
		// 	if (!hasValidTarget) {
		// 		stop = true;
		// 		RioLogger.errorLog("TargetSkateBot isFinished stopping. No target");
		// 	}
		// }
		if (OI.gamePad.getRawButton(3)) {
			RioLogger.errorLog("TargetSkateBot isFinished stopping. button pressed");

			stop = true;
		}
		if(OI.limelight.targetArea() > DESIRED_TARGET_AREA){
			RioLogger.errorLog("TargetSkateBot isFinished stopping > TargetArea");
			stop = true;
		}
		return stop;
	}

	@Override
	protected void end() {
		OI.driveTrain.stop();
		OI.limelight.turnOffLED();
		RioLogger.errorLog("TargetSkateBot command finished.");
		initializeCommand();
		RioLoggerThread.log(log.logTrailer());
	}

	/**
	 * This function implements a simple method of generating driving and steering
	 * commands based on the tracking data from a limelight camera.
	 */
	public void Update_Limelight_Tracking() {
		driveCommand = 0.0;
		steerCommand = 0.0;

		hasValidTarget = OI.limelight.hasTargets();
		if (!hasValidTarget) {
			return;
		}
		isTargeting = true;
		// double ty = OI.limelight.y();
		double tx = OI.limelight.x();
		double ta = OI.limelight.targetArea();
	
		// Start with proportional steering
		steerCommand = (tx - X_OFFSET) * STEER_K;
		SmartDashboard.putNumber("Limelight.SteerCommand", steerCommand);

		// try to drive forward until the target area reaches our desired area
		driveCommand = (DESIRED_TARGET_AREA - ta) * DRIVE_K;
		SmartDashboard.putNumber("Limelight.DriveCommand", driveCommand);

		log.drvCmd = driveCommand;
		log.strCmd = steerCommand;
	}

	private void initializeCommand() {
		ledsON = false;
		isTargeting = false;
	}

}
