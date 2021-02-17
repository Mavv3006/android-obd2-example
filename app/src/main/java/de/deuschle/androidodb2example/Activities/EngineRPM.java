package de.deuschle.androidodb2example.Activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import de.deuschle.androidodb2example.BluetoothLeService;
import de.deuschle.androidodb2example.LogTags.LogTags;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Streams.BleInputStream;
import de.deuschle.androidodb2example.Streams.BleOutputStream;
import de.deuschle.androidodb2example.Streams.MyInputStream;
import de.deuschle.androidodb2example.Streams.MyOutputStream;
import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.commands.engine.ThrottlePositionCommand;
import de.deuschle.obd.exceptions.NonNumericResponseException;

public class EngineRPM extends AppCompatActivity {
    private static final String TAG = EngineRPM.class.getSimpleName();
    private final MyInputStream bleInputStream = new BleInputStream();
    private final MyOutputStream bleOutputStream = new BleOutputStream();
    private BluetoothLeService bluetoothLeService;
    private SharedPreferences sharedPreferences;

    private TextView resultTextView;
    private ObdCommand command;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!bluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

            Log.e(TAG, "mBluetoothLeService is okay");
            bleOutputStream.setBleService(bluetoothLeService);

            String deviceAddress = sharedPreferences.getString(getString(R.string.shared_preferences_device_address), null);
            Log.d(LogTags.SHARED_PREFERENCES, "Device Address read: " + deviceAddress);
            if (deviceAddress != null) {
                // Automatically connects to the device upon successful start-up initialization.
                bluetoothLeService.connect(deviceAddress);
            } else {
                Log.e(LogTags.SHARED_PREFERENCES, "unable to connect to device, device address not saved");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bluetoothLeService = null;
        }
    };


    // Handles various events fired by the Service.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.e(TAG, "RECV DATA");
                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                if (data != null) {
                    Log.i(LogTags.OBD2, "Data: " + data);
                    bleInputStream.setData(data);
                }
            }
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

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

        this.resultTextView = findViewById(R.id.result);
        Button getDataButton = findViewById(R.id.button);
        this.command = new ThrottlePositionCommand();

        getDataButton.setOnClickListener(view -> {
            try {
                this.bleInputStream.setData("01 11\n41 11 2F\r\n>");
                Log.d(TAG, "Trying to run command " + this.command.getName());
                this.command.run(bleInputStream, bleOutputStream);
                this.resultTextView.setText(this.command.getFormattedResult());
            } catch (IOException | InterruptedException | NonNumericResponseException e) {
                Log.e(TAG, "Command failed with: " + e.getMessage());
                e.printStackTrace();
                String error = "Error";
                this.resultTextView.setText(error);
            }
        });

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        Log.d(TAG, "Try to bindService = " + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));

        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_file_key), Context.MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
    }
}