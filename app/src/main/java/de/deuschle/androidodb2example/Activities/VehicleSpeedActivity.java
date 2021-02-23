package de.deuschle.androidodb2example.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import de.deuschle.androidodb2example.LogTags.LogTags;
import de.deuschle.androidodb2example.R;
import de.deuschle.obd.commands.engine.SpeedCommand;
import de.deuschle.obd.exceptions.NonNumericResponseException;

public class VehicleSpeedActivity extends CommandActivity {
    private final String TAG = VehicleSpeedActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_speed);
        Toolbar toolbar = findViewById(R.id.vehicle_speed_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.vehicle_speed_toolbar_title));
        }

        valueTextView = findViewById(R.id.vehicle_speed_text_view_value);
        command = new SpeedCommand();

        setup();
    }

    @Override
    protected void handleData(String data) {
        if (data == null) return;

        Log.i(LogTags.OBD2, "Data: " + data);
        bleInputStream.setData(data);
        if (bleInputStream.isFinished()) {
            try {
                command.readResult();
                valueTextView.setText(command.getFormattedResult());
            } catch (IOException | NonNumericResponseException e) {
                Log.e(TAG, "Error in processing the input data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void handleDisconnect() {
        Log.i(TAG, "disconnected");
    }

    public void readData(View view) {
        try {
            Log.d(TAG, "Trying to run command: [" + command.getCommandPID() + "]");
            command.run(bleInputStream, bleOutputStream);
            Log.d(TAG, "result: " + command.getResult());
            valueTextView.setText(command.getFormattedResult());
        } catch (Exception e) {
            Log.e(TAG, "Command failed with: " + e.getMessage());
            e.printStackTrace();
        }
    }
}