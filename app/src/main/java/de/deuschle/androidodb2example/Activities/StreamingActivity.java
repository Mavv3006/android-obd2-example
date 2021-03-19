package de.deuschle.androidodb2example.Activities;

import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import de.deuschle.androidodb2example.LogTags.LogTags;
import de.deuschle.androidodb2example.R;

public abstract class StreamingActivity extends CommandActivity {
    static final String STREAMING = "streaming";
    static final String NOT_STREAMING = "not streaming";
    Button startStreamingButton;
    Button stopStreamingButton;
    Toolbar toolbar;
    ViewStub viewStub;
    boolean isStreaming = false;

    protected void initLayout() {
        setContentView(R.layout.activity_streaming);
        viewStub = findViewById(R.id.streaming_view_stub);
        toolbar = findViewById(R.id.streaming_toolbar);
        startStreamingButton = findViewById(R.id.streaming_button_start);
        stopStreamingButton = findViewById(R.id.streaming_button_stop);
    }

    @Override
    void setup() {
        super.setup();
        startStreamingButton.setOnClickListener(this::onStartStreamingButtonClick);
        stopStreamingButton.setOnClickListener(this::onStopStreamingButtonClick);
    }

    public void onStopStreamingButtonClick(View view) {
        toggleStreamingButton();
        Log.i(LogTags.STREAMING, "Stopped Streaming");
        application.getCommandQueue().clear();
    }

    public void onStartStreamingButtonClick(View view) {
        toggleStreamingButton();
        addStreamingCommand();
    }

    protected void toggleStreamingButton() {
        startStreamingButton.setEnabled(!startStreamingButton.isEnabled());
        stopStreamingButton.setEnabled(!stopStreamingButton.isEnabled());
        isStreaming = !isStreaming;
        toolbar.setSubtitle(isStreaming ? STREAMING : NOT_STREAMING);
    }

    protected abstract void addStreamingCommand();
}