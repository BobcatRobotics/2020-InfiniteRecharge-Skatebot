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

    /**
     * Limelight's streaming states
     */
    public enum STREAM {
        STANDARD,
        MAIN,
        SECONDARY
    }

    /**
     * Limelight's snapshot states
     */
    public enum SNAPSHOT {
        OFF,
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
    public boolean v() {
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
     * @return Skew or rotation
     */
    public double s() {
        return getDouble("ts");
    }

    /**
     * @return The pipeline’s latency contribution (ms)
     */
    public double l() {
        return getDouble("tl");
    }

    /**
     * @return Target Area
     */
    public double a() {
        return getDouble("ta");
    }

    /**
     * @return Side length of shortest side of the fitted bounding box
     */
    public int tshort() {
        return getInt("tshort");
    }

    /**
     * @return Side length of longest side of the fitted bounding box
     */
    public int tlong() {
        return getInt("tlong");
    }

    /**
     * @return Horizontal side length of the rough bounding box
     */
    public int thor() {
        return getInt("thor");
    }

    /**
     * @return Vertical side length of the rough bounding box
     */
    public int tvert() {
        return getInt("tvert");
    }

    /**
     * @return True active pipeline index of the camera
     */
    public int getPipe() {
        return getInt("getpipe");
    }

    /**
     * @param entryName The entry's ID
     * @param entryValue Value that the entry will be set to
     */
    public void setEntry(String entryName, int entryValue) {
        getEntry(entryName).setNumber(entryValue);
    }

    /**
     * @param state What to set the Limelight SNAPSHOT mode to
     */
    public void setSnapshot(SNAPSHOT state) {
        setSnapshot(state.ordinal());
    }

    /**
     * @param state What to set the Limelight SNAPSHOT mode to
     */
    public void setSnapshot(int state) {
        setEntry("snapshot", state);
    }

    /**
     * @return The state of the SNAPSHOT
     */
    public SNAPSHOT getSnapshot() {
        return SNAPSHOT.values()[getInt("snapshot")];
    }

    /**
     * @param state What to set the Limelight STREAM mode to
     */
    public void setStream(STREAM state) {
        setStream(state.ordinal());
    }

    /**
     * @param state What to set the Limelight STREAM mode to
     */
    public void setStream(int state) {
        setEntry("stream", state);
    }

    /**
     * @return The state of the STREAM
     */
    public STREAM getStream() {
        return STREAM.values()[getInt("stream")];
    }

    /**
     * @param state What to set the Limelight LED mode to
     */
    public void setLED(LED state) {
        setLED(state.ordinal());
    }

    /**
     * @param state What to set the Limelight LED mode to
     */
    public void setLED(int state) {
        setEntry("ledMode", state);
    }

    /**
     * @return The state of the LEDs
     */
    public LED getLED() {
        return LED.values()[getInt("ledMode")];
    }

    /**
     * @param state What to set the Limelight camera mode to
     */
    public void setCAM(CAM state) {
        setCAM(state.ordinal());
    }

    /**
     * @param state What to set the Limelight camera mode to
     */
    public void setCAM(int state) {
        setEntry("camMode", state);
    }

    /**
     * @return The state of the Limelight camera
     */
    public CAM getCAM() {
        return CAM.values()[getInt("ledMode")];
    }
}