/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.commands;

import frc.robot.OI;
import frc.robot.lib.RioLogger;
import edu.wpi.first.wpilibj.command.Command;

public class DriveStraight extends Command {
	private static double DRIVE_SPEED = 0.25;
	private static double NBR_LOOPS = 25;

	private int loopNbr = 0;

	public DriveStraight() {
		super();
		requires(OI.driveTrain);
		loopNbr = 0;
		RioLogger.errorLog("DriveStraight Command Initialized");
	}

	@Override
	protected void execute() {
		OI.driveTrain.setLeftPower(DRIVE_SPEED*-1.0);
		OI.driveTrain.setRightPower(DRIVE_SPEED*-1.0);
		OI.driveTrain.drive();
		loopNbr++;
	}

	@Override
	protected boolean isFinished() {
		if (loopNbr > NBR_LOOPS)
			return true;
		return false;
	}

	@Override
	protected void end() {
		OI.driveTrain.stop();
		loopNbr = 0;
		RioLogger.errorLog("DriveStraight command finished.");
	}

}
