package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.lib.RioLogger;

public class Limelight extends SubsystemBase {
    // The mode that Limelight's camera will start in
    private static final camMode camModeStart = camMode.DRIVER;
    // The mode that Limelight's LEDs will start in
    private static final ledMode ledModeStart = ledMode.OFF;

    private NetworkTable limelight;
    private NetworkTableEntry tTarget;
    private NetworkTableEntry tx;
    private NetworkTableEntry ty;
    private NetworkTableEntry ta;
    // private NetworkTableEntry ta0;
    // private NetworkTableEntry ta1;

    private boolean xPress;
    private boolean yPress;
    private boolean bPress;

    public enum ledMode {
        PIPELINE(0), OFF(1), BLINK(2), ON(3);

        private double value;

        private ledMode(double value) {
            this.value = value;
        }
    }

    public enum camMode {
        VISION(0), DRIVER(1);

        private double value;

        private camMode(double value) {
            this.value = value;
        }
    }

    public Limelight() {
        // Initialize Limelight
        super();
        setCamMode(camModeStart);
        setLedMode(ledModeStart);
        xPress = false;
        yPress = false;
        bPress = false;

        limelight = NetworkTableInstance.getDefault().getTable("limelight");
        try {
            tTarget = limelight.getEntry("tv");
            tx = limelight.getEntry("tx");
            ty = limelight.getEntry("ty");
            ta = limelight.getEntry("ta");
            // ta0 = limelight.getEntry("ta0");
            // ta1 = limelight.getEntry("ta1");
        } catch (Exception e) {
            RioLogger.errorLog("Unable to initialize LimeLight. Error is " + e);
        }
        RioLogger.log("Limelight initialized");
    }

    /**
     * @return Whether the limelight has any valid targets
     */
    public boolean hasTargets() {
        return tTarget.getDouble(0.0) == 1.0;
    }

    /**
     * @return Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
     */
    public double tx() {
        return tx.getDouble(0.0);
    }

    /**
     * @return Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
     */
    public double ty() {
        return ty.getDouble(0.0);
    }

    /**
     * @return Target Area (0% of image to 100% of image)
     */
    public double ta() {
        return ta.getDouble(0.0);
    }

    // public double ta1() {
    // return ta1.getDouble(0.0);
    // }

    // public double ta0() {
    // return ta0.getDouble(0.0);
    // }

    /**
     * @return The current mode of the Limelight LEDs.
     */
    public ledMode getLedMode() {
        double entry = (double) getLedModeEntry().getNumber(0);
        if (entry == 0) {
            return ledMode.PIPELINE;
        } else if (entry == 1) {
            return ledMode.OFF;
        } else if (entry == 2) {
            return ledMode.BLINK;
        }
        return ledMode.ON;
    }

    private NetworkTableEntry getLedModeEntry() {
        return limelight.getEntry("ledMode");
    }

    /**
     * @param mode Sets the LED mode of Limelight to this.
     */
    public void setLedMode(ledMode mode) {
        getLedModeEntry().setNumber(mode.value);
        RioLogger.log("Led mode: " + mode.value);
    }

    /**
     * @return The current mode of the Limelight camera.
     */
    public camMode getCamMode() {
        double entry = (double) getCamModeEntry().getNumber(0);
        if (entry == 0) {
            return camMode.VISION;
        }
        return camMode.DRIVER;
    }

    private NetworkTableEntry getCamModeEntry() {
        return limelight.getEntry("camMode");
    }

    /**
     * @param mode Sets the camera mode of Limelight to this.
     */
    public void setCamMode(camMode mode) {
        getCamModeEntry().setNumber(mode.value);
        RioLogger.log("Cam mode: " + mode.value);
    }

    /**
     * Switches the camera mode from DRIVER to VISION and vice versa.
     */
    public void switchCamMode() {
        camMode cam = getCamMode();
        if (cam == camMode.DRIVER) {
            setCamMode(camMode.VISION);
        } else {
            setCamMode(camMode.DRIVER);
        }
        System.out.println("camMode: " + getCamMode().name());
    }

    /**
     * Switches the LED mode from PIPELINE to BLINK to OFF to ON.
     */
    public void switchLedMode() {
        ledMode led = getLedMode();
        if (led == ledMode.PIPELINE) {
            setLedMode(ledMode.BLINK);
        } else if (led == ledMode.BLINK) {
            setLedMode(ledMode.OFF);
        } else if (led == ledMode.OFF) {
            setLedMode(ledMode.ON);
        } else {
            setLedMode(ledMode.PIPELINE);
        }
        System.out.println("ledMode: " + getLedMode().name());
    }

    /**
     * Switches the LED mode to ON if it is in OFF mode, else switches mode to OFF.
     */
    public void switchLedModeOnOff() {
        ledMode led = getLedMode();
        if (led == ledMode.OFF) {
            setLedMode(ledMode.ON);
        } else {
            setLedMode(ledMode.OFF);
        }
        System.out.println("ledMode: " + getLedMode().name());
    }

    /**
     * This method is called periodically by the CommandScheduler.
     */
    @Override
    public void periodic() {
        // This block executes periodically during teleoperated mode.
        if (RobotState.isOperatorControl()) {
            // Press the B button to switch the camera mode of Limelight.
            // DRIVER -> VISION
            if (OI.gamePad.getRawButtonPressed(RobotMap.padB) && !bPress) {
                bPress = true;
                switchCamMode();
            } else {
                bPress = false;
            }

            // Press the X Button to switch the LED mode of Limelight.
            // PIPELINE -> BLINK -> OFF -> ON
            if (OI.gamePad.getRawButtonPressed(RobotMap.padX) && !xPress) {
                xPress = true;
                switchLedMode();
            } else {
                xPress = false;
            }

            // Press the Y Button to switch the LED mode of Limelight from ON and OFF.
            // Old value -> Off -> On
            if (OI.gamePad.getRawButtonPressed(RobotMap.padY) && !yPress) {
                yPress = true;
                switchLedModeOnOff();
            } else {
                yPress = false;
            }
        // This block executes periodically during disabled mode.
        } else if (RobotState.isDisabled()) {
            xPress = false;
            yPress = false;
            bPress = false;
            setLedMode(ledMode.OFF);
        }
    }
}
