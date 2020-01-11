/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.OI;
import frc.robot.lib.RioLogger;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class DriveSkateBot extends Command {
	
	public DriveSkateBot() {
		super();
		requires(OI.driveTrain);
		RioLogger.errorLog("DriveSkateBot Command Initialized");
	}

	@Override
	protected void execute() {
		// Driving
		double right = OI.gamePad.getRawAxis(Joystick.AxisType.kY.value);
		double left = OI.gamePad.getRawAxis(Joystick.AxisType.kTwist.value);

		//Making the controls like Forza
		//double reverse = OI.gamePad.getRawAxis(3);
		//double forward = OI.gamePad.getRawAxis(2);
		//double turn = OI.gamePad.getRawAxis(Joystick.AxisType.kX.value);
		double leftPower = 0.0;
		double rightPower = 0.0;

		if (Math.abs(right) < 0.02) {
			right = 0.0;
			//forward = 0.0;
			//done to prevent motor wear, in case joystick doesn't center
		}

		if (Math.abs(left) < 0.02) {
			left = 0.0;
			//reverse = 0.0;
			//done to prevent motor wear, in case joystick doesn't center
		}

		/* if (Math.abs(turn) < 0.2) {
			turn = 0.0;
			//done to prevent motor wear, in case joystick doesn't center
		} */
		
		// reverse = -1 * reverse;

		// double power = forward + reverse;
		//rightPower = power;
		//leftPower = power;

		rightPower = left;
		leftPower = right;

		// if (power > 0.0) {
		// 	if (turn < 0.0) {
		// 		leftPower -= turn;
		// 	} else if (turn > 0.0) {
		// 		rightPower += turn;
		// 	}	
		// } else if (power < 0.0) {
		// 	if (turn < 0.0) {
		// 		rightPower += turn;
		// 	} else if (turn > 0.0) {
		// 		leftPower -= turn;
		// 	}
		// } else {
		// 	if (turn < 0.0) {
		// 		rightPower += turn;
		// 	} else if (turn > 0.0) {
		// 		leftPower -= turn;
		// 	}
		// }
		

		OI.driveTrain.setLeftPower(rightPower);
		OI.driveTrain.setRightPower(leftPower);
		OI.driveTrain.drive();
	}


	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		OI.driveTrain.stop();
		RioLogger.errorLog("DriveSkateBot Command end()");
	}

	@Override
	protected void interrupted() {
	  RioLogger.errorLog("DriveSkateBot interrupted");
	}
}
