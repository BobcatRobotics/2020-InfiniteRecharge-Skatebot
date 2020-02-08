package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.TargetEntity;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight.camMode;
import frc.robot.subsystems.Limelight.ledMode;
import frc.robot.subsystems.Turret;

public class Robot extends TimedRobot {
  // The mode that Limelight's camera will start in
  private final camMode camModeStart = camMode.VISION;

  // The mode that Limelight's LED's will start in
  private final ledMode ledModeStart = ledMode.ON;

  private static final CommandScheduler schedule = CommandScheduler.getInstance();

  private final DriveTrain driveTrain = OI.driveTrain;
  private final Turret turret = OI.turret;

  private final XboxController gamePad = OI.gamePad;

  private boolean xPress;
  private boolean yPress;
  private boolean bPress;

  private TargetEntity targetEntity;

  @Override
  public void robotInit() {
    xPress = false;
    yPress = false;
    bPress = false;
    OI.limelight.setCamMode(camModeStart);
    OI.limelight.setLedMode(ledModeStart);
  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void autonomousInit() {
    driveTrain.stop();
    targetEntity = new TargetEntity();
    if (!schedule.isScheduled(targetEntity)) {
      targetEntity.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
    if (!schedule.isScheduled(targetEntity) && OI.gamePad.getRawButtonPressed(RobotMap.padY)) {
      targetEntity.schedule();
    }

    schedule.run();
  }

  @Override
  public void teleopInit() {
  }

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
    SmartDashboard.putBoolean("Can Zero Turret:", turret.canZeroTurret());
    if (gamePad.getRawButton(RobotMap.leftButton) && turret.canZeroTurret()) {
      // Checks if the turret is within 250 distance of the zero point
      // If the turret is, it will not zero the turret
      turret.zeroTurret();
    } else {
      turret.updateTalonSpeed();
    }

    // Press the B button to switch the camera mode of Limelight.
    // DRIVER -> VISION
    if (gamePad.getRawButtonPressed(RobotMap.padB) && !bPress) {
      bPress = true;
      OI.limelight.switchCamMode();
    } else {
      bPress = false;
    }

    // Press the X Button to switch the LED mode of Limelight.
    // PIPELINE -> BLINK -> OFF -> ON
    if (gamePad.getRawButtonPressed(RobotMap.padX) && !xPress) {
      xPress = true;
      OI.limelight.switchLedMode();
    } else {
      xPress = false;
    }

    // Press the Y Button to switch the LED mode of Limelight from ON and OFF.
    // Old value -> Off -> On
    if (gamePad.getRawButtonPressed(RobotMap.padY) && !yPress) {
      yPress = true;
      OI.limelight.switchLedModeOnOff();
    } else {
      yPress = false;
    }
  }

  @Override
  public void disabledInit() {
    xPress = false;
    yPress = false;
    bPress = false;
  }

  @Override
  public void testPeriodic() {
    driveTrain.updateAndShowValues();
    turret.updateAndShowValues();
  }

  @Override
  public void disabledPeriodic() {
  }
}
