package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.lib.RioLogger;

public class Limelight extends SubsystemBase {
    private NetworkTable limelight;
    private NetworkTableEntry tTarget;
    private NetworkTableEntry tx;
    private NetworkTableEntry ty;
    private NetworkTableEntry ta;
    private NetworkTableEntry ta0;
    private NetworkTableEntry ta1;

    public enum ledMode {
        PIPELINE(0),
        OFF(1),
        BLINK(2),
        ON(3);

        private double value;
        private ledMode(double value) {
            this.value = value;
        }
    }

    public enum camMode {
        VISION(0),
        DRIVER(1);

        private double value;
        private camMode(double value) {
            this.value = value;
        }
    }

    public Limelight() {
        // Initialize Limelight
        RioLogger.log("Limelight() created.");
        limelight = NetworkTableInstance.getDefault().getTable("limelight");
        try {
            tTarget = limelight.getEntry("tv");
            tx = limelight.getEntry("tx");
            ty = limelight.getEntry("ty");
            ta = limelight.getEntry("ta");
            ta0 = limelight.getEntry("ta0");
            ta1 = limelight.getEntry("ta1");
        } catch (Exception e) {
            RioLogger.errorLog("Unable to initialize LimeLight. Error is " + e);
        }
    }

    public boolean hasTargets() {
        return tTarget.getDouble(0.0) == 1.0;
    }
 
    public double x() {
        return tx.getDouble(0.0);
    }
 
    public double y() {
        return ty.getDouble(0.0);
    }
 
    public double targetArea() {
        return ta.getDouble(0.0);
    }
 
    public double rightTarget() {
        return ta1.getDouble(0.0);
    }
 
    public double leftTarget() {
        return ta0.getDouble(0.0);
    }

    public ledMode getLedMode() {
        double entry = (double)getLedModeEntry().getNumber(0);
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

    public void setLedMode(ledMode mode) {
        getLedModeEntry().setNumber(mode.value);
    }

    public camMode getCamMode() {
        double entry = (double)getCamModeEntry().getNumber(0);
        if (entry == 0) {
            return camMode.VISION;
        }
        return camMode.DRIVER;
    }

    private NetworkTableEntry getCamModeEntry() {
        return limelight.getEntry("camMode");
    }

    public void setCamMode(camMode mode) {
        getCamModeEntry().setNumber(mode.value);
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
        System.out.println("ledMode: "+ getLedMode().name());
    }
}