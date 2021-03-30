package de.deuschle.androidodb2example.Session;

import java.util.List;
import java.util.Map;

import de.deuschle.obd.commands.ObdCommand;

public interface Session {
    Metadata getMetadata();

    Map<String, SessionData> getValues();

    void addValue(ObdCommand command);

    void setUsedCommands(List<ObdCommand> commands);

    void stop();

    void start();
}
