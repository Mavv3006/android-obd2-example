package de.deuschle.androidodb2example.Commands;

import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.enums.AvailableCommand;

public class StopStreamingCommand extends ObdCommand {
    public StopStreamingCommand(ObdCommand other) {
        super(other);
    }

    public StopStreamingCommand() {
        super(AvailableCommand.CustomCommand.rawCommand("STOP"));
    }

    @Override
    protected void performCalculations() {

    }

    @Override
    public String getFormattedResult() {
        return null;
    }

    @Override
    public String getCalculatedResult() {
        return null;
    }
}
