package de.deuschle.androidodb2example.ViewModel;

import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.deuschle.obd.commands.ObdCommand;

public class StreamingActivityViewModel {
    protected final Map<String, ObdCommand> supportedCommands = new HashMap<>();
    protected final List<ObdCommand> commandList = new ArrayList<>();

    public void addStreamingCommand(SwitchCompat switchCompat, String key) {
        if (switchCompat.isChecked() && supportedCommands.containsKey(key)) {
            commandList.add(supportedCommands.get(key));
        }
    }

    public void putCommand(String key, ObdCommand command) {
        supportedCommands.put(key, command);
    }

    public ObdCommand[] getCommands() {
        return commandList.toArray(new ObdCommand[0]);
    }

}
