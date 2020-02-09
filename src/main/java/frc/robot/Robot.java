package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.DriveWithJoysticks;
import frc.robot.commands.TargetEntity;
import frc.robot.commands.ZeroTurret;;

public class Robot extends TimedRobot {
  private DriveWithJoysticks driveWithJoysticks;
  private TargetEntity targetEntity;
  private ZeroTurret zeroTurret;
  private CommandScheduler scheduler;
  private boolean upPress;

  @Override
  public void robotInit() {
    upPress = false;
    scheduler = CommandScheduler.getInstance();
    driveWithJoysticks = new DriveWithJoysticks();
    targetEntity = new TargetEntity();
    zeroTurret = new ZeroTurret();
    targetEntity.execute();
  }

  @Override
  public void robotPeriodic() {
    // Runs all Commands and Subsystems. ** Needs to called in a periodic block **
    scheduler.run();
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    scheduler.schedule(driveWithJoysticks);
    scheduler.schedule(zeroTurret);
  }

  @Override
  public void teleopPeriodic() {
    // Press the Up arrow on the POV to schedule the TargetEntity Command
    if (!upPress && OI.gamePad.getPOV(RobotMap.pov) == RobotMap.povUp && !scheduler.isScheduled(targetEntity)) {
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
