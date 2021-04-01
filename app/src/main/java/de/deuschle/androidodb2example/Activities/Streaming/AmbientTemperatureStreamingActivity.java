package de.deuschle.androidodb2example.Activities.Streaming;

import android.os.Bundle;

import de.deuschle.obd.commands.temperature.AmbientAirTemperatureCommand;

public class AmbientTemperatureStreamingActivity extends StreamingActivity {
    private static final String TOOLBAR_TITLE = "Ambient Temperature Streaming";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
        toolbar.setTitle(TOOLBAR_TITLE);
    }

    @Override
    protected void addStreamingCommand() {
        addStreamingCommand(new AmbientAirTemperatureCommand());
    }
}