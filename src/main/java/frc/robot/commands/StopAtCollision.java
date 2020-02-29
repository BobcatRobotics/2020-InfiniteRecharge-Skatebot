package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import com.kauailabs.navx.frc.AHRS;

import frc.robot.lib.RioLogger;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.NavxGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.*;

// If a collision is detected in auto mode, then stop the robot for 1 second
public class StopAtCollision extends CommandBase {
    private NavxGyro gyro = new NavxGyro(SPI.Port.kMXP);
    private DriveTrain dt = new DriveTrain();
    double lastWorldAccelX = 0.0;
    double lastWorldAccelY = 0.0;
    final static double kCollisionThreshold_DeltaG = 1.2; // Jerk (m/s^3) threshold
    public static boolean collisionDetected = false;

    public StopAtCollision(NavxGyro gyro, DriveTrain dt) {
        this.dt = dt;
        this.gyro = gyro;
        gyro.calibrate();
    }

    public void DetectCollision() {
        // Calculating jerk
        double currWorldAccelX = gyro.getWorldLinearAccelX();
        double currentJerkX = currWorldAccelX - lastWorldAccelX;
        lastWorldAccelX = currWorldAccelX;

        double currWorldAccelY = gyro.getWorldLinearAccelY();
        double currentJerkY = currWorldAccelY - lastWorldAccelY;
        lastWorldAccelY = currWorldAccelX;

        SmartDashboard.putNumber("Acceleration X", currWorldAccelX);
        SmartDashboard.putNumber("Acceleration Y", currWorldAccelY);
        SmartDashboard.putNumber("Jerk X", currentJerkX);
        SmartDashboard.putNumber("Jerk Y", currentJerkY);

        // Testing the actual jerk against the threshold
        if (Math.abs(currentJerkY) > kCollisionThreshold_DeltaG
                && Math.abs(currentJerkX) > kCollisionThreshold_DeltaG) {

            collisionDetected = true;
        } else {
            collisionDetected = false;
        }
    }

    public void StopIfCollision(boolean collisionDetected) throws InterruptedException {
        if (collisionDetected) {
            // Stop the drivetrain
            dt.stop();
            SmartDashboard.putBoolean("Collision? ", true);
        } else {
            SmartDashboard.putBoolean("Collision? ", false);
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        DetectCollision();
        try {
            StopIfCollision(collisionDetected);
        } catch (InterruptedException e) {
            RioLogger.log("Interrupted Exception " + e);
        }
    }
}