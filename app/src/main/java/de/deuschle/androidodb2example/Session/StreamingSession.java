package de.deuschle.androidodb2example.Session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.deuschle.obd.commands.ObdCommand;

public class StreamingSession implements Session {
    private final Metadata metadata = new StreamingMetadata();
    private final Map<String, SessionData> values = new HashMap<>();
    private boolean isStoped = false;

    @Override
    public Metadata getMetadata() {
        return this.metadata;
    }

    @Override
    public Map<String, SessionData> getValues() {
        return this.values;
    }

    @Override
    public void addValue(ObdCommand command) {
        if (isStoped) return;

        String commandPID = command.getCommandPID();

        if (!values.containsKey(commandPID)) {
            values.put(commandPID, new SessionData());
        }

        SessionData currentSessionData = values.get(commandPID);
        String commandStringValue = command.getCalculatedResult();
        int endIndex = commandStringValue.length() - command.getResultUnit().length();
        double commandValue = Double.parseDouble(commandStringValue.substring(0, endIndex));
        SessionData nextValue = calcNextValue(currentSessionData, commandValue);
        values.put(commandPID, nextValue);
    }

    protected SessionData calcNextValue(SessionData sessionData, double commandValue) {
        sessionData.value = sessionData.value + (commandValue - sessionData.value) / (++sessionData.n);
        return sessionData;
    }

    @Override
    public void setUsedCommands(List<ObdCommand> commands) {
        this.metadata.setUsedCommands(commands);
    }

    @Override
    public void stop() {
        this.isStoped = true;
        this.metadata.calcDrivingTime(LocalTime.now());
    }

    @Override
    public void start() {
        this.metadata.setStartingTime(LocalTime.now());
        this.metadata.setDate(LocalDate.now());
    }
}
