package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.subsystems.DriveTrain;

public class PushBot extends CommandBase {

    private DriveTrain drivetrain;

    public PushBot(DriveTrain dt) {
        this.drivetrain = dt;
        addRequirements(drivetrain);
    }


    @Override
    public void execute() {
        if (OI.collision) {
            drivetrain.stop();
        } else if (!OI.collision) {
            drivetrain.setRightPower(1.0);
            drivetrain.setLeftPower(1.0);
            drivetrain.drive();
        }
    }
}