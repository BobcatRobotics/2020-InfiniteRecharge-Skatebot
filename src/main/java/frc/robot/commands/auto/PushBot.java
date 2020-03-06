package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.commands.Drive;
import frc.robot.subsystems.DriveTrain;

public class PushBot extends CommandBase {

    private double driveSpeed;
    private double driveTime;

    public PushBot(double dS, double dT) {
        driveSpeed = dS;
        driveTime = dT;
    }


    @Override
    public void execute() {
        OI.driveTrain.setSpeed(driveSpeed);
        OI.driveTrain.setTime(driveTime);
        OI.driveTrain.driveStraight();
    }

    @Override
    public void end(boolean interrupted) {
        OI.driveTrain.stop();
    }
}