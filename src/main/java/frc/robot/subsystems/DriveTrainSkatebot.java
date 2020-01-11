package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrainSkatebot extends Subsystem {
	/** Inverts drive direction **/
	private static final double INVERT_MOTOR = -1.0;
	
	private WPI_VictorSPX leftFront;
	private WPI_TalonSRX rightFront;
	private boolean invertLeft = true;
	private double leftPower = 0.0;
	private double rightPower = 0.0;

	public DriveTrainSkatebot() {
		// Initialize Drive Train
		leftFront = new WPI_VictorSPX(1);
		rightFront = new WPI_TalonSRX(0);
	}
	
	
	// Put methods for controlling this subsystem here. Call these from Commands.
	public void setLeftMotorsReverse(boolean invert) {
		invertLeft = invert;
	}
	
	public double getLeftPower() {
		return leftPower;
	}

	public void setLeftPower(double leftPwr) {
		if (leftPwr > 1.0)
			leftPwr = 1.0;
		else
		if (leftPwr < -1.0)
			leftPwr = -1.0;
			//was leftPwr = 1.0, we thought it should be -1.0
		this.leftPower = leftPwr;
	}

	public double getRightPower() {
		return rightPower;
	}

	public void setRightPower(double rightPwr) {
		if (rightPwr > 1.0)
			rightPwr = 1.0;
		else
		if (rightPwr < -1.0)
			rightPwr = -1.0;
			//was rightPwr = 1.0, we thought it should be -1.0
		this.rightPower = rightPwr;
	}

	public void drive() {
		drive(leftPower,rightPower);
	}
	
	public void drive(double leftPwr, double rightPwr) {
		if (invertLeft)
			leftPwr *= INVERT_MOTOR;
		else
			rightPwr *= INVERT_MOTOR;
		
		double scale = 0.7;
		leftFront.set(leftPwr * scale);
		rightFront.set(rightPwr * scale);
	}

	public void stop() {
		leftPower = 0.0;
		rightPower = 0.0;
		leftFront.stopMotor();
		rightFront.stopMotor();
	}

	public void reset() {
		leftPower = 0.0;
		rightPower = 0.0;
	}

	@Override
	public void initDefaultCommand() {
	  // Set the default command for a subsystem here.
	  // setDefaultCommand(new MySpecialCommand());
	}
}