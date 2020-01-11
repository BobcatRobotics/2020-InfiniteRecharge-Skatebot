/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.commands.DriveSkateBot;
import frc.robot.lib.RioLoggerThread;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  static OI oi = new OI();
  static boolean commandsStarted = false;
  static boolean loggerInit = false;

  // Command m_autonomousCommand;
  // SendableChooser<Command> m_chooser = new SendableChooser<>();
  Command m_DriveWithJoysticks;
  // Command m_MoveElevator;
  // Command m_RunWrist;

  @Override
  public void robotInit() {

    m_DriveWithJoysticks = new DriveSkateBot();
    // m_MoveElevator = new MoveElevator();
    // m_RunWrist = new RunWrist();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void autonomousInit() {
    startCommands();
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    startCommands();
    if (!loggerInit) {
      RioLoggerThread.getInstance();
      RioLoggerThread.setLoggingParameters(600, 60); // 10 mins, 1 min
      loggerInit = true;
    }
  }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  // This function is called periodically during test mode.
  @Override
  public void testPeriodic() {
  }

  // Starts up all commands once
  private void startCommands() {
    if (!commandsStarted) {
      m_DriveWithJoysticks.start();
      // m_MoveElevator.start();
      // m_RunWrist.start();
      // For testing always have joysticks available
      commandsStarted = /*true*/ false;
    }
  }
}
