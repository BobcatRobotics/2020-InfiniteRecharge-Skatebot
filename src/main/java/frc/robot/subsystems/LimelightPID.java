/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class LimelightPID extends PIDSubsystem {
  private Limelight limelight = new Limelight();
  // private static double P = 1.0;
  // private static double I = 0.01;
  // private static double D = 0.025;

  // This value needs to be set based on the area the BOT
  // sees when it is on the target
  private double tgt_area = 0.0;
  private double driveSpeed = 0.0;
  
 
  public LimelightPID(double P, double I, double D, double target_area) {
    super("LimeLightTracker", P, I, D);
    setSetpoint(target_area);
    setInputRange(0.0, target_area);
    setOutputRange(0.0, 1.0);
    setPercentTolerance(1.0);
    this.tgt_area = target_area;
  }

  public void start() {
    enable();
  }

  public void stop() {
    disable();
  }

  public double getDriveSpeed() {
    return this.driveSpeed;
  }

  @Override
  protected double returnPIDInput() {
    return limelight.targetArea();
  }

  @Override
  protected void usePIDOutput(double output) {
    driveSpeed = output;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

}
