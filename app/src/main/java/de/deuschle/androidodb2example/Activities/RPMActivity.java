package de.deuschle.androidodb2example.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import de.deuschle.androidodb2example.R;
import de.deuschle.obd.commands.engine.RPMCommand;

public class RPMActivity extends CommandActivity {
    private static final String TAG = RPMActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_r_p_m);
        Toolbar toolbar = findViewById(R.id.engine_rpm_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.rpm_toolbar_title);
        }

        this.valueTextView = findViewById(R.id.result);
        setup();
    }

    public void readData(View view) {
        addCommand(new RPMCommand());
    }
}