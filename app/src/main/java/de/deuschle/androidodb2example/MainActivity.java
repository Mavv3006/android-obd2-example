package de.deuschle.androidodb2example;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.deuschle.androidodb2example.bluetooth.BluetoothConnection;
import de.deuschle.androidodb2example.bluetooth.BluetoothConnectionFactory;

public class MainActivity extends AppCompatActivity {

    BluetoothConnection connection;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.linearLayout = findViewById(R.id.list_layout);
        this.connection = BluetoothConnectionFactory.getConnection();
        this.showBondedBluetoothDeviceNames();
    }


    private void showBondedBluetoothDeviceNames() {
        linearLayout.removeAllViewsInLayout();
        if (this.connection.getBoundedDevices().size() > 0) {
            for (BluetoothDevice device : this.connection.getBoundedDevices()) {
                addTextView(device.getName() + "\n" + device.getAddress());
            }
        } else {
            addTextView("No devices bonded");
        }
    }

    private void addTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        linearLayout.addView(textView);
    }
}