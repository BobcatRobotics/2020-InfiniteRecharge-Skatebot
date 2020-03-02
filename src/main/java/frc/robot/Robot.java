package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

public class Robot extends TimedRobot {
  private static final DriveTrain drivetrain = new DriveTrain();
  private static final Limelight limelight = new Limelight();
  private static final Turret turret = new Turret();

  private static DriveTele drive;
  private static TargetEntity target;

  private static boolean shouldTargetEntity = false;

  @Override
  public void robotInit() {
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
    drive = new DriveTele(drivetrain, limelight, turret);
    target = new TargetEntity(limelight, turret);
  }

  @Override
  public void teleopPeriodic() {
    drive.schedule();

    if (OI.gamePad.getPOV() == RobotMap.povUp || shouldTargetEntity) {
      shouldTargetEntity = true;
      target.schedule();
    } else if (OI.gamePad.getPOV() == RobotMap.povDown) shouldTargetEntity = false;

    CommandScheduler.getInstance().run();
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
