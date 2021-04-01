package de.deuschle.androidodb2example.Activities.Streaming;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import de.deuschle.androidodb2example.Activities.CommandActivity;
import de.deuschle.androidodb2example.LogTags.LogTags;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Session.StreamingSession;
import de.deuschle.obd.commands.ObdCommand;

public abstract class StreamingActivity extends CommandActivity {
    static final String STREAMING = "streaming";
    static final String NOT_STREAMING = "not streaming";

    StreamingSession streamingSessionInterface;
    Button startStreamingButton;
    Button stopStreamingButton;
    Toolbar toolbar;
    boolean isStreaming = false;

    protected void initLayout() {
        setContentView(R.layout.activity_streaming);
        toolbar = findViewById(R.id.streaming_toolbar);
        startStreamingButton = findViewById(R.id.streaming_button_start);
        stopStreamingButton = findViewById(R.id.streaming_button_stop);
    }

    @Override
    protected void setup() {
        initLayout();
        streamingSessionInterface = new StreamingSession();
        toolbar.setSubtitle(NOT_STREAMING);
        startStreamingButton.setOnClickListener(this::onStartStreamingButtonClick);
        stopStreamingButton.setOnClickListener(this::onStopStreamingButtonClick);
        super.setup();
    }

    public void onStopStreamingButtonClick(View view) {
        stopStreaming();
    }

    public void onStartStreamingButtonClick(View view) {
        streamingSessionInterface.start();
        toggleStreamingButton();
        addStreamingCommand();
    }

    protected void toggleStreamingButton() {
        startStreamingButton.setEnabled(!startStreamingButton.isEnabled());
        stopStreamingButton.setEnabled(!stopStreamingButton.isEnabled());
        isStreaming = !isStreaming;
        toolbar.setSubtitle(isStreaming ? STREAMING : NOT_STREAMING);
    }

    @Override
    protected void addCommand(ObdCommand command) {
        if (!isStreaming) return;
        super.addCommand(command);
    }

    @Override
    protected void addCommand(ObdCommand[] commands) {
        if (!isStreaming) return;
        super.addCommand(commands);
    }

    @Override
    protected void handleProcessedData(ObdCommand activeCommand, byte[] processedData) {
        // TODO: remove try-catch when possible
        try {
            streamingSessionInterface.addValue(activeCommand);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        addStreamingCommand(activeCommand);
    }

    protected void stopStreaming() {
        streamingSessionInterface.stop();
        toggleStreamingButton();
        Log.i(LogTags.STREAMING, "Stopped Streaming");
        application.getCommandQueue().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopStreaming();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopStreaming();
    }

    protected abstract void addStreamingCommand();

    protected void addStreamingCommand(ObdCommand obdCommand) {
        addCommand(obdCommand);
    }
}