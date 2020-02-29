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
    double last_linear_accel_x = 0;
    double last_linear_accel_y = 0;
    final static float kCollisionThreshold_DeltaG = 0.5f; // Jerk (m/s^3) threshold
    public static boolean collisionDetected = false;

    public StopAtCollision(NavxGyro gyro, DriveTrain dt) {
        this.dt = dt;
        this.gyro = gyro;
        gyro.calibrate();
    }

    public void DetectCollision() {
        // Calculating jerk
        long timeInitialX = System.nanoTime();
        last_linear_accel_x = gyro.getWorldLinearAccelX();
        double curr_linear_accel_x = gyro.getWorldLinearAccelX();
        double currentJerkX = (curr_linear_accel_x - last_linear_accel_x) / (System.nanoTime() * 1000000000 - timeInitialX * 1000000000);
        long timeInitialY = System.nanoTime();
        last_linear_accel_y = gyro.getWorldLinearAccelY();
        double curr_linear_accel_y = gyro.getWorldLinearAccelY();
        double currentJerkY = (curr_linear_accel_y - last_linear_accel_y) / (System.nanoTime() * 1000000000 - timeInitialY * 1000000000);

        RioLogger.log("current jerk x : " + currentJerkX);
        RioLogger.log("current jerk y : " + currentJerkY);

        // Testing the actual jerk against the threshold
        if ((Math.abs(currentJerkX) > kCollisionThreshold_DeltaG)
                || (Math.abs(currentJerkY) > kCollisionThreshold_DeltaG)) {
            collisionDetected = true;
        }
    }

    public void StopIfCollision(boolean collisionDetected) throws InterruptedException {
        if (collisionDetected) {
            // Stop the drivetrain
            dt.stop();
            RioLogger.log("collision detected");
        } else {
            RioLogger.log("no collision detected");
        }
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