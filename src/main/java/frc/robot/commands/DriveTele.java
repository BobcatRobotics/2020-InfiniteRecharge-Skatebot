package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.OI.TurretConstants;
import frc.robot.lib.RioLogger;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Limelight.CAM;
import frc.robot.subsystems.Limelight.LED;

public class DriveTele extends CommandBase {
    // The purpose of these are to make an event fire only once every click, and to not repeatedly fire until unpressed
    private boolean bPress;
    private boolean xPress;
    private boolean yPress;

    private DriveTrain driveTrain;
    private Limelight limelight;
    private Turret turret;

    /**
	 * Initialize the DriveWithJoysticks command
	 */
	public DriveTele(DriveTrain driveTrain, Limelight limelight, Turret turret) {
        bPress = false;
        xPress = false;
        yPress = false;
        this.driveTrain = driveTrain;
        this.limelight = limelight;
        this.turret = turret;
        addRequirements(this.driveTrain);
        addRequirements(this.turret);
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
        driveTrain.update();
        turret.update();

        // Press the right button to set the zero position of the turret.
        // Also stops the drive train.
        SmartDashboard.putBoolean("Zero Drive:", OI.gamePad.getRawButton(RobotMap.rightButton));
        if (OI.gamePad.getRawButton(RobotMap.rightButton)) {
            handleDrive();
        } else {
            driveTrain.drive();
        }

        // Press the left button to zero the Limelight camera.
        // This makes it unwind the cables and spin back into the defined zero position.
        boolean canZero = Math.abs(turret.getDistance()) > TurretConstants.zeroThreshold;
        SmartDashboard.putBoolean("Can Zero Limelight:", canZero);
        if (OI.gamePad.getRawButton(RobotMap.leftButton) && canZero) {
            handleCenterLimelight();
        } else {
            turret.updateSpeed();
        }

        // Press the B button to switch the camera mode of Limelight.
        // DRIVER -> VISION
        if (OI.gamePad.getRawButtonPressed(RobotMap.padB) && !bPress) {
            bPress = true;
            handleCamMode();
        } else {
            bPress = false;
        }

        // Press the X Button to switch the LED mode of Limelight.
        // PIPELINE -> BLINK -> OFF -> ON
        if (OI.gamePad.getRawButtonPressed(RobotMap.padX) && !xPress) {
            xPress = true;
            handleAdvancedLedMode();
        } else {
            xPress = false;
        }

        // Press the Y Button to switch the LED mode of Limelight from ON and OFF.
        // Old value -> Off -> On
        if (OI.gamePad.getRawButtonPressed(RobotMap.padY) && !yPress) {
            yPress = true;
            handleBasicLedMode();
        } else {
            yPress = false;
        }
    }

    private void handleDrive() {
        // Defines the zero position of the turret
        OI.turretTalon.setSelectedSensorPosition(0);
        turret.setDistance(0.0);
        driveTrain.stop();
    }

    private void handleCenterLimelight() {
        // Checks if the turret is within 250 distance of the zero point
        // If the turret is, it will not zero the Limelight camera
        if (turret.getDistance() > -TurretConstants.zeroThreshold) {
            turret.setSpeed(-(Math.abs(turret.getStick()) * 0.5) + 0.1);
        } else {
            turret.setSpeed(Math.abs(turret.getStick()) * 0.5 + 0.1);
        }
    }

    private void handleCamMode() {
        if (limelight.getCAM() == CAM.VISION) limelight.setCAM(CAM.DRIVER);
        else limelight.setCAM(CAM.VISION);
    }

    private void handleAdvancedLedMode() {
        if (limelight.getLED() == LED.PIPELINE) limelight.setLED(LED.BLINK);
        else if (limelight.getLED() == LED.BLINK) limelight.setLED(LED.OFF);
        else if (limelight.getLED() == LED.OFF) limelight.setLED(LED.ON);
        else limelight.setLED(LED.PIPELINE);
    }

    private void handleBasicLedMode() {
        if (limelight.getLED() == LED.OFF) limelight.setLED(LED.ON);
        else limelight.setLED(LED.OFF);
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
        bPress = false;
        xPress = false;
        yPress = false;
		driveTrain.stop();
		if (failed) {
			RioLogger.log("Driving command was interrupted");
		} else {
			RioLogger.log("Driving command finished");
		}
	}
}