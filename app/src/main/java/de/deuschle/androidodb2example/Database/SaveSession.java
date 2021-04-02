package de.deuschle.androidodb2example.Database;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Map;

import de.deuschle.androidodb2example.Activities.Streaming.StreamingActivity;
import de.deuschle.androidodb2example.Session.Session;
import de.deuschle.androidodb2example.Session.SessionData;

import static de.deuschle.androidodb2example.Conversion.SupportedCommands.AMBIENT_AIR_TEMP;
import static de.deuschle.androidodb2example.Conversion.SupportedCommands.ENGINE_RPM;
import static de.deuschle.androidodb2example.Conversion.SupportedCommands.SPEED;

public class SaveSession {
    private static final String TAG = SaveSession.class.getSimpleName();
    private final Session session;
    private final MyDatabase db;
    private final WeakReference<StreamingActivity> weakReference;

    public SaveSession(Session session, MyDatabase db, StreamingActivity activity) {
        this.session = session;
        this.db = db;
        this.weakReference = new WeakReference<>(activity);
    }

    public void save() {
        new SavingTask(session, db, weakReference.get()).execute();
    }

    private static class SavingTask extends AsyncTask<Session, Void, Boolean> {
        private final Session session;
        private final MyDatabase db;
        private final WeakReference<StreamingActivity> weakReference;

        public SavingTask(Session session, MyDatabase db, StreamingActivity activity) {
            this.session = session;
            this.db = db;
            this.weakReference = new WeakReference<>(activity);
        }


        @Override
        protected Boolean doInBackground(Session... sessions) {
            try {
                // Session in die Sessiontabelle einfügen
                SessionDao sessionDao = db.getSessionDao();
                SessionEntity sessionEntity = new SessionEntity();
                sessionEntity.date = session.getMetadata().getDate();
                sessionEntity.sessionId = 0;
                sessionDao.insert(sessionEntity);
                RPMDao rpmDao = db.getRPMDao();
                VehicleSpeedDao vehicleSpeedDao = db.getVehicleSpeedDao();
                AmbientTemperatureDao ambientTemperatureDao = db.getAmbientTemperatureDao();

                // Neueste SessionID holen
                int sessionId = sessionDao.getLatestSessionId();

                // Über Datenpunkte iterieren und speichern
                for (Map.Entry<String, SessionData> value : session.getValues().entrySet()) {
                    SessionData sessionData = value.getValue();
                    sessionData.sessionId = sessionId;
                    if (ENGINE_RPM.equals(value.getKey())) {
                        rpmDao.insert(sessionData);
                    } else if (SPEED.equals(value.getKey())) {
                        vehicleSpeedDao.insert(sessionData);
                    } else if (AMBIENT_AIR_TEMP.equals(value.getKey())) {
                        ambientTemperatureDao.insert(sessionData);
                    }
                }
                Log.i(TAG, "returning true");
                return true;
            } catch (Exception | Error e) {
                Log.i(TAG, "returning false");
                Log.e(TAG, e.getMessage() == null ? "null" : e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            StreamingActivity activity = weakReference.get();
            if (activity == null) return;

            Toast.makeText(activity, "Session successfully saved", Toast.LENGTH_LONG).show();
        }
    }

}
