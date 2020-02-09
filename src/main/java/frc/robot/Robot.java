package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.DriveWithJoysticks;
import frc.robot.commands.SwitchLimelightMode;
import frc.robot.commands.TargetEntity;
import frc.robot.commands.ZeroTurret;

public class Robot extends TimedRobot {
  private DriveWithJoysticks driveWithJoysticks;
  private SwitchLimelightMode switchLimelightMode;
  private TargetEntity targetEntity;
  private ZeroTurret zeroTurret;
  private CommandScheduler scheduler;

  @Override
  public void robotInit() {
    scheduler = CommandScheduler.getInstance();
    driveWithJoysticks = new DriveWithJoysticks();
    switchLimelightMode = new SwitchLimelightMode();
    targetEntity = new TargetEntity();
    zeroTurret = new ZeroTurret();
  }

  @Override
  public void robotPeriodic() {
    /*
     * Runs a single iteration of the scheduler. The execution occurs in the following order: 
     * Subsystem periodic methods are called. 
     * Button bindings are polled, and new commands are scheduled from them.
     * Currently-scheduled commands are executed. 
     * (What this means is that the code in the execute() block of the Command is executed once)
     * End conditions are checked on currently-scheduled commands, 
     * and commands that are finished have their end methods called and are removed. 
     * Any subsystems not being used as requirements have their default methods started.
     */
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
    // Schedules the commands to execute during teloperated mode
    driveWithJoysticks.schedule();
    switchLimelightMode.schedule();
    targetEntity.schedule();
    zeroTurret.schedule();
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }
}
