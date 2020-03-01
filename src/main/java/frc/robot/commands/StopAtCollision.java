package frc.robot.commands;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.lib.RioLogger;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.NavxGyro;

// If a collision is detected in auto mode, then stop the robot for 1 second
public class StopAtCollision extends CommandBase {
    private NavxGyro gyro = new NavxGyro(SPI.Port.kMXP);
    private DriveTrain dt = new DriveTrain();
    double lastWorldAccelX = 0.0;
    double lastWorldAccelY = 0.0;
    long lastSystemTime = 0;
    final static double kCollisionJerkThreshold = 1.2; // Jerk (m/s^3) threshold
    public static boolean collisionDetected = false;

    public StopAtCollision(NavxGyro gyro, DriveTrain dt) {
        this.dt = dt;
        this.gyro = gyro;
        gyro.calibrate();
    }

    public void DetectCollision() {
        // Finds difference in time between current and previous iteration
        long currSystemTime = System.nanoTime(); // Gets the current time in nanoseconds
        long deltaTime = currSystemTime - lastSystemTime; // Elapsed time between current and previous iteration
        double deltaTimeSeconds = deltaTime / Math.pow(10, 9); // Converts the difference in time to seconds
        lastSystemTime = currSystemTime; // Sets the last time to the current one for the next iteration

        // Finds difference in acceleration in X direction between current and previous iteration
        double currWorldAccelX = gyro.getWorldLinearAccelX(); // Gets the current X acceleration
        double deltaAccelX = currWorldAccelX - lastWorldAccelX; // Change between the current and previous acceleration
        lastWorldAccelX = currWorldAccelX; // Sets the last acceleration to the current one for the next iteration
        
        // Finds difference in acceleration in X direction between current and previous iteration
        double currWorldAccelY = gyro.getWorldLinearAccelY(); // Gets the current Y acceleration
        double deltaAccelY = currWorldAccelY - lastWorldAccelY; // Change between the current and previous acceleration
        lastWorldAccelY = currWorldAccelX; // Sets the last acceleration to the current one for the next iteration

        // jerk is the rate at which an object's acceleration changes with respect to time
        // so it has to be divided by time. Measured in g/s
        double currentJerkX = deltaAccelX / deltaTimeSeconds;
        double currentJerkY = deltaAccelY / deltaTimeSeconds;
        
        SmartDashboard.putNumber("Acceleration X", currWorldAccelX);
        SmartDashboard.putNumber("Acceleration Y", currWorldAccelY);
        SmartDashboard.putNumber("Change in Acceleration X", deltaAccelX);
        SmartDashboard.putNumber("Change in Acceleration Y", deltaAccelY);
        SmartDashboard.putNumber("Jerk X", currentJerkX);
        SmartDashboard.putNumber("Jerk Y", currentJerkY);

        // Testing the actual jerk against the threshold
        if (Math.abs(currentJerkY) > kCollisionJerkThreshold
                && Math.abs(currentJerkX) > kCollisionJerkThreshold) {

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
    public void execute() {
        // First detect if there is a collision 
        // (if jerk is greater than threshold)
        DetectCollision();
        try {
            // If there is a collision stop the drive train
            StopIfCollision(collisionDetected);
        } catch (InterruptedException e) {
            RioLogger.log("Interrupted Exception " + e);
        }
    }
}