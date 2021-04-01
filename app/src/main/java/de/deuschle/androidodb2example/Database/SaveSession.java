package de.deuschle.androidodb2example.Database;

import androidx.room.Room;

import java.util.Map;

import de.deuschle.androidodb2example.Session.Session;
import de.deuschle.androidodb2example.Session.SessionData;
import de.deuschle.obd.enums.AvailableCommand;

public class SaveSession {
    Session session;
    Database db;
    static final String engine_rpm = AvailableCommand.ENGINE_RPM.getCommand().substring(3);
    static final String speed = AvailableCommand.SPEED.getCommand().substring(3);
    static final String ambient_air_temp = AvailableCommand.AMBIENT_AIR_TEMP.getCommand().substring(3);

    public SaveSession(Session session, Database db) {
        this.session = session;
        this.db = db;
    }

    public void save() {
        // Session in die Sessiontabelle einfügen
        SessionDao sessionDao = db.getSessionDao();
        sessionDao.insert(session.getMetadata().getDate());
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
