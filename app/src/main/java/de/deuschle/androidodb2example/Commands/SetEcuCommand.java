package de.deuschle.androidodb2example.Commands;

import de.deuschle.obd.commands.protocol.ObdProtocolCommand;
import de.deuschle.obd.enums.AvailableCommand;

public class SetEcuCommand extends ObdProtocolCommand {

    public SetEcuCommand(String header) {
        super(AvailableCommand.CustomCommand.rawCommand("AT SH " + header));
    }

    @Override
    public String getFormattedResult() {
        return getResult();
    }
}
