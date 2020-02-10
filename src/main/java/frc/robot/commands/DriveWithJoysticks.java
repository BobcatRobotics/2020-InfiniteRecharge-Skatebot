package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.lib.RioLogger;

public class DriveWithJoysticks extends CommandBase {
    
    /**
	 * Initialize the DriveWithJoysticks command
	 */
	public DriveWithJoysticks() {
		super();
        addRequirements(OI.driveTrain);
        addRequirements(OI.turret);
		RioLogger.log("Driving started");
    }

    /*
    1. Drives Skatebot and also defines the zero position of the turret
    2. If the right button on the game pad is pressed, it stores that value in a boolean and displays it on SmartDashboard
    3. If it is not pressed, then the joysticks can be used to drive the Skatebot:
        1. Pushing the left stick forward makes the left wheels go forward
        2. Pushing the right stick forward makes the right wheels go forward
    4. If it is pressed, then:
        1. It detects the current position of the turret
        2. It sets that position as the defined zero position that the Limelight camera will return to
        3. It stops driving
    5. Switch out of teleoperated mode to stop the command and stop driving
    */
    
    /**
	 * Execute one iteration of the DriveWithJoysticks command (For multiple iterations, call multiple times)
	 */
    @Override
    public void execute() {
        // Press the right button to set the zero position of the turret.
        // Also stops the drive train.
        SmartDashboard.putBoolean("Zero Drive:", OI.gamePad.getRawButton(RobotMap.rightButton));
        if (OI.gamePad.getRawButton(RobotMap.rightButton)) {
            // Defines the zero position of the turret
            OI.turret.defineZeroPosition();
            OI.driveTrain.stop();
        } else {
            OI.driveTrain.drive();
        }
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