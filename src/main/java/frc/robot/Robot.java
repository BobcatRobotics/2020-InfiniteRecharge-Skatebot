package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.TargetEntity;
import frc.robot.subsystems.Limelight.camMode;
import frc.robot.subsystems.Limelight.ledMode;

public class Robot extends TimedRobot {
  // The mode that Limelight's camera will start in
  private final camMode camModeStart = camMode.DRIVER;

  // The mode that Limelight's LED's will start in
  private final ledMode ledModeStart = ledMode.OFF;

  private TargetEntity targetEntity;
  private CommandScheduler schedule;
  private boolean xPress;
  private boolean yPress;
  private boolean bPress;
  private boolean upPress;

  @Override
  public void robotInit() {
    xPress = false;
    yPress = false;
    bPress = false;
    upPress = false;
    schedule = CommandScheduler.getInstance();
    targetEntity = new TargetEntity();
    targetEntity.execute();

    OI.limelight.setCamMode(camModeStart);
    OI.limelight.setLedMode(ledModeStart);
    // Toggles square root scaling of the speed of the drive train. Sets it to false
    OI.driveTrain.toggleSqrt();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    // Press the Up arrow on the D-pad to schedule the TargetEntity Command
    if (!upPress && OI.gamePad.getPOV(RobotMap.pov) == RobotMap.povUp && !schedule.isScheduled(targetEntity)) {
      upPress = true;
      targetEntity.schedule();
    } else {
      upPress = false;
    }

    // Press the right button to set the zero position of the turret.
    // Also stops the drive train.
    boolean zeroDrive = OI.gamePad.getRawButton(RobotMap.rightButton);
    SmartDashboard.putBoolean("Zero Drive:", zeroDrive);
    if (zeroDrive) {
      // Defines the zero position of the turret
      OI.turret.zeroDrive();
      OI.driveTrain.stop();
    } else {
      OI.driveTrain.drive();
    }

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

    OI.driveTrain.updateAndShowValues();
    OI.turret.updateAndShowValues();
    SmartDashboard.putNumber("GamePad.POV", OI.gamePad.getPOV(RobotMap.pov));
    // Runs all Commands. ** Needs to called in a periodic block **
    schedule.run();
  }

  @Override
  public void disabledInit() {
    xPress = false;
    yPress = false;
    bPress = false;
    OI.limelight.setLedMode(ledMode.OFF);
  }

  @Override
  public void testPeriodic() {
    OI.driveTrain.updateAndShowValues();
    OI.turret.updateAndShowValues();
  }

  @Override
  public void disabledPeriodic() {
  }
}
