package de.deuschle.androidodb2example.Database;

import java.util.Map;

import de.deuschle.androidodb2example.Session.Session;
import de.deuschle.androidodb2example.Session.SessionData;

import static de.deuschle.androidodb2example.Conversion.SupportedCommands.AMBIENT_AIR_TEMP;
import static de.deuschle.androidodb2example.Conversion.SupportedCommands.ENGINE_RPM;
import static de.deuschle.androidodb2example.Conversion.SupportedCommands.SPEED;

public class SaveSession {
    private final Session session;
    private final MyDatabase db;

    public SaveSession(Session session, MyDatabase db) {
        this.session = session;
        this.db = db;
    }

    public void save() {
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
    }
}
