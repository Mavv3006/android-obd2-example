package de.deuschle.androidodb2example.Commands;

import br.ufrn.imd.obd.commands.ObdCommand;
import br.ufrn.imd.obd.enums.AvailableCommand;

public class TransmissionActualGearCommand extends ObdCommand {
    public TransmissionActualGearCommand(TransmissionActualGearCommand other) {
        super(other);
    }

    public TransmissionActualGearCommand() {
        super(AvailableCommand.CustomCommand.rawCommand("01 A4"));
    }

    @Override
    protected void performCalculations() {
        // TODO
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
