package de.deuschle.androidodb2example.Activities.Commands.Streaming;

import android.os.Bundle;

import de.deuschle.obd.commands.engine.RPMCommand;

public class RPMStreamingActivity extends StreamingActivity {
    private static final String TOOLBAR_TITLE = "Engine RPM Streaming";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
        toolbar.setTitle(TOOLBAR_TITLE);
    }

    @Override
    protected void addStreamingCommand() {
        addCommand(new RPMCommand());
    }
}