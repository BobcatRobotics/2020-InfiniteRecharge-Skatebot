package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.lib.RioLogger;

public class SwitchLimelightMode extends CommandBase {
    private boolean bPress;
    private boolean xPress;
    private boolean yPress;

    /**
     * Initialize the SwitchLimelightMode command
     */
    public SwitchLimelightMode() {
        super();
        bPress = false;
        xPress = false;
        yPress = false;
        addRequirements(OI.limelight);
        RioLogger.log("Switching started");
    }

    /*
    1. Switches the LED and camera modes of Limelight
    2. If the B button on the game pad is pressed: 
        1. It switches between the two camera modes
        2. If it is in VISION mode it switches to DRIVER mode and vice versa
    3. If the X button is pressed:
        1. It switches between the four different LED modes
        2. From PIPELINE -> BLINK -> OFF -> ON -> PIPELINE
    4. If the Y button is pressed:
        1. It switches between the ON and OFF LED modes
        2. If it is in OFF mode, it switches it ON
        3. Else it switches to OFF mode
    5. Switch out of teleoperated mode to stop being able to switch the modes
    */

    /**
     * Execute one iteration of the SwitchLimelightMode command (For multiple
     * iterations, call multiple times)
     */
    @Override
    public void execute() {
        // Press the B button to switch the camera mode of Limelight.
        // DRIVER -> VISION
        if (OI.gamePad.getRawButtonPressed(RobotMap.padB) && !bPress) {
            bPress = true;
            OI.limelight.switchCamMode();
        } else {
            bPress = false;
        }

        // Press the X Button to switch the LED mode of Limelight.
        // PIPELINE -> BLINK -> OFF -> ON
        if (OI.gamePad.getRawButtonPressed(RobotMap.padX) && !xPress) {
            xPress = true;
            OI.limelight.switchLedMode();
        } else {
            xPress = false;
        }

        // Press the Y Button to switch the LED mode of Limelight from ON and OFF.
        // Old value -> Off -> On
        if (OI.gamePad.getRawButtonPressed(RobotMap.padY) && !yPress) {
            yPress = true;
            OI.limelight.switchLedModeOnOff();
        } else {
            yPress = false;
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
	 * Ends the command and stops being able to switch modes. Prints if it was interrupted.
	 */
	@Override
	public void end(boolean failed) {
        bPress = false;
        xPress = false;
        yPress = false;

		if (failed) {
			RioLogger.log("Switching command was interrupted, switch to teleoperated mode to start again");
		} else {
			RioLogger.log("Switching command finished, switch to teleoperated mode to start again");
		}
	}
}