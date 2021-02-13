package de.deuschle.androidodb2example.Activities;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import br.ufrn.imd.obd.commands.ObdCommand;
import br.ufrn.imd.obd.exceptions.NonNumericResponseException;
import de.deuschle.androidodb2example.BluetoothLeService;
import de.deuschle.androidodb2example.Commands.OdometerCommand;
import de.deuschle.androidodb2example.LogTags.LogTags;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Streams.BleInputStream;
import de.deuschle.androidodb2example.Streams.BleOutputStream;
import de.deuschle.androidodb2example.Streams.MyInputStream;
import de.deuschle.androidodb2example.Streams.MyOutputStream;

public class OdometerActivity extends AppCompatActivity {
    private static final String TAG = OdometerCommand.class.getSimpleName();
    private final MyInputStream bleInputStream = new BleInputStream();
    private final MyOutputStream bleOutputStream = new BleOutputStream();
    private BluetoothLeService bluetoothLeService;
    private TextView valueTextView;
    private ObdCommand command;
    private SharedPreferences sharedPreferences;

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

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!bluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

            Log.i(TAG, "mBluetoothLeService is okay");
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odometer);
        Toolbar toolbar = findViewById(R.id.odometer_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // init data
        this.valueTextView = findViewById(R.id.odometer_text_view_value);
        this.command = new OdometerCommand();

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        Log.d(TAG, "Try to bindService = " + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_file_key), Context.MODE_PRIVATE);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }

    public void readData(View view) {
        if (bluetoothLeService.getSupportedGattServices() == null) {
            Log.e(TAG, "Bluetooth device not connected");
            return;
        }
        try {
            Log.d(TAG, "Trying to run command: [" + this.command.getCommandPID() + "]");
            this.command.run(bleInputStream, bleOutputStream);
            this.valueTextView.setText(this.command.getFormattedResult());
        } catch (IOException | InterruptedException | NonNumericResponseException e) {
            Log.e(TAG, "Command failed with: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
    }
}