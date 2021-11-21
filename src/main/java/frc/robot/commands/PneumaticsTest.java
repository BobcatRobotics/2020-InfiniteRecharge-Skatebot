package frc.robot.commands;

import frc.robot.RobotMap;
import frc.robot.OI;
import frc.robot.lib.RioLogger;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class PneumaticsTest extends Command
{
    //Use OI.gamepad for any gamepad references :DDDD

    private Solenoid testSolenoid;
    private Compressor testCompressor;
    private JoystickButton compressorButton;
    private JoystickButton solenoidButton;

    private void DisplayInfo()
    {
        RioLogger.log("Solenoid status: " + testSolenoid.get());
        RioLogger.log("Compressor status: " + testCompressor.enabled());
        RioLogger.log("Is compressor pressure low? :" + (testCompressor.getPressureSwitchValue() ? "yes!" : "no"));
        RioLogger.log("Compressor Current value: " + testCompressor.getCompressorCurrent());
    }
    public PneumaticsTest()
    {
        super();
        testSolenoid = new Solenoid(RobotMap.solenoidPort);
        testCompressor = new Compressor(RobotMap.compressorPort);
        compressorButton = new JoystickButton(OI.gamePad, RobotMap.compressorButton);
        solenoidButton = new JoystickButton(OI.gamePad, RobotMap.solenoidButton);
    }

    @Override
    public void initialize()
    {
        RioLogger.log("Pneumatics Test Command Initialized :DDDDDDDDDDDDDDDDDD");        
    }

    @Override
    public void execute()
    {
        if(compressorButton.get())
        {
            testCompressor.start();
        }
        else
            testCompressor.stop();
        testSolenoid.set(solenoidButton.get());
        DisplayInfo();
    }

    @Override
    public void end()
    {
        RioLogger.errorLog("Pneumatics Testing Over");
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override 
    protected void interrupted(){
        RioLogger.errorLog("Something fucking interrupted the Pneumatics Testing Command");
    }
}