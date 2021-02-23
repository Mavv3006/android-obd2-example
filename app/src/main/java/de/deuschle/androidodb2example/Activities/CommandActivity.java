package de.deuschle.androidodb2example.Activities;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import de.deuschle.androidodb2example.BluetoothLeService;
import de.deuschle.androidodb2example.LogTags.LogTags;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Streams.BleInputStream;
import de.deuschle.androidodb2example.Streams.BleOutputStream;
import de.deuschle.androidodb2example.Streams.MyInputStream;
import de.deuschle.androidodb2example.Streams.MyOutputStream;
import de.deuschle.obd.commands.ObdCommand;

abstract public class CommandActivity extends AppCompatActivity {
    private static final String TAG = CommandActivity.class.getSimpleName();
    protected final MyInputStream bleInputStream = new BleInputStream();
    protected final MyOutputStream bleOutputStream = new BleOutputStream();
    protected BluetoothLeService bluetoothLeService;
    protected TextView valueTextView;
    protected ObdCommand command;
    protected SharedPreferences sharedPreferences;
    // Handles various events fired by the Service.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                valueTextView.setText(getString(R.string.connected));
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.e(TAG, "RECV DATA");
                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                if (bluetoothLeService.getSupportedGattServices() == null) {
                    Log.e(TAG, "Bluetooth device not supported");
                    return;
                }
                handleData(data);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                handleDisconnect();
            }
        }
    };


    final ServiceConnection mServiceConnection = new ServiceConnection() {
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

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }

    void setup() {
        registerService();
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_file_key), Context.MODE_PRIVATE);
    }

    protected abstract void handleData(String data);

    protected abstract void handleDisconnect();

    protected void setActionBar(int stringId) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(stringId));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mGattUpdateReceiver);
            unbindService(mServiceConnection);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void registerService() {
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        Log.d(TAG, "Try to bindService = " + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }
}
