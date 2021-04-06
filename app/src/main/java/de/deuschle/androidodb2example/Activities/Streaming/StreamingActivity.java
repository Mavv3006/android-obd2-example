package de.deuschle.androidodb2example.Activities.Streaming;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import de.deuschle.androidodb2example.Activities.CommandActivity;
import de.deuschle.androidodb2example.Database.SaveSession;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase;
import de.deuschle.androidodb2example.LogTags.LogTags;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Session.StreamingSession;
import de.deuschle.obd.commands.ObdCommand;

public abstract class StreamingActivity extends CommandActivity {
    static final String STREAMING = "streaming";
    static final String NOT_STREAMING = "not streaming";
    public static final String TAG = StreamingActivity.class.getSimpleName();

    StreamingSession session;
    Button startStreamingButton;
    Button stopStreamingButton;
    Toolbar toolbar;
    boolean isStreaming = false;

    protected void initLayout() {
        setContentView(R.layout.activity_streaming);
        toolbar = findViewById(R.id.streaming_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        startStreamingButton = findViewById(R.id.streaming_button_start);
        stopStreamingButton = findViewById(R.id.streaming_button_stop);
    }

    @Override
    protected void setup() {
        initLayout();
        session = new StreamingSession();
        toolbar.setSubtitle(NOT_STREAMING);
        startStreamingButton.setOnClickListener(this::onStartStreamingButtonClick);
        stopStreamingButton.setOnClickListener(this::onStopStreamingButtonClick);
        super.setup();
    }

    public void onStopStreamingButtonClick(View view) {
        stopStreaming();
    }

    public void onStartStreamingButtonClick(View view) {
        session.start();
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
            session.addValue(activeCommand);
        } catch (NumberFormatException e) {
            Log.e(TAG, "NumberFormatException");
            e.printStackTrace();
        }
        addStreamingCommand(activeCommand);
    }

    protected void stopStreaming() {
        session.stop();
        toggleStreamingButton();
        Log.i(LogTags.STREAMING, "Stopped Streaming");
        application.getCommandQueue().clear();
        if (!session.needsToBeSaved()) return;

        StreamingDataDatabase db = Room.databaseBuilder(getApplicationContext(), StreamingDataDatabase.class, getString(R.string.database_name)).build();
        SaveSession saveSession = new SaveSession(session, db, this);
        saveSession.save();
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