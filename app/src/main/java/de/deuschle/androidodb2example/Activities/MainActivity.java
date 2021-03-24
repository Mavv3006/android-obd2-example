package de.deuschle.androidodb2example.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.deuschle.androidodb2example.Activities.Streaming.AmbientTemperatureStreamingActivity;
import de.deuschle.androidodb2example.Activities.Streaming.CombinedStreamingActivity;
import de.deuschle.androidodb2example.Activities.Streaming.RPMStreamingActivity;
import de.deuschle.androidodb2example.ObdApplication;
import de.deuschle.androidodb2example.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObdApplication application = (ObdApplication) (getApplication());
        String deviceAddress = application.getDeviceAdress();
        String deviceName = application.getDeviceName();

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle(deviceName);
        toolbar.setSubtitle(deviceAddress);
        setSupportActionBar(toolbar);
    }

    public void goToEngineRPM(View view) {
        Intent intent = new Intent(this, RPMActivity.class);
        startActivity(intent);
    }

    public void readVehicleSpeed(View view) {
        Intent intent = new Intent(this, VehicleSpeedActivity.class);
        startActivity(intent);
    }

    public void readAmbientTemperature(View view) {
        Intent intent = new Intent(this, AmbientTemperatureActivity.class);
        startActivity(intent);
    }

    public void goToInit(View view) {
        Intent intent = new Intent(this, InitActivity.class);
        startActivity(intent);
    }

    public void goToRpmStreaming(View view) {
        Intent intent = new Intent(this, RPMStreamingActivity.class);
        startActivity(intent);
    }

    public void goToAmbientTemperatureStreaming(View view) {
        Intent intent = new Intent(this, AmbientTemperatureStreamingActivity.class);
        startActivity(intent);
    }

    public void goToCombinedStreaming(View view) {
        startActivity(new Intent(this, CombinedStreamingActivity.class));
    }
}