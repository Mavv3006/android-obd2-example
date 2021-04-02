package de.deuschle.androidodb2example.Database;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;

public abstract class DatabaseTest {
    protected MyDatabase db;
    protected SessionDao sessionDao;
    protected RPMDao rpmDao;
    protected AmbientTemperatureDao ambientTemperatureDao;
    protected VehicleSpeedDao vehicleSpeedDao;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MyDatabase.class).build();
        sessionDao = db.getSessionDao();
        rpmDao = db.getRPMDao();
        ambientTemperatureDao = db.getAmbientTemperatureDao();
        vehicleSpeedDao = db.getVehicleSpeedDao();
    }

    @After
    public void tearDown() {
        db.close();
    }
}
