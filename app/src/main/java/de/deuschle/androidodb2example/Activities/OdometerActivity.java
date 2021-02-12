package de.deuschle.androidodb2example.Activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

            Log.e(TAG, "mBluetoothLeService is okay");
            bleOutputStream.setBleService(bluetoothLeService);
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
    }

    public void readData(View view) {
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