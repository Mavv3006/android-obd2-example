package de.deuschle.androidodb2example.Commands;

import de.deuschle.obd.commands.protocol.ObdProtocolCommand;
import de.deuschle.obd.enums.AvailableCommand;

public class ProtocolAutoCommand extends ObdProtocolCommand {


    public ProtocolAutoCommand() {
        super(AvailableCommand.CustomCommand.rawCommand("AT SP 0"));
    }

    @Override
    public String getFormattedResult() {
        return getResult();
    }
}
