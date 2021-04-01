package de.deuschle.androidodb2example.Commands;

import de.deuschle.obd.commands.protocol.ObdProtocolCommand;
import de.deuschle.obd.enums.AvailableCommand;

public class HeadersOnCommand extends ObdProtocolCommand {

    public HeadersOnCommand() {
        super(AvailableCommand.CustomCommand.rawCommand("AT H1"));
    }

    @Override
    public String getFormattedResult() {
        return getResult();
    }
}
