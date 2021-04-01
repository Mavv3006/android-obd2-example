package de.deuschle.androidodb2example.Database;

import java.util.Map;

import de.deuschle.androidodb2example.Session.Session;
import de.deuschle.androidodb2example.Session.SessionData;
import de.deuschle.obd.enums.AvailableCommand;

import static de.deuschle.androidodb2example.Conversion.SupportedCommands.ambient_air_temp;
import static de.deuschle.androidodb2example.Conversion.SupportedCommands.engine_rpm;
import static de.deuschle.androidodb2example.Conversion.SupportedCommands.speed;

public class SaveSession {
    Session session;
    MyDatabase db;

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
            if (engine_rpm.equals(value.getKey())) {
                rpmDao.insert(sessionData);
            } else if (speed.equals(value.getKey())) {
                vehicleSpeedDao.insert(sessionData);
            } else if (ambient_air_temp.equals(value.getKey())) {
                ambientTemperatureDao.insert(sessionData);
            }
        }
    }
}
