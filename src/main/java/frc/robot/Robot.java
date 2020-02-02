package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.TargetEntity;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Limelight.camMode;
import frc.robot.subsystems.Limelight.ledMode;
import frc.robot.subsystems.Turret;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  private final camMode camModeStart = camMode.DRIVER;
  private final ledMode ledModeStart = ledMode.OFF;
  private final Limelight limelight = OI.limelight;

  private final DriveTrain driveTrain = OI.driveTrain;
  private final Turret turret = OI.turret;

  private final XboxController gamePad = OI.gamePad;

  private boolean xPress = false;
  private boolean yPress = false;
  private boolean bPress = false;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
  }

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {
    limelight.setLedMode(ledMode.PIPELINE);
    limelight.setCamMode(camMode.VISION);
    (new TargetEntity()).execute();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    driveTrain.drive(0.0, 0.0);
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
    limelight.setCamMode(camModeStart);
    limelight.setLedMode(ledModeStart);
  }

  /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {
    driveTrain.update();
    turret.update();
    
    // Press the right button to set the zero position of the turret.
    // Also stops the drive train.
    boolean zeroDrive = gamePad.getRawButton(6);
    SmartDashboard.putBoolean("Zero Drive:", zeroDrive);
    if (zeroDrive) {
      // Defines the zero position of the turret
      turret.zeroDrive();
      // Stops the driveTrain
      driveTrain.drive(0.0, 0.0);
    } else {
      driveTrain.drive();
    }

    // Press the left button to zero the turret.
    // This makes it unwind the cables and spin back into the defined zero position.
    boolean zeroTurret = gamePad.getRawButton(5);
    //SmartDashboard.putBoolean("Zero Turret:", zeroTurret);
    //SmartDashboard.putBoolean("Can Zero Turret:", canZeroTurret);
    if (zeroTurret && turret.canZeroTurret) {
      // Checks if the turret is within 500 distance of the zero point
      // If the turret is, it will not zero the turret
      turret.zeroTurret();
    } else {
      turret.updateTalon();
    }

    // Press the B button to switch the camera mode of Limelight.
    // This switches it from DRIVER mode to VISION mode and vice versa.
    if (gamePad.getRawButtonPressed(2) && !bPress) {
      bPress = true;
      limelight.switchCamMode();
    } else {
      bPress = false;
    }

    // Press the X Button to switch the LED mode of Limelight.
    // PIPELINE -> BLINK -> OFF -> ON
    if (gamePad.getRawButtonPressed(3) && !xPress) {
      xPress = true;
      limelight.switchLedMode();
    } else {
      xPress = false;
    }

    // Press the Y Button to switch the LED mode of Limelight from ON and OFF.
    // If the LED is OFF it turns it ON, else it turns it OFF.
    if (gamePad.getRawButtonPressed(4) && !yPress) {
      yPress = true;
      limelight.switchLedModeOnOff();
    } else {
      yPress = false;
    }

    showValuesOnSmartDashboard();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    showValuesOnSmartDashboard();
  }

  @Override
  public void disabledInit() {
    limelight.setLedMode(ledMode.OFF);
    limelight.setCamMode(camMode.DRIVER);
  }

   /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void disabledPeriodic() {
    driveTrain.update();
    turret.update();
    showValuesOnSmartDashboard();
  }

  /**
   * This function shows all stick, velocity, and distance values on SmartDashboard.
   */
  public void showValuesOnSmartDashboard() {
    driveTrain.showValuesOnSmartDashboard();
    turret.showValuesOnSmartDashboard();
  }
}
