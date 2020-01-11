package frc.robot.lib;

import frc.robot.OI;

public class LimelightLogger {
    public double ta = 0.0;
    public double tx = 0.0;
    public double ta0 = 0.0;
    public double ta1 = 0.0;
    public double ts0 = 0.0;
    public double ts1 = 0.0;
    public double drvCmd = 0.0;
    public double strCmd = 0.0;
    public double leftPwr = 0.0;
    public double rightPwr = 0.0;

    public void logCurrent() {
        tx = OI.limelight.x();
        ta = OI.limelight.targetArea();
        ta0 = OI.limelight.targetArea(0);
        ta1 = OI.limelight.targetArea(1);
        ts0 = OI.limelight.targetSkew(0);
        ts1 = OI.limelight.targetSkew(1);
    }

    public String logLine() {
        return String.format("%6.4f %6.4f %6.4f %6.4f %6.4f %6.4f %6.4f %6.4f %6.4f %6.4f", ta, tx, ta0, ta1, ts0, ts1,
                drvCmd, strCmd, leftPwr, rightPwr);
    }

    public String logHeader() {
        return "ta tx ta0 ta1 ts0 ts1 drvCmd strCmd leftPwr rightPwr";
    }

    public String logTrailer() {
        return "===========================================================";
    }

}