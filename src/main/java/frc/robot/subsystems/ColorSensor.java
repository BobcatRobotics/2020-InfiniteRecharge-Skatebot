package frc.robot.subsystems;

import com.revrobotics.CIEColor;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.networktables.NetworkTable;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.lib.RioLogger;
import frc.robot.util.Vector3d;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
public class ColorSensor extends Subsystem{

    private ColorSensorV3 colorSensor;

    NetworkTable table = NetworkTableInstance.getDefault().getTable("Color");
	private ShuffleboardTab tab = Shuffleboard.getTab("ColorSensro3");
	private NetworkTableEntry color = tab.add("Color", "").getEntry();
    public ColorSensor(){
        colorSensor = new ColorSensorV3(RobotMap.colorSensorPort);
    }

    final Vector3d[] CIE_TO_RGB_MATRIX = {
        new Vector3d(3.2404542, -.9692660, .0556434),
        new Vector3d(-1.5371385, 1.8760108, -.2040259),
        new Vector3d(-.4985314, .0415560, 1.0572252)
    };

    public void printColor(){
        //RioLogger.log(colorSensor.getColor().toString());

        CIEColor color = colorSensor.getCIEColor();
        Vector3d cieVals = new Vector3d(color.getX(), color.getY(), color.getZ());
        Vector3d rgbVals = Vector3d.matrixVectorMultiply(CIE_TO_RGB_MATRIX, cieVals);
        
        System.out.println(rgbVals);
        table.getEntry("color").setString(colorSensor.getColor().toString());
    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub

    }

}