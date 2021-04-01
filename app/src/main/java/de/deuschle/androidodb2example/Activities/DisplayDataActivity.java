package de.deuschle.androidodb2example.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import de.deuschle.androidodb2example.Database.AmbientTemperatureDao;
import de.deuschle.androidodb2example.Database.AmbientTemperatureEntity;
import de.deuschle.androidodb2example.Database.MyDatabase;
import de.deuschle.androidodb2example.Database.RPMDao;
import de.deuschle.androidodb2example.Database.RPMEntity;
import de.deuschle.androidodb2example.Database.SessionDao;
import de.deuschle.androidodb2example.Database.SessionEntity;
import de.deuschle.androidodb2example.Database.VehicleSpeedDao;
import de.deuschle.androidodb2example.Database.VehicleSpeedEntity;
import de.deuschle.androidodb2example.R;

public class DisplayDataActivity extends AppCompatActivity {

    TextView dateTextView;
    TextView rpmTextView;
    TextView vehicleSpeedTextView;
    TextView ambientTemperatureTextView;

    public static final String extra = "sessionIdExtra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        Toolbar toolbar = findViewById(R.id.displayDataToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dateTextView = findViewById(R.id.sessionDateTextView);
        rpmTextView = findViewById(R.id.RPMTextView);
        vehicleSpeedTextView = findViewById(R.id.VehicleSpeedTextView);
        ambientTemperatureTextView = findViewById(R.id.AmbientTemperatureTextView);

        int sessionId = Integer.parseInt(getIntent().getStringExtra(extra));

        MyDatabase db = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, getString(R.string.database_name)).build();
        SessionDao sessionDao = db.getSessionDao();
        AmbientTemperatureDao ambientTemperatureDao = db.getAmbientTemperatureDao();
        RPMDao rpmDao = db.getRPMDao();
        VehicleSpeedDao vehicleSpeedDao = db.getVehicleSpeedDao();

        SessionEntity sessionEntity = sessionDao.getSessionById(sessionId);
        dateTextView.setText(sessionEntity.date.toString());

        RPMEntity rpmEntity = rpmDao.getRPMById(sessionId);
        rpmTextView.setText(String.valueOf(rpmEntity.value));

        VehicleSpeedEntity vehicleSpeedEntity = vehicleSpeedDao.getVehicleSpeedById(sessionId);
        vehicleSpeedTextView.setText(String.valueOf(vehicleSpeedEntity.value));

        AmbientTemperatureEntity ambientTemperatureEntity = ambientTemperatureDao.getAmbientTemperatureById(sessionId);
        ambientTemperatureTextView.setText(String.valueOf(ambientTemperatureEntity.value));
    }
}