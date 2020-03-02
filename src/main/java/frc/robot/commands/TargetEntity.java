package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;
import frc.robot.subsystems.Limelight.LED;
import frc.robot.OI.TurretConstants;

public class TargetEntity extends CommandBase {
	private Limelight limelight;
	private Turret turret;
	private double power; // Updated by the LimeLight camera; equal to power

	/**
	 * 1. Check if Limelight has a target<br>
	 * 2. Get the tx value <br>
	 * 3. Turn the value into a speed by inverting the X value and multiplying it by
	 * the power constant <br>
	 * 4. Tune the power to comply with the minimum power and threshold <br>
	 * 5. Get the additional power from the right stick's Y value <br>
	 * 6. If the total power is greater than 1, set it to 1 <br>
	 * 7. If the total power is less than -1, set it to -1 <br>
	 * 8. Pass the power to the motor <br>
	 * 9. Print the values to SmartDashboard
	 * 
	 * @param ll   Limelight subsystem
	 * @param trrt Turret subsystem
	 * @param gmpd XboxController instance
	 */
	public TargetEntity(Limelight limelight, Turret turret) {
		power = 0;
		this.turret = turret;
		this.limelight = limelight;
		addRequirements(this.limelight);
		addRequirements(this.turret);
	}

	/**
	 * Execute one iteration of the TargetEntity command (For multiple iterations,
	 * call multiple times) Only executes if it is enabled by the game pad
	 */
	@Override
	public void execute() {
		limelight.setLED(Limelight.LED.ON); // Turn on the LED's if they haven't been turned on before
		limelight.setCAM(Limelight.CAM.VISION); // Turn on vision mode if it wasn't turned on before

		// Move the turret if it has a target
		power = getPower();
		turret.setSpeed(power);

		
		SmartDashboard.putNumber("TargetEntity.power", power);
	}

	/**
	 * Preform calculations to calculate the turret power
	 */
	public double getPower() {
		double power;
		double x = limelight.x(); // The number of degrees the target is off center horizontally

		// If the target is within the treshold, do nothing
		if (x < TurretConstants.threshold && x > -TurretConstants.threshold) return 0.0;
		else {
			// Divide the degrees to center by 27
			// Ex. 27 degrees is a power of 1
			// We don't need to verify the value because 0.037037 (k) * 27 is actually 0.999999
			power = TurretConstants.k * -x;

			// Make sure the minimum power value is statisfied
			if (x > TurretConstants.threshold && power > -TurretConstants.minimumPower) return -TurretConstants.minimumPower;
			else if (x < -TurretConstants.threshold && power < TurretConstants.minimumPower) return TurretConstants.minimumPower;
			return power;
		}
	}

	/**
	 * If the the down arrow on the POV is pressed, the command is done. Stops
	 * targeting and turns off the LEDs so people don't get blinded.
	 * 
	 * @param interrupted Whether the command was interrupted/canceled
	 */
	@Override
	public void end(boolean interrupted) {
		limelight.setLED(LED.OFF);
	}
}