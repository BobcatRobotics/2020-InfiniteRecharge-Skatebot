package frc.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import com.revrobotics.ColorSensorV3;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // Motor controller assignments
	public static int driveRightMotorFront = 0;
	public static int driveRightMotorMiddle = 1;
	public static int driveRightMotorRear = 2;

	public static int driveLeftMotorFront = 3;
	public static int driveLeftMotorMiddle = 4;
	public static int driveLeftMotorRear = 5;

	public static int cargoMotor = 6;   // Motor to pickup and eject cargo balls
	public static int pegLegMotor = 7;  // Motor on bottom of peg leg

	public static int elevMotor1 = 10;   // Elevator motor 1 has the sensor attached
	public static int elevMotor2 = 11;	
	public static int elevMotor3 = 12;

	// Elevator Limit switches DIO channels
	public static int elevLSwitchT = 0;
	public static int elevLSwitchB = 1;

 	// Define Joy Sticks and gamepads channels
	public static int leftJoystick = 0;     // Left stick is stick 0, and should be first USB
	public static int rightJoystick = 1;    // Right stick is stick 1, and should be second USB
	public static int gamePad = 2;          // Gamepad is stick 2, and should be third USB 

	// Define buttons and switches on the left & right joystick
	public static int stickShift = 3;       // Shift Gears On right stick
	public static int lockAndPegEngage = 3; // Engage/DisEngage Onleft stick
	
	// Define thumbsticks and buttons on the gamepad
	public static int gamePadLeftPwrStick = 1;   // Left Thumb Stick
	public static int gamePadRightPwrStick = 3;  // Right Thumb Stick -- not used at the moment

	public static int cargoInB = 6;              // Button to spin cargo motor to suck in cargo 
	public static int cargoOutB = 8;             // Button to spin cargo motor to eject cargo
	public static int panelHoldB = 5;            // Button to move panel mech to grab panel
	public static int panelReleaseB = 7;         // Button to move panel mech to release panel
	public static int wristStowB = 4;            // Button to completely stow wrist mech
	public static int wristDepB = 2;             // Button to move wrist to pickup cargo from floor
	public static int wristDelB = 3;             // Button to move wrist to deliver position
	public static int PanelIntakeInOutB = 1;     // Button to toggle write mech in/out

	// Solenoids
	public static int shiftSolenoid = 0;      // Drive train high-low gear shift solenoid
	public static int wristSolenoid1 = 5;     // Main wrist solenoid (deploys big wrist mechanism)
	public static int wristSolenoid2 = 7;     // Second wrist solenoid (small tilt to wrist)
	public static int wristLockSol = 4;       // Solenoid to engage wrist lock and peg leg latches
	public static int panelGripSolenoid = 6;  // Solenoid to engage panel hold/release mechanism
	public static int panelInOutSolenoid = 1; // Solenoid to move panel mechanism in/out

	// Color Sensor
	public static I2C.Port colorSensorPort = I2C.Port.kOnboard;
}
