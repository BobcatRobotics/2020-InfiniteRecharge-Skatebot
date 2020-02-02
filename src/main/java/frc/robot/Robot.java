package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.commands.TargetEntity;
import frc.robot.subsystems.*;
import frc.robot.subsystems.Limelight.camMode;
import frc.robot.subsystems.Limelight.ledMode;

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
    driveTrain.stop();
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
    
    boolean zeroDrive = gamePad.getRawButton(6); //Right Button
    SmartDashboard.putBoolean("Zero Drive:", zeroDrive);
    if (zeroDrive) {
      turret.zeroDrive();
      driveTrain.stop();
    } else {
      driveTrain.drive();
    }

    turret.zeroTurret(); //Left Button to zero the turret

    if (gamePad.getRawButtonPressed(2) && !bPress) { //B
      bPress = true;
      camMode cam = limelight.getCamMode();
      if (cam == camMode.DRIVER) {
        limelight.setCamMode(camMode.VISION);
      } else {
        limelight.setCamMode(camMode.DRIVER);
      }
      System.out.println("camMode: "+limelight.getCamMode().name());
    } else {
      bPress = false;
    }

    if (gamePad.getRawButtonPressed(3) && !xPress) { //X
      xPress = true;
      ledMode led = limelight.getLedMode();
      if (led == ledMode.PIPELINE) {
        limelight.setLedMode(ledMode.BLINK);
      } else if (led == ledMode.BLINK) {
        limelight.setLedMode(ledMode.OFF);
      } else if (led == ledMode.OFF) {
        limelight.setLedMode(ledMode.ON);
      } else {
        limelight.setLedMode(ledMode.PIPELINE);
      }
      System.out.println("ledMode: "+limelight.getLedMode().name());
    } else {
      xPress = false;
    }

    if (gamePad.getRawButtonPressed(4) && !yPress) { //Y
      yPress = true;
      ledMode led = limelight.getLedMode();
      if (led == ledMode.OFF) {
        limelight.setLedMode(ledMode.ON);
      } else {
        limelight.setLedMode(ledMode.OFF);
      }
      System.out.println("ledMode: "+limelight.getLedMode().name());
    } else {
      yPress = false;
    }

    readTalonsAndShowValues();
  }

   /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void disabledPeriodic() {
    driveTrain.update();
    turret.update();
    readTalonsAndShowValues();
}

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    readTalonsAndShowValues();
  }

  public void readTalonsAndShowValues() {
    SmartDashboard.putNumber("left stick:", driveTrain.leftStick);
    SmartDashboard.putNumber("right stick:", driveTrain.rightStick);
    SmartDashboard.putNumber("turret stick:", turret.stick);
    SmartDashboard.putNumber("left distance:", driveTrain.leftDistance);
    SmartDashboard.putNumber("left velocity:", driveTrain.leftVelocity);
    SmartDashboard.putNumber("right distance:", driveTrain.rightDistance);
    SmartDashboard.putNumber("right velocity:", driveTrain.rightVelocity);
    SmartDashboard.putNumber("turret distance:", turret.distance);
    SmartDashboard.putNumber("turret velocity:", turret.velocity);
  }

  @Override
  public void disabledInit() {
    limelight.setLedMode(ledMode.OFF);
    limelight.setCamMode(camMode.DRIVER);
  }
}
