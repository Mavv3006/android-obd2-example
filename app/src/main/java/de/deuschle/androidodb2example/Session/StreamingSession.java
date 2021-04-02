package de.deuschle.androidodb2example.Session;

import android.util.Log;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import de.deuschle.obd.commands.ObdCommand;

public class StreamingSession implements Session {
    private static final String TAG = StreamingSession.class.getSimpleName();
    private final StreamingMetadata metadata = new StreamingMetadata();
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

    public void addValue(ObdCommand command) {
        if (isStoped) return;

        String commandPID = command.getCommandPID();
        Log.d(TAG, "commandPID: " + commandPID);

        if (!values.containsKey(commandPID)) {
            values.put(commandPID, new SessionData());
        }

        SessionData currentSessionData = values.get(commandPID);
        String commandStringValue = command.getCalculatedResult();
        Log.d(TAG, "commandStringValue = " + commandStringValue);
        int lastIndex = commandStringValue.length() - command.getResultUnit().length();
        Log.d(TAG, "lastIndex = " + lastIndex);
        try {
            String stringValue = commandStringValue.substring(0, lastIndex);
            Log.d(TAG, "value to add: " + stringValue);
            double commandValue = Double.parseDouble(stringValue);
            SessionData nextValue = calcNextValue(currentSessionData, commandValue);
            values.put(commandPID, nextValue);
            Log.i(TAG, command.getName() + " [" + commandPID + "] added (" + stringValue + "): " + nextValue.toString());
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, e.getMessage() == null ? "null" : e.getMessage());
            Log.e(TAG, String.valueOf(e.getCause()));
        }
    }

    protected SessionData calcNextValue(SessionData sessionData, double commandValue) {
        sessionData.value = sessionData.value + (commandValue - sessionData.value) / (++sessionData.n);
        return sessionData;
    }

    public void stop() {
        this.isStoped = true;
    }

    public void start() {
        this.metadata.setDate(LocalDateTime.now());
    }
}
