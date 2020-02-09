package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.lib.RioLogger;

public class ZeroTurret extends CommandBase {

    /**
	 * Initialize the DriveWithJoysticks command
	 */
	public ZeroTurret() {
		super();
        addRequirements(OI.turret);
		RioLogger.log("Zeroing started");
    }

    /*
    1. Returns the limelight camera to the defined zero position on the turret and also makes the turret spin
    2. If the left button on the game pad is pressed, it stores that value in a boolean and displays it on SmartDashboard
    3. If it is pressed, then:
        1. It checks if the turret is within the threshold (250) to be able to zero the turret
        2. If it is, then it moves the camera back to the defined zero position
        3. It will unwind the cables as it is doing this
    4. If it is not pressed, then:
        1. Changes the speed of the turret depending on the left joystick on the game pad
        2. Moving the joystick to the left will make the camera spin counterclockwise
        3. Moving the joystick to the right will make the camera spin clockwise
    5. Switch out of teleoperated mode to stop the command and stop moving the turret
    */

    /**
	 * Execute one iteration of the ZeroTurret command (For multiple iterations, call multiple times)
	 */
    @Override
    public void execute() {
        // Press the left button to zero the turret.
        // This makes it unwind the cables and spin back into the defined zero position.
        SmartDashboard.putBoolean("Can Zero Turret:", OI.turret.canZeroTurret());
        if (OI.gamePad.getRawButton(RobotMap.leftButton) && OI.turret.canZeroTurret()) {
            // Checks if the turret is within 250 distance of the zero point
            // If the turret is, it will not zero the turret
            OI.turret.zeroTurret();
        } else {
            OI.turret.updateTalonSpeed();
        }
    }

    /**
	 * If the robot switches out of teleoperated mode, the command is done.
	 */
	@Override
	public boolean isFinished() {
		if (!RobotState.isOperatorControl()) {
			return true;
		}
		return false;
	}

	/**
	 * Ends the command and stops moving the turret. Prints if it was interrupted.
     * @param failed Whether the command was interrupted/canceled
	 */
	@Override
	public void end(boolean failed) {
		OI.turret.setTurretSpeed(0.0);
		if (failed) {
			RioLogger.log("Zeroing command was interrupted, switch to teleoperated mode to start again");
		} else {
			RioLogger.log("Zeroing command finished, switch to teleoperated mode to start again");
		}
	}
}