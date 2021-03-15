package de.deuschle.androidodb2example.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import de.deuschle.androidodb2example.R;
import de.deuschle.obd.commands.temperature.AmbientAirTemperatureCommand;

public class AmbientTemperatureActivity extends CommandActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambient_temperature);
        Toolbar toolbar = findViewById(R.id.ambient_temperature_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        valueTextView = findViewById(R.id.result);

        setup();
    }

    public void readData(View view) {
        addCommand(new AmbientAirTemperatureCommand());
    }
}