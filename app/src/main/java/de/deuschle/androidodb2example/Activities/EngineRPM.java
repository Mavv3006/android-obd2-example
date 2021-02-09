package de.deuschle.androidodb2example.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import br.ufrn.imd.obd.commands.ObdCommand;
import br.ufrn.imd.obd.commands.engine.ThrottlePositionCommand;
import br.ufrn.imd.obd.exceptions.NonNumericResponseException;
import de.deuschle.androidodb2example.BluetoothLeService;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Streams.BleInputStream;
import de.deuschle.androidodb2example.Streams.BleOutputStream;
import de.deuschle.androidodb2example.Streams.MyInputStream;
import de.deuschle.androidodb2example.Streams.MyOutputStream;

public class EngineRPM extends AppCompatActivity {
    private static final String TAG = EngineRPM.class.getSimpleName();
    private final MyInputStream bleInputStream = new BleInputStream();
    private final MyOutputStream bleOutputStream = new BleOutputStream();
    private BluetoothLeService bluetoothLeService;

    private TextView resultTextView;
    private ObdCommand command;

//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
//            if (!bluetoothLeService.initialize()) {
//                Log.e(TAG, "Unable to initialize Bluetooth");
//                finish();
//            }
//
//            Log.e(TAG, "mBluetoothLeService is okay");
//            bleOutputStream.setBleService(bluetoothLeService);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            bluetoothLeService = null;
//        }
//    };


    // Handles various events fired by the Service.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
//    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//
//            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                Log.e(TAG, "RECV DATA");
//                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
//                if (data != null) {
//                    Log.i(LogTags.OBD2, "Data: " + data);
//                    bleInputStream.setData(data);
//                }
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_r_p_m);

        Toolbar toolbar = (Toolbar) findViewById(R.id.engine_rpm_toolbar);
        String subtitle = "Dies ist ein Test";
        toolbar.setTitle(subtitle);
        setSupportActionBar(toolbar);

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

//        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
//        Log.d(TAG, "Try to bindService=" + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(mGattUpdateReceiver);
//        unbindService(mServiceConnection);
//    }
}