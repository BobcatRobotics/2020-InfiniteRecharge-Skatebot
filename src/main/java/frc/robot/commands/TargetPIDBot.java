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
import frc.robot.subsystems.LimelightPID;

public class TargetPIDBot extends Command {
	private ShuffleboardTab tab = Shuffleboard.getTab("TargetTuning");
    private NetworkTableEntry tgt_area = tab.add("Target Area", 15.0).getEntry();
	private NetworkTableEntry nte_p = tab.add("PID P", 0.5).getEntry();
	private NetworkTableEntry nte_i = tab.add("PID I", 0.025).getEntry();
	private NetworkTableEntry nte_d = tab.add("PID D", 0.01).getEntry();
 
	private LimelightPID llPID;
	private double DESIRED_TARGET_AREA;  // Area of the target when the robot reaches the wall
	private static double P;
    private static double I;
    private static double D;

	// The following fields are updated by the LimeLight Camera
	private boolean hasValidTarget = false;

	// The following fields are updated by the state of the Command
	private boolean ledsON = false;
	private LimelightLogger log = new LimelightLogger();

	public TargetPIDBot() {
		super();
		requires(OI.driveTrain);
		requires(OI.limelight);
		RioLogger.errorLog("TargetPIDBot Command Initialized");
	}

	@Override
	protected void execute() {
		// Turn on the LED's if they haven't been turned on before
		if (!ledsON) {
			OI.limelight.turnOnLED();
			ledsON = true;
			RioLogger.log("TargetPIDBot.execute() LED turned on");
			DESIRED_TARGET_AREA  = tgt_area.getDouble(0.0); 
			P = nte_p.getDouble(0.0); 
			I = nte_i.getDouble(0.0);
			D = nte_d.getDouble(0.0);  
			RioLogger.errorLog("TargetPIDBot.execute() tgt_area " + DESIRED_TARGET_AREA);
			RioLogger.errorLog("TargetPIDBot.execute() PID " +  P + ", " + I + ", " + D);
			llPID = new LimelightPID(P, I, D, DESIRED_TARGET_AREA);
			llPID.start();
			RioLoggerThread.log(log.logHeader());
		}

		// Driving
		hasValidTarget = OI.limelight.hasTargets();
		if (!hasValidTarget) {
			return;
		}
		double driveCommand = llPID.getDriveSpeed();
		double steerCommand = 0.0;
	
		double leftPwr = (driveCommand - steerCommand) * -1.0;
		double rightPwr = (driveCommand + steerCommand) * -1.0;

		// double ta0 = OI.limelight.targetArea(0);
		// double ta1 = OI.limelight.targetArea(1);
		// if (Math.abs(ta0 - ta1) > 0.05) {
		// 	if (ta0 > ta1)
		// 		rightPwr += 0.25;
		// 	else
		// 		leftPwr += 0.25;
		// }
	
		leftPwr =  0.0;
		rightPwr = 0.0;
		
		OI.driveTrain.setLeftPower(leftPwr);
		OI.driveTrain.setRightPower(rightPwr);
		OI.driveTrain.drive();
		SmartDashboard.putBoolean("Limelight.TargetIdentified", hasValidTarget);
		SmartDashboard.putNumber("LimeLight.RightPower", rightPwr);
		SmartDashboard.putNumber("LimeLight.LeftPower", leftPwr);

		log.logCurrent();
		log.drvCmd = driveCommand;
		log.strCmd = steerCommand;
		log.leftPwr = leftPwr;
		log.rightPwr = rightPwr;
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
		if (!hasValidTarget) {
			stop = true;
			RioLogger.errorLog("TargetPIDBot isFinished stopping. No target");
		}
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
		llPID.stop();
		ledsON = false;
		RioLogger.errorLog("TargetPIDBot command finished.");
		RioLoggerThread.log(log.logTrailer());
	}
}
