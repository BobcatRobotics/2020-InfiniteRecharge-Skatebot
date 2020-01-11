/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.lib.LimelightLogger;
import frc.robot.lib.RioLogger;
import frc.robot.lib.RioLoggerThread;

public class TargetAdjustBot extends Command {
	private static double DESIRED_TARGET_AREA = 16.4; // Area of the target when the robot reaches the wall
	private static double DRIVE_K = 0.020; // how hard to drive fwd toward the target
	private static double DRIVE_I = 0.08; //tune integral
	private static double STEER_K = 0.010; //0.015; // how hard to turn toward the target
	private static double X_OFFSET = -10.4;  // The number of degrees camera is off center
	private static double dt = .02; 

	// The following fields are updated by the LimeLight Camera
	private boolean hasValidTarget = false;
	private double driveCommand = 0.0;
	private double steerCommand = 0.0;
	private double driveIntegral = 0.0;
	private double steerIntegral = 0.0;


	// The following fields are updated by the state of the Command
	private boolean ledsON = false;
	private boolean isTargeting = false;
	private LimelightLogger log = new LimelightLogger();

	public TargetAdjustBot() {
		super();
		requires(OI.driveTrain);
		requires(OI.limelight);
		initializeCommand();
		RioLogger.errorLog("TargetAdjustBot Command Initialized");
	}

	@Override
	protected void execute() {
		// Turn on the LED's if they haven't been turned on before
		if (!ledsON) {
			OI.limelight.turnOnLED();
			ledsON = true;
			RioLogger.log("TargetAdjustBot.execute() LED turned on");
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

		log.drvCmd = driveCommand;
		log.strCmd = steerCommand;
		log.leftPwr = leftPwr;
		log.rightPwr = rightPwr;
		log.logCurrent();
		RioLoggerThread.log(log.logLine());
	}

	@Override
	protected boolean isFinished() {
		boolean stop = false;
		// if (isTargeting) {
		// if (!hasValidTarget) {
		// stop = true;
		// }
		// if (speedToTarget < 0.01) {
		// stop = true;
		// }
		// }
		if (OI.gamePad.getRawButton(3)) {
			stop = true;
		}
		if(OI.limelight.targetArea() > DESIRED_TARGET_AREA){
			stop = true;
		}
		return stop;
	}

	@Override
	protected void end() {
		OI.driveTrain.stop();
		OI.limelight.turnOffLED();
		RioLogger.errorLog("TargetAdjustBot command finished.");
		initializeCommand();
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
		double distanceError = DESIRED_TARGET_AREA - ta;
		if( Math.abs(distanceError) < 2){
			driveIntegral = driveIntegral + dt*distanceError;
		}
		else{
			driveIntegral = 0.0;
		}
		RioLogger.errorLog("Drive Integral:" + driveIntegral + "Actual: " + DRIVE_K*distanceError);
		RioLogger.errorLog("Drive Error:" + driveIntegral + "Actual Drive Error: " + driveIntegral * DRIVE_I);
		driveCommand = DRIVE_K*distanceError + driveIntegral * DRIVE_I;
		SmartDashboard.putNumber("Limelight.DriveCommand", driveCommand);
	}

	private void initializeCommand() {
		ledsON = false;
		isTargeting = false;
	}
}
