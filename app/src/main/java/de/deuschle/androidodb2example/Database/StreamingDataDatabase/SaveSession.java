package de.deuschle.androidodb2example.Database.StreamingDataDatabase;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Map;

import de.deuschle.androidodb2example.Activities.Commands.Streaming.StreamingActivity;
import de.deuschle.androidodb2example.Session.Session;
import de.deuschle.androidodb2example.Session.SessionData;

import static de.deuschle.androidodb2example.Util.SupportedCommands.AMBIENT_AIR_TEMP;
import static de.deuschle.androidodb2example.Util.SupportedCommands.ENGINE_RPM;
import static de.deuschle.androidodb2example.Util.SupportedCommands.SPEED;

public class SaveSession {
    private static final String TAG = SaveSession.class.getSimpleName();
    private final Session session;
    private final StreamingDataDatabase db;
    private final WeakReference<StreamingActivity> weakReference;

    public SaveSession(Session session, StreamingDataDatabase db, StreamingActivity activity) {
        this.session = session;
        this.db = db;
        this.weakReference = new WeakReference<>(activity);
    }

    public void save() {
        Log.i(TAG, "starting saving the current session");
        session.willBeSaved();
        new SavingTask(session, db, weakReference.get()).execute();
    }

    private static class SavingTask extends AsyncTask<Session, Void, Boolean> {
        private final Session session;
        private final StreamingDataDatabase db;
        private final WeakReference<StreamingActivity> weakReference;

        public SavingTask(Session session, StreamingDataDatabase db, StreamingActivity activity) {
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
                Log.w(TAG, "saving new session");
                sessionDao.insert(sessionEntity);
                RPMDao rpmDao = db.getRPMDao();
                VehicleSpeedDao vehicleSpeedDao = db.getVehicleSpeedDao();
                AmbientTemperatureDao ambientTemperatureDao = db.getAmbientTemperatureDao();

                // Neueste SessionID holen
                int sessionId = sessionDao.getLatestSessionId();
                Log.d(TAG, "latest sessionId: " + sessionId);

                // Über Datenpunkte iterieren und speichern
                for (Map.Entry<String, SessionData> value : session.getValues().entrySet()) {
                    SessionData sessionData = value.getValue();
                    sessionData.sessionId = sessionId;
                    if (ENGINE_RPM.equals(value.getKey())) {
                        Log.d(TAG, "saved Engine RPM");
                        rpmDao.insert(sessionData);
                    } else if (SPEED.equals(value.getKey())) {
                        Log.d(TAG, "saved Vehicle Speed");
                        vehicleSpeedDao.insert(sessionData);
                    } else if (AMBIENT_AIR_TEMP.equals(value.getKey())) {
                        Log.d(TAG, "saved Ambient Temperature");
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
