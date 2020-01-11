package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.lib.RioLogger;
import frc.robot.lib.RioLoggerThread;
import frc.robot.lib.SmartDashLog;
import frc.robot.subsystems.*;
import frc.robot.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  // Loggers
  //public static RioLoggerThread logFile;
  //public static SmartDashLog smartLog = new SmartDashLog();

  // Drive Chain Subsystem
  // Competition and Practive Bot
  //public static DriveTrain driveTrain = new DriveTrain();
  // SKATEBOT
  public static DriveTrainSkatebot driveTrain = new DriveTrainSkatebot();

  //Wrist Subsystem
  //public static Wrist wrist = new Wrist();

  // Joysticks
  //public static Joystick rightStick = new Joystick(RobotMap.rightJoystick);
  //public static Joystick leftStick = new Joystick(RobotMap.leftJoystick);
  public static Joystick gamePad = new Joystick(RobotMap.gamePad);

  //Solenoids
  //public static Solenoid shifter = new Solenoid(RobotMap.shiftSolenoid);

  //Elevator
  ///public static Elevator elev1 = new Elevator();
  //public static boolean limitOn = false;

  //Panel Intake
 // public static PanelIntake panel = new PanelIntake();

  //Cargo Roller
  // public static CargoRoller cargo = new CargoRoller();

  //Peg leg
  //public static PegLeg pegleg = new PegLeg();

  //Lock
  //public static Lock lock = new Lock();

  // Camera
  //public static Camera camera = new Camera();
  public static Limelight limelight = new Limelight();
 
  // Buttons
 // public static Button btnRollerIn = new JoystickButton(gamePad, RobotMap.cargoInB);
  //public static Button btnRollerOut = new JoystickButton(gamePad, RobotMap.cargoOutB);
  public static Button btnTargetRobot = new JoystickButton(gamePad, 2);
  public static Button btnDriveStraight = new JoystickButton(gamePad, 1);
  public static Button btnToggleLED = new JoystickButton(gamePad, 4);
  // public static Button btnPanelHold = new JoystickButton(gamePad, RobotMap.panelHoldB);
  // public static Button btnPanelRelease = new JoystickButton(gamePad, RobotMap.panelReleaseB);
  // public static Button btnWristStow = new JoystickButton(gamePad, RobotMap.wristStowB);
  // public static Button btnWristDep = new JoystickButton(gamePad, RobotMap.wristDepB);
  // public static Button btnWristDel = new JoystickButton(gamePad, RobotMap.wristDelB);
  // public static Button btnPanelInOutToggle = new JoystickButton(gamePad, RobotMap.PanelIntakeInOutB);

  // Triggers
  // public static Trigger trigShifter = new JoystickButton(rightStick, RobotMap.stickShift);
  // public static Trigger lockAndPegTrigger = new JoystickButton(leftStick, RobotMap.lockAndPegEngage);

  static {
   
    // Start Logging Thread
    // logFile = RioLoggerThread.getInstance();
    // Initialize Camera
   // camera.initializeCamera();
   limelight.initializeLimeLight();
   RioLogger.errorLog("limeLight camera is initialized.");

    // trigShifter.whenActive(new ShiftHigh());
    // trigShifter.whenInactive(new ShiftLow());
    // btnRollerIn.whileHeld(new CargoRollerIn());
    // btnRollerOut.whileHeld(new CargoRollerOut());

    //btnTargetRobot.whenPressed(new TargetBot());
    btnTargetRobot.whenPressed(new TargetPIDBot());
    btnDriveStraight.whenPressed(new DriveStraight());
    btnToggleLED.whenPressed(new ToggleLimeLight());

    // btnPanelHold.whenPressed(new PanelIntakeHoldPanel());
    // btnPanelRelease.whenPressed(new PanelIntakeReleasePanel());
    // btnWristStow.whenPressed(new WristStowed());
    // btnWristDep.whenPressed(new WristDeployed());
    // btnWristDel.whenPressed(new WristDeliever());
    // lockAndPegTrigger.whenActive(new LockEnable());
    // lockAndPegTrigger.whenInactive(new LockDisable());
    // btnPanelInOutToggle.whenPressed(new PanelIntakeInOutToggle());

    RioLogger.log("OI static block finished.");
  }
}
