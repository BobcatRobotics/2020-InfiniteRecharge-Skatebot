package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.lib.RioLogger;
import frc.robot.subsystems.DriveTrain;

public class SwitchScalar extends CommandBase {

    DriveTrain drivetrain;

    public SwitchScalar(DriveTrain dTrain) {

        this.drivetrain = dTrain;
        RioLogger.log("TargetEntity Command Initialized");
    }

    @Override
    public void execute() {
        drivetrain.toggleSqrt();
    }
}