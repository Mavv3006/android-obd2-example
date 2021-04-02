package de.deuschle.androidodb2example.Activities.Session;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.deuschle.androidodb2example.Database.AmbientTemperatureDao;
import de.deuschle.androidodb2example.Database.AmbientTemperatureEntity;
import de.deuschle.androidodb2example.Database.MyDatabase;
import de.deuschle.androidodb2example.Database.RPMDao;
import de.deuschle.androidodb2example.Database.RPMEntity;
import de.deuschle.androidodb2example.Database.SessionDao;
import de.deuschle.androidodb2example.Database.SessionEntity;
import de.deuschle.androidodb2example.Database.VehicleSpeedDao;
import de.deuschle.androidodb2example.Database.VehicleSpeedEntity;
import de.deuschle.androidodb2example.ObdApplication;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Session.Session;
import de.deuschle.androidodb2example.Session.SessionData;
import de.deuschle.androidodb2example.Session.StreamingMetadata;
import de.deuschle.androidodb2example.Session.StreamingSession;
import de.deuschle.androidodb2example.Util.SupportedCommands;

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
        ObdApplication application = (ObdApplication) getApplication();
        MyDatabase db = application.getDatabase();

        try {
            Session session = new RetrievingTask(db).execute(sessionId).get();
            displaySessionData(session);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void displaySessionData(Session session) {
        Map<String, SessionData> sessionValues = session.getValues();
        String engineSpeedText = String.valueOf(sessionValues.get(SupportedCommands.ENGINE_RPM).value);
        String vehicleSpeedText = String.valueOf(sessionValues.get(SupportedCommands.SPEED).value);
        String temperatureText = String.valueOf(sessionValues.get(SupportedCommands.AMBIENT_AIR_TEMP).value);
        dateTextView.setText(session.getMetadata().getDate().toString());
        rpmTextView.setText(engineSpeedText);
        vehicleSpeedTextView.setText(vehicleSpeedText);
        ambientTemperatureTextView.setText(temperatureText);
    }

    private static class RetrievingTask extends AsyncTask<Integer, Void, Session> {
        private final MyDatabase db;

        private RetrievingTask(MyDatabase db) {
            this.db = db;
        }

        @Override
        protected Session doInBackground(Integer... integers) {
            // only accept one integer representing the sessionId to query
            if (integers.length > 1) return null;

            // setup work
            SessionDao sessionDao = db.getSessionDao();
            AmbientTemperatureDao ambientTemperatureDao = db.getAmbientTemperatureDao();
            RPMDao rpmDao = db.getRPMDao();
            VehicleSpeedDao vehicleSpeedDao = db.getVehicleSpeedDao();
            int sessionId = integers[0];

            // querying the data
            SessionEntity sessionEntity = sessionDao.getSessionById(sessionId);
            RPMEntity rpmEntity = rpmDao.getRPMById(sessionId);
            VehicleSpeedEntity vehicleSpeedEntity = vehicleSpeedDao.getVehicleSpeedById(sessionId);
            AmbientTemperatureEntity ambientTemperatureEntity = ambientTemperatureDao.getAmbientTemperatureById(sessionId);

            // returning the data
            StreamingSession session = new StreamingSession();
            Map<String, SessionData> values = new HashMap<>();
            values.put(SupportedCommands.ENGINE_RPM, SessionData.fromDbEntity(rpmEntity));
            values.put(SupportedCommands.SPEED, SessionData.fromDbEntity(vehicleSpeedEntity));
            values.put(SupportedCommands.AMBIENT_AIR_TEMP, SessionData.fromDbEntity(ambientTemperatureEntity));
            session.setValues(values);
            session.setMetadata(new StreamingMetadata(sessionEntity.date));

            return session;
        }
    }
}