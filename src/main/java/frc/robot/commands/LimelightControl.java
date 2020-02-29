package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Limelight.CAM;
import frc.robot.subsystems.Limelight.LED;

public class LimelightControl extends CommandBase {
    private boolean bPress;
    private boolean xPress;
    private boolean yPress;

    private Limelight limelight;
    private Turret turret;

    /**
     * Initialize the SwitchLimelightMode command
     * @param limelight Limelight subsystem
     * @param turret Turret Subsystem
     */
    public LimelightControl(Limelight limelight, Turret turret) {
        bPress = false;
        xPress = false;
        yPress = false;
        this.limelight = limelight;
        this.turret = turret;
        addRequirements(this.limelight);
        addRequirements(this.turret);
    }

    /**
     * Bind buttons on the gamepad
     */
    @Override
    public void execute() {
        // Press the B button to switch the camera mode of Limelight.
        // DRIVER -> VISION
        if (OI.gamePad.getRawButtonPressed(RobotMap.padB) && !bPress) {
            bPress = true;
            if (limelight.getCAM() == CAM.VISION) limelight.setCAM(CAM.DRIVER);
            else limelight.setCAM(CAM.VISION);
        } else {
            bPress = false;
        }

        // Press the X Button to switch the LED mode of Limelight.
        // PIPELINE -> BLINK -> OFF -> ON
        if (OI.gamePad.getRawButtonPressed(RobotMap.padX) && !xPress) {
            xPress = true;
            if (limelight.getLED() == LED.PIPELINE) limelight.setLED(LED.BLINK);
            else if (limelight.getLED() == LED.BLINK) limelight.setLED(LED.OFF);
            else if (limelight.getLED() == LED.OFF) limelight.setLED(LED.ON);
            else limelight.setLED(LED.PIPELINE);
        } else {
            xPress = false;
        }

        // Press the Y Button to switch the LED mode of Limelight from ON and OFF.
        // Old value -> Off -> On
        if (OI.gamePad.getRawButtonPressed(RobotMap.padY) && !yPress) {
            yPress = true;
            if (limelight.getLED() == LED.OFF) limelight.setLED(LED.ON);
            else limelight.setLED(LED.OFF);
        } else {
            yPress = false;
        }
        
        // Press the left button to zero the Limelight camera.
        // This makes it unwind the cables and spin back into the defined zero position.
        boolean canZero = Math.abs(turret.getDistance()) > OI.zeroThreshold;
        SmartDashboard.putBoolean("Can Zero Limelight:", canZero);
        if (OI.gamePad.getRawButton(RobotMap.leftButton) && canZero) {
            // Checks if the turret is within 250 distance of the zero point
            // If the turret is, it will not zero the Limelight camera
            if (turret.getDistance() > -(OI.zeroThreshold)) {
                turret.setSpeed(-(Math.abs(turret.getStick()) * 0.5) + 0.1);
            } else {
                turret.setSpeed(Math.abs(turret.getStick()) * 0.5 + 0.1);
            }
        } else {
            turret.updateTalonSpeed();
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
     * @param failed Whether the command was interrupted/canceled
	 */
	@Override
	public void end(boolean failed) {
        bPress = false;
        xPress = false;
        yPress = false;
	}
}