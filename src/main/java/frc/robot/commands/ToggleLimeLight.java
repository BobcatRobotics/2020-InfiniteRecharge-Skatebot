/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.lib.RioLogger;

public class ToggleLimeLight extends Command {
  private boolean ledON = false;

  public ToggleLimeLight() {
    requires(OI.limelight);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    RioLogger.errorLog("ToggleLimeLight Command Initialized");

  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    RioLogger.errorLog("ToggleLimeLight.execute() ledON switch is " + ledON);
   if (ledON)
      OI.limelight.turnOffLED();
    else
      OI.limelight.turnOnLED();
    ledON = !ledON;
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    RioLogger.errorLog("ToggleLimeLight interrupted");

  }
}
