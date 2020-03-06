package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class Drive extends CommandBase {
	
	private double right = 0.0;
	private double left = 0.0;

    /**
	 * Initialize the DriveWithJoysticks command
	 */
	public Drive() {
		super();
		addRequirements(OI.driveTrain);
		RioLogger.log("Driving started");
	}

    /**
	 * Execute one iteration of the DriveWithJoysticks command (For multiple iterations, call multiple times)
	 */
    @Override
    public void execute() {

		right = OI.gamePad.getY(Hand.kRight);
		left = OI.gamePad.getY(Hand.kLeft);
		
		OI.driveTrain.setRightPower(right);
		OI.driveTrain.setLeftPower(left);

        OI.driveTrain.drive();
    }

    /**
	 * If the robot switches out of teleoperated mode, the command is done.
     * @return Whether the command is finished.
	 */
	@Override
	public boolean isFinished() {
		if (!RobotState.isOperatorControl()) {
			return true;
		}
		return false;
	}

	/**
	 * Ends the command and stops driving. Prints if it was interrupted.
     * @param failed Whether the command was interrupted/canceled
	 */
	@Override
	public void end(boolean failed) {
		OI.driveTrain.stop();
		if (failed) {
			RioLogger.log("Driving command was interrupted, switch to teleoperated mode to start again");
		} else {
			RioLogger.log("Driving command finished, switch to teleoperated mode to start again");
		}
	}
}