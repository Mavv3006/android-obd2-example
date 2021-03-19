package de.deuschle.androidodb2example.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.deuschle.androidodb2example.R;
import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.commands.engine.RPMCommand;

public class RPMStreamingActivity extends StreamingActivity {
    private static final String TAG = RPMStreamingActivity.class.getSimpleName();
    private static final String TOOLBAR_TITLE = "Engine RPM Streaming";
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocalDateTime now;
    LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        contentLayout = findViewById(R.id.streaming_rpm_content_list);
        viewStub.setLayoutResource(R.layout.activity_rpm_streaming);
        toolbar.setTitle(TOOLBAR_TITLE);
    }

    @Override
    protected void handleProcessedData(ObdCommand activeCommand, byte[] processedData) {
        Log.i(TAG, activeCommand.getCommandPID());

        addTextView(activeCommand.getFormattedResult());
        addStreamingCommand();
    }

    @Override
    protected void addStreamingCommand() {
        addCommand(new RPMCommand());
    }

    private void addTextView(String text) {
        TextView textView = new TextView(this);
        now = LocalDateTime.now();
        String combinedText = dateTimeFormatter.format(now) + "  " + text;
        textView.setText(combinedText);
        contentLayout.addView(textView);
    }
}