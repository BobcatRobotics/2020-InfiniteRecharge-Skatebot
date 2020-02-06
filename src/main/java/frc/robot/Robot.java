package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.TargetEntity;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Limelight.camMode;
import frc.robot.subsystems.Limelight.ledMode;
import frc.robot.subsystems.Turret;

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

  private TargetEntity targetEntity = new TargetEntity();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {
    driveTrain.stop();
    if (!CommandScheduler.getInstance().isScheduled(targetEntity)) {
      targetEntity.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
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
    driveTrain.updateAndShowValues();
    turret.updateAndShowValues();
    
    // Press the right button to set the zero position of the turret.
    // Also stops the drive train.
    boolean zeroDrive = gamePad.getRawButton(RobotMap.rightButton);
    SmartDashboard.putBoolean("Zero Drive:", zeroDrive);
    if (zeroDrive) {
      // Defines the zero position of the turret
      turret.zeroDrive();
      driveTrain.stop();
    } else {
      driveTrain.drive();
    }

    // Press the left button to zero the turret.
    // This makes it unwind the cables and spin back into the defined zero position.
    boolean zeroTurret = gamePad.getRawButton(RobotMap.leftButton);
    SmartDashboard.putBoolean("Zero Turret:", zeroTurret);
    SmartDashboard.putBoolean("Can Zero Turret:", turret.canZeroTurret());
    if (zeroTurret && turret.canZeroTurret()) {
      // Checks if the turret is within 500 distance of the zero point
      // If the turret is, it will not zero the turret
      turret.zeroTurret();
    } else {
      turret.updateTalonSpeed();
    }

    // Press the B button to switch the camera mode of Limelight.
    // This switches it from DRIVER mode to VISION mode and vice versa.
    if (gamePad.getRawButtonPressed(RobotMap.padB) && !bPress) {
      bPress = true;
      limelight.switchCamMode();
    } else {
      bPress = false;
    }

    // Press the X Button to switch the LED mode of Limelight.
    // PIPELINE -> BLINK -> OFF -> ON
    if (gamePad.getRawButtonPressed(RobotMap.padX) && !xPress) {
      xPress = true;
      limelight.switchLedMode();
    } else {
      xPress = false;
    }

    // Press the Y Button to switch the LED mode of Limelight from ON and OFF.
    // Old value -> Off -> On
    if (gamePad.getRawButtonPressed(RobotMap.padY) && !yPress) {
      yPress = true;
      limelight.switchLedModeOnOff();
    } else {
      yPress = false;
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    driveTrain.updateAndShowValues();
    turret.updateAndShowValues();
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
    driveTrain.updateAndShowValues();
    turret.updateAndShowValues();
  }
}
