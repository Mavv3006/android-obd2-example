package de.deuschle.androidodb2example.Activities.Session;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.deuschle.androidodb2example.Database.StreamingDataDatabase.AmbientTemperatureDao;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.AmbientTemperatureEntity;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.RPMDao;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.RPMEntity;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.SessionDao;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.SessionEntity;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.StreamingDataDatabase;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.VehicleSpeedDao;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.VehicleSpeedEntity;
import de.deuschle.androidodb2example.ObdApplication;
import de.deuschle.androidodb2example.R;
import de.deuschle.androidodb2example.Session.Session;
import de.deuschle.androidodb2example.Session.SessionData;
import de.deuschle.androidodb2example.Session.StreamingMetadata;
import de.deuschle.androidodb2example.Session.StreamingSession;
import de.deuschle.androidodb2example.Util.SupportedCommands;
import de.deuschle.obd.commands.ObdCommand;
import de.deuschle.obd.commands.engine.RPMCommand;
import de.deuschle.obd.commands.engine.SpeedCommand;
import de.deuschle.obd.commands.temperature.AmbientAirTemperatureCommand;

public class DisplayDataActivity extends AppCompatActivity {
    public static final String extra = "sessionIdExtra";
    private static final String TAG = DisplayDataActivity.class.getSimpleName();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM);
    public static final String BLANK = " ";

    private TextView dateTextView;
    private TextView rpmTextView;
    private TextView vehicleSpeedTextView;
    private TextView idTextView;
    private TextView ambientTemperatureTextView;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        Toolbar toolbar = findViewById(R.id.displayDataToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateTextView = findViewById(R.id.sessionDateTextView);
        rpmTextView = findViewById(R.id.RPMTextView);
        vehicleSpeedTextView = findViewById(R.id.VehicleSpeedTextView);
        idTextView = findViewById(R.id.session_id_text_view);
        ambientTemperatureTextView = findViewById(R.id.AmbientTemperatureTextView);

        int sessionId = Integer.parseInt(getIntent().getStringExtra(extra));
        ObdApplication application = (ObdApplication) getApplication();
        StreamingDataDatabase db = application.getStreamingDataDatabase();

        try {
            Log.d(TAG, "starting background task");
            Session session = new RetrievingTask(db).execute(sessionId).get();
            Log.d(TAG, "finished background task");
            displaySessionData(session);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void displaySessionData(Session session) {
        Log.d(TAG, "displaying session " + session.getMetadata().getDate().toString()
                + " with values: " + session.getValues().toString());
        Map<String, SessionData> sessionValues = session.getValues();
        idTextView.setText(getIntent().getStringExtra(extra));
        dateTextView.setText(session.getMetadata().getDate().format(formatter));

        if (sessionValues.containsKey(SupportedCommands.ENGINE_RPM)) {
            ObdCommand obdCommand = new RPMCommand();
            String engineSpeedText = sessionValues.get(SupportedCommands.ENGINE_RPM).value + BLANK + obdCommand.getResultUnit();
            rpmTextView.setText(engineSpeedText);
        }
        if (sessionValues.containsKey(SupportedCommands.SPEED)) {
            ObdCommand obdCommand = new SpeedCommand();
            String vehicleSpeedText = sessionValues.get(SupportedCommands.SPEED).value + BLANK + obdCommand.getResultUnit();
            vehicleSpeedTextView.setText(vehicleSpeedText);
        }
        if (sessionValues.containsKey(SupportedCommands.AMBIENT_AIR_TEMP)) {
            ObdCommand obdCommand = new AmbientAirTemperatureCommand();
            String temperatureText = sessionValues.get(SupportedCommands.AMBIENT_AIR_TEMP).value + BLANK + obdCommand.getResultUnit();
            ambientTemperatureTextView.setText(temperatureText);
        }
    }

    private static class RetrievingTask extends AsyncTask<Integer, Void, Session> {
        private final StreamingDataDatabase db;

        private RetrievingTask(StreamingDataDatabase db) {
            this.db = db;
        }

        @Override
        protected Session doInBackground(Integer... integers) {
            // only accept one integer representing the sessionId to query
            if (integers.length > 1) return null;

            // setup work
            Log.d(TAG, "starting setup work for sessionId " + integers[0]);
            SessionDao sessionDao = db.getSessionDao();
            AmbientTemperatureDao ambientTemperatureDao = db.getAmbientTemperatureDao();
            RPMDao rpmDao = db.getRPMDao();
            VehicleSpeedDao vehicleSpeedDao = db.getVehicleSpeedDao();
            int sessionId = integers[0];

            // querying the data
            Log.d(TAG, "querying data");
            SessionEntity sessionEntity = sessionDao.getSessionById(sessionId);
            RPMEntity rpmEntity = rpmDao.getRPMById(sessionId);
            VehicleSpeedEntity vehicleSpeedEntity = vehicleSpeedDao.getVehicleSpeedById(sessionId);
            AmbientTemperatureEntity ambientTemperatureEntity = ambientTemperatureDao.getAmbientTemperatureById(sessionId);

            // returning the data
            Log.d(TAG, "returning the data for the session from " + sessionEntity.date.toString());
            StreamingSession session = new StreamingSession();
            Map<String, SessionData> values = new HashMap<>();
            if (rpmEntity != null) {
                values.put(SupportedCommands.ENGINE_RPM, SessionData.fromDbEntity(rpmEntity));
            }
            if (vehicleSpeedEntity != null) {
                values.put(SupportedCommands.SPEED, SessionData.fromDbEntity(vehicleSpeedEntity));
            }
            if (ambientTemperatureEntity != null) {
                values.put(SupportedCommands.AMBIENT_AIR_TEMP, SessionData.fromDbEntity(ambientTemperatureEntity));
            }
            Log.d(TAG, "values: " + values.toString());
            session.setValues(values);
            session.setMetadata(new StreamingMetadata(sessionEntity.date));

            return session;
        }
    }
}