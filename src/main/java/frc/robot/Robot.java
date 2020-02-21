package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.*;

import frc.robot.lib.RioLogger;
import frc.robot.subsystems.NavxGyro;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  private CommandScheduler scheduler;
  private DriveWithJoysticks driveWithJoysticks;
  private SwitchLimelightMode switchLimelightMode;
  private TargetEntity targetEntity;
  private ZeroLimelight zeroLimelight;
  private NavxGyro navx;

  @Override
  public void robotInit() {
    scheduler = CommandScheduler.getInstance();
    // Initializes the commands
    driveWithJoysticks = new DriveWithJoysticks();
    switchLimelightMode = new SwitchLimelightMode();
    targetEntity = new TargetEntity();
    zeroLimelight = new ZeroLimelight();
    navx = new NavxGyro(SPI.Port.kMXP);
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
    /*
     * This schedules the commands that execute during teleoperated mode. 
     * All logic is handled inside their seperate files.
     * When a command is scheduled, its execute() method will run repeatedly.
     * These commands allow driving, switching Limelight modes, targeting, and moving the turret.
     * (Note: Calling an execute() method once does not mean that 
     * it will "complete" the command, it has to be called periodically)
     */
    driveWithJoysticks.schedule();
    switchLimelightMode.schedule();
    targetEntity.schedule();
    zeroLimelight.schedule();
  }

  @Override
  public void teleopPeriodic() {
    SmartDashboard.putNumber("Quaternion X", navx.getQuaternionX());
    SmartDashboard.putNumber("Quaternion Y", navx.getQuaternionY());
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
