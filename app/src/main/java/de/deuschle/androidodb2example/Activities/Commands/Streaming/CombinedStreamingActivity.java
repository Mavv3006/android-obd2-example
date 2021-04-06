package de.deuschle.androidodb2example.Activities.Commands.Streaming;

import android.os.Bundle;

import androidx.annotation.Nullable;

import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.commands.engine.RPMCommand;
import de.deuschle.obd.commands.temperature.AmbientAirTemperatureCommand;

public class CombinedStreamingActivity extends StreamingActivity {
    private static final String TOOLBAR_TITLE = "Combined Streaming";

    @Override
    protected void addStreamingCommand() {
        ObdCommand[] commands = {new RPMCommand(), new AmbientAirTemperatureCommand()};
        addCommand(commands);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
        toolbar.setTitle(TOOLBAR_TITLE);
    }
}
