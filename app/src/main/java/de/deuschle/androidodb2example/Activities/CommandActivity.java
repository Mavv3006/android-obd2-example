package de.deuschle.androidodb2example.Activities;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Queue;

import de.deuschle.androidodb2example.Conversion.ProcessRawData;
import de.deuschle.androidodb2example.LogTags.LogTags;
import de.deuschle.androidodb2example.ObdApplication;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Services.BluetoothLeService;
import de.deuschle.androidodb2example.Streams.BleOutputStream;
import de.deuschle.obd.commands.ObdCommand;

abstract public class CommandActivity extends AppCompatActivity {
    private static final String TAG = CommandActivity.class.getSimpleName();
    protected final BleOutputStream bleOutputStream = new BleOutputStream();
    protected BluetoothLeService bluetoothLeService;
    protected TextView valueTextView;
    //    protected SharedPreferences sharedPreferences;
    protected ObdApplication application;
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

//            String deviceAddress = sharedPreferences.getString(getString(R.string.shared_preferences_device_address), null);
            String deviceAddress = application.getDeviceAdress();
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
        application = (ObdApplication) (getApplication());
//        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_file_key), Context.MODE_PRIVATE);
    }

    protected void handleData(String data) {
        if (data == null) return;
        Log.i(LogTags.OBD2, "Data: " + data);

        ObdCommand command = application.getCommandQueue().poll();
        byte[] processedData = ProcessRawData.convert(data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(processedData);
        try {
            assert command != null;
            command.readResult(inputStream);
        } catch (Exception e) {
            handleCommandError(e, command);
        }

        valueTextView.setText(command.getFormattedResult());
        sendCommand();

//        bleInputStream.setData(data);
//        if (bleInputStream.isFinished()) {
//            try {
//                activeCommand.readResult();
//                valueTextView.setText(activeCommand.getFormattedResult());
//            } catch (IOException | NonNumericResponseException e) {
//                Log.e(TAG, "Error in processing the input data: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
    }

    protected void handleDisconnect() {
        Log.i(TAG, "disconnected");
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

    protected void handleCommandError(Exception e, ObdCommand command) {
        Log.e(TAG, "Command " + command.getName() + " failed with: " + e.getMessage());
        e.printStackTrace();
    }

    protected void addCommand(ObdCommand command) {
        application.getCommandQueue().offer(command);
        sendCommand();
    }

    protected void sendCommand() {
        Queue<ObdCommand> queue = application.getCommandQueue();
        if (queue.size() > 0) {
            try {
                ObdCommand command = queue.peek();
                assert command != null;
                command.sendCommand(bleOutputStream);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
