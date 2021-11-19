package frc.robot.commands;

import frc.robot.lib.RioLogger;
import edu.wpi.first.wpilibj.command.Command;

public class PneumaticsTest extends Command
{
    private Solenoid testSolenoid;
    private Compressor testCompressor;

    private void DisplayInfo()
    {
        RioLogger.log()
    }
    public PneumaticsTest()
    {
        super();
        testSolenoid = new Solenoid(solenoidPort);
        testCompressor = new Compressor(compressorPort);
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