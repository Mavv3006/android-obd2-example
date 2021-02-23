package de.deuschle.androidodb2example.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import de.deuschle.androidodb2example.BluetoothLeService;
import de.deuschle.androidodb2example.LogTags.LogTags;
import de.deuschle.androidodb2example.R;
import de.deuschle.obd.commands.engine.ThrottlePositionCommand;
import de.deuschle.obd.exceptions.NonNumericResponseException;

public class EngineRPM extends CommandActivity {
    private static final String TAG = EngineRPM.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_r_p_m);

        Toolbar toolbar = findViewById(R.id.engine_rpm_toolbar);
        String title = "Dies ist ein Test";
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.valueTextView = findViewById(R.id.result);
        Button getDataButton = findViewById(R.id.button);
        this.command = new ThrottlePositionCommand();

        getDataButton.setOnClickListener(view -> {
            try {
                this.bleInputStream.setData("01 11\n41 11 2F\r\n>");
                Log.d(TAG, "Trying to run command " + this.command.getName());
                this.command.run(bleInputStream, bleOutputStream);
                this.valueTextView.setText(this.command.getFormattedResult());
            } catch (IOException | InterruptedException | NonNumericResponseException e) {
                Log.e(TAG, "Command failed with: " + e.getMessage());
                e.printStackTrace();
                String error = "Error";
                this.valueTextView.setText(error);
            }
        });

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        Log.d(TAG, "Try to bindService = " + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));

        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_file_key), Context.MODE_PRIVATE);
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

    }
}