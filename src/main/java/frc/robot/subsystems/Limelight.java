package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {

    /**
     * Limelight's camera states
     */
    public enum CAM {
        VISION,
        DRIVER
    }

    /**
     * Limelight's LED states
     */
    public enum LED {
        PIPELINE,
        OFF,
        BLINK,
        ON
    }

    private NetworkTable limelight;

    /**
     * Initialize the Limelight file
     */
    public Limelight() {
        limelight = NetworkTableInstance.getDefault().getTable("limelight");
    }

    /**
     * @param entry The entry's ID
     * @return The network table entry
     */
    public NetworkTableEntry getEntry(String entry) {
        return limelight.getEntry(entry);
    }

    /**
     * @param entry The entry's ID
     * @return The double stored in the entry
     */
    public double getDouble(String entry) {
        return getEntry(entry).getDouble(0.0);
    }

    /**
     * @param entry The entry's ID
     * @return The boolean stored in the entry
     */
    public boolean getBoolean(String entry) {
        return getDouble(entry) == 1;
    }

    /**
     * @param entry The entry's ID
     * @return The integer stored in the entry
     */
    public int getInt(String entry) {
        return (int) getDouble(entry);
    }

    /**
     * @return Whether or not the limelight has any valid targets
     */
    public boolean hasTarget() {
        return getBoolean("tv");
    }

    /**
     * @return Horizontal Offset From Crosshair To Target (-27 to 27 degrees)
     */
    public double x() {
        return getDouble("tx");
    }

    /**
     * @return Vertical Offset From Crosshair To Target (-20.5 to 20.5 degrees)
     */
    public double y() {
        return getDouble("ty");
    }

    /**
     * @return Target Area (0% of image to 100% of image)
     */
    public double targetArea() {
        return getDouble("ta");
    }

    /**
     * @param entryName The entry's ID
     * @param entryValue Value that the entry will be set to
     */
    public void setEntry(String entryName, int entryValue) {
        getEntry(entryName).setNumber(entryValue);
    }

    /**
     * @param state what to set the limelight LED mode to
     */
    public void setLED(LED state) {
        setEntry("ledMode", state.ordinal());
    }

    /**
     * @return Get the state of the LEDs
     */
    public LED getLED() {
        return LED.values()[getInt("ledMode")];
    }

    /**
     * @param state what to set the limelight camera mode to
     */
    public void setCAM(CAM state) {
        setEntry("camMode", state.ordinal());
    }

    /**
     * @return the state of the Limelight camera
     */
    public CAM getCAM() {
        return CAM.values()[getInt("ledMode")];
    }
}