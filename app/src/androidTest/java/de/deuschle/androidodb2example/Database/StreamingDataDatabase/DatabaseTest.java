package de.deuschle.androidodb2example.Database.StreamingDataDatabase;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;

public abstract class DatabaseTest {
    protected StreamingDataDatabase db;
    protected SessionDao sessionDao;
    protected RPMDao rpmDao;
    protected AmbientTemperatureDao ambientTemperatureDao;
    protected VehicleSpeedDao vehicleSpeedDao;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, StreamingDataDatabase.class).build();
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
