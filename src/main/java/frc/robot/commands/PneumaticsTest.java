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
        System.out.println("Solenoid status: " + testSolenoid.get());
        System.out.println("Compressor status: " + testCompressor.enabled());
        System.out.println("Is compressor pressure low? :" + (testCompressor.getPressureSwitchValue() ? "yes!" : "no"));
        System.out.println("Compressor Current value: " + testCompressor.getCompressorCurrent());
    }
    public PneumaticsTest()
    {
        super();
        testSolenoid = new Solenoid(RobotMap.solenoidPort);
        testCompressor = new Compressor();
        compressorButton = new JoystickButton(OI.gamePad, RobotMap.compressorButton);
        solenoidButton = new JoystickButton(OI.gamePad, RobotMap.solenoidButton);
    }

    @Override
    public void initialize()
    {
        System.out.println("Pneumatics Test Command Initialized :DDDDDDDDDDDDDDDDDD");
        testSolenoid.set(true);
        testSolenoid.set(false);
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
        System.out.println("Pneumatics Testing Over");
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override 
    protected void interrupted(){
        System.out.println("Something fucking interrupted the Pneumatics Testing Command");
    }
}