/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Limelight.ledMode;
import frc.robot.Limelight.camMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  private final boolean enableLimelight = false; // Turn on/off the limelight LED's

  private final WPI_TalonSRX leftTalon = new WPI_TalonSRX(7);
  private final WPI_TalonSRX rightTalon = new WPI_TalonSRX(8);
  private final WPI_TalonSRX turretTalon = new WPI_TalonSRX(10);

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(leftTalon, rightTalon);
  private final Joystick l_stick = new Joystick(0);
  private final Joystick r_stick = new Joystick(1);
  private final XboxController gamePad = new XboxController(2);

  private double leftStick = 0.0;
  private double rightStick = 0.0;
  private double leftVelocity = 0.0;
  private double leftDistance = 0.0;
  private double rightDistance = 0.0;
  private double rightVelocity = 0.0;

  private double turretStick = 0.0;
  private double turretVelocity = 0.0;
  private double turretDistance = 0.0;

  private Limelight limelight;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    // Flip the phase of the encoder for use with SRX motion magic, etc.
    // and set current position to 0.0;
    leftTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
    leftTalon.setSelectedSensorPosition(0,0,0);
    leftTalon.setSensorPhase(true);

    rightTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
    rightTalon.setSelectedSensorPosition(0,0,0);
    rightTalon.setSensorPhase(false);

    turretTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,0);
    turretTalon.setSelectedSensorPosition(0,0,0);
    turretTalon.setSensorPhase(false);
  }

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
    limelight = new Limelight();
    if (!enableLimelight) {
      // Disable the Limelight on start so people don't get blinded
      limelight.setLedMode(ledMode.OFF);
    }
  }

  /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {
    leftStick = l_stick.getRawAxis(Joystick.AxisType.kY.value)*0.8;
    rightStick = r_stick.getRawAxis(Joystick.AxisType.kY.value)*0.8;
    turretStick = gamePad.getX(Hand.kLeft)*-1;

    boolean zeroDrive = gamePad.getRawButton(6);
    SmartDashboard.putBoolean("Zero Drive:", zeroDrive);
    if (zeroDrive) {
      turretTalon.setSelectedSensorPosition(0);
      turretDistance = 0.0;
      m_robotDrive.tankDrive(0.0, 0.0);
    } else {
      m_robotDrive.tankDrive(leftStick, rightStick);
    }

    boolean zeroTurret = gamePad.getRawButton(5);
    SmartDashboard.putBoolean("Zero Turret:", zeroTurret);

    boolean canZeroTurret = turretDistance < -500 || turretDistance > 500;
    SmartDashboard.putBoolean("Can Zero Turret:", canZeroTurret);
    if (zeroTurret) {
      if (canZeroTurret) {
        if (turretDistance > -500) {
          turretTalon.set(ControlMode.PercentOutput, Math.abs(turretStick)*-1);
        } else {
          turretTalon.set(ControlMode.PercentOutput, Math.abs(turretStick));
        }
      }
    } else {
      turretTalon.set(ControlMode.PercentOutput, turretStick);
    }

    if (gamePad.getBButton()) {
      camMode cam = limelight.getCamMode();
      if (cam == camMode.DRIVER) {
        limelight.setCamMode(camMode.VISION);
      } else {
        limelight.setCamMode(camMode.DRIVER);
      }
    }

    if (gamePad.getXButton()) {
      ledMode led = limelight.getLedMode();
      if (led == ledMode.PIPELINE) {
        limelight.setLedMode(ledMode.BLINK);
      } else if (led == ledMode.BLINK) {
        limelight.setLedMode(ledMode.OFF);
      } else if (led == ledMode.OFF) {
        limelight.setLedMode(ledMode.ON);
      } else {
        limelight.setLedMode(ledMode.PIPELINE);
      }
    }

    if (gamePad.getYButton()) {
      ledMode led = limelight.getLedMode();
      if (led == ledMode.OFF) {
        limelight.setLedMode(ledMode.ON);
      } else {
        limelight.setLedMode(ledMode.OFF);
      }
    }

    readTalonsAndShowValues();
  }

   /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void disabledPeriodic() {
    leftStick = l_stick.getRawAxis(Joystick.AxisType.kY.value);
    rightStick = r_stick.getRawAxis(Joystick.AxisType.kY.value);
    turretStick = gamePad.getY(Hand.kLeft);

    readTalonsAndShowValues();
}

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  public void readTalonsAndShowValues() {
    leftDistance = leftTalon.getSelectedSensorPosition(0);
    rightDistance = rightTalon.getSelectedSensorPosition(0);
    turretDistance = turretTalon.getSelectedSensorPosition(0);

    leftVelocity = leftTalon.getSelectedSensorVelocity(0);
    rightVelocity = rightTalon.getSelectedSensorVelocity(0);
    turretVelocity = turretTalon.getSelectedSensorVelocity(0);

    SmartDashboard.putNumber("left stick:", leftStick);
    SmartDashboard.putNumber("right stick:", rightStick);
    SmartDashboard.putNumber("turret stick:", turretStick);
    SmartDashboard.putNumber("left distance:", leftDistance);
    SmartDashboard.putNumber("left velocity:", leftVelocity);
    SmartDashboard.putNumber("right distance:", rightDistance);
    SmartDashboard.putNumber("right velocity:", rightVelocity);
    SmartDashboard.putNumber("turret distance:", turretDistance);
    SmartDashboard.putNumber("turret velocity:", turretVelocity);
  }
}
