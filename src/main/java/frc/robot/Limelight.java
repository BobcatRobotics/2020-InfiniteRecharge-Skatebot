package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTablesJNI;

public final class Limelight {
    private NetworkTable limelight;

    public enum ledMode {
        PIPELINE(0),
        OFF(1),
        BLINK(2),
        ON(3);

        private int value;
        private ledMode(int value) {
            this.value = value;
        }
    }

    public enum camMode {
        VISION(0),
        DRIVER(1);

        private int value;
        private camMode(int value) {
            this.value = value;
        }
    }

    public Limelight() {
        limelight = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public ledMode getLedMode() {
        return ledMode.values()[NetworkTablesJNI.getEntry(NetworkTableInstance.getDefault().getHandle(), "ledMode")];
    }

    public NetworkTableEntry getLedModeEntry() {
        return limelight.getEntry("ledMode");
    }

    public void setLedMode(ledMode mode) {
        getLedModeEntry().setNumber(mode.value);
    }

    public camMode getCamMode() {
        return camMode.values()[NetworkTablesJNI.getEntry(NetworkTableInstance.getDefault().getHandle(), "camMode")];
    }

    public NetworkTableEntry getCamModeEntry() {
        return limelight.getEntry("camMode");
    }

    public void setCamMode(camMode mode) {
        getCamModeEntry().setNumber(mode.value);
    }
}