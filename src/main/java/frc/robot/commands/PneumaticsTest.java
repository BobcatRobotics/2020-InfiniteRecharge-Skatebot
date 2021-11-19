package frc.robot.commands;

import frc.robot.OI;
import frc.robot.lib.RioLogger;
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
        RioLogger.log("Solenoid status: " + solenoid.get());
        RioLogger.log("Compressor status: " + compressor.get());
    }
    public PneumaticsTest()
    {
        super();
        testSolenoid = new Solenoid(solenoidPort);
        testCompressor = new Compressor(compressorPort);
        compressorButton = new JoystickButton(OI.gamePad, RobotMap.compressorButton);
        solenoidButton = new JoystickButton(OI.gamePad, RobotMap.solenoidButton);
    }

    @Override
    public void execute()
    {
        DisplayInfo();
    }

    @Override
    public void end()
    {

    }

}