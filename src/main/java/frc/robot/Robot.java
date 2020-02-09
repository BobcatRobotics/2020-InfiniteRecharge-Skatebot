package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.TargetEntity;

public class Robot extends TimedRobot {
  private TargetEntity targetEntity;
  private CommandScheduler schedule;
  private boolean upPress;

  @Override
  public void robotInit() {
    upPress = false;
    schedule = CommandScheduler.getInstance();
    targetEntity = new TargetEntity();
    targetEntity.execute();
  }

  @Override
  public void robotPeriodic() {
    // Runs all Commands and Subsystems. ** Needs to called in a periodic block **
    schedule.run();
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
    if (!upPress && OI.gamePad.getPOV(RobotMap.dpad) == RobotMap.dpadUp && !schedule.isScheduled(targetEntity)) {
      upPress = true;
      targetEntity.schedule();
    } else {
      upPress = false;
    }
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testPeriodic() {
  }
}
