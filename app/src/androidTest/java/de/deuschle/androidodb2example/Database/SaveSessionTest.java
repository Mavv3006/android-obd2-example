package de.deuschle.androidodb2example.Database;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import de.deuschle.androidodb2example.Conversion.SupportedCommands;
import de.deuschle.androidodb2example.Session.Metadata;
import de.deuschle.androidodb2example.Session.Session;
import de.deuschle.androidodb2example.Session.SessionData;
import de.deuschle.androidodb2example.Session.StreamingMetadata;

import static org.junit.Assert.assertEquals;

public class SaveSessionTest {
    private MyDatabase db;
    private static final LocalDateTime dateTime = LocalDateTime.now();

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private static class TestSession implements Session {
        @Override
        public Metadata getMetadata() {
            StreamingMetadata metadata = new StreamingMetadata();
            metadata.setDate(dateTime);
            return metadata;
        }

        @Override
        public Map<String, SessionData> getValues() {
            Map<String, SessionData> map = new HashMap<>();
            map.put(SupportedCommands.ENGINE_RPM, new SessionData(20, 0, 20));
            map.put(SupportedCommands.SPEED, new SessionData(10, 0, 20));
            map.put(SupportedCommands.AMBIENT_AIR_TEMP, new SessionData(5, 0, 20));
            return map;
        }
    }

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MyDatabase.class).build();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void save() {
        Session session = new TestSession();
        SaveSession saveSession = new SaveSession(session, db, weakReference);

        saveSession.save();

        db
                .getSessionDao()
                .getAll()
                .observeForever(entities -> {
                    assertEquals(1, entities.size());
                    SessionEntity entity = entities.get(0);
                    assertEquals(1, entity.sessionId);
                    assertEquals(dateTime, entity.date);
                });
        db
                .getAmbientTemperatureDao()
                .getAll()
                .observeForever(entities -> {
                    assertEquals(1, entities.size());
                    AmbientTemperatureEntity entity = entities.get(0);
                    assertEquals(1, entity.sessionId);
                    assertEquals(5, entity.value, 0);
                });
        db
                .getRPMDao()
                .getAll()
                .observeForever(entities -> {
                    assertEquals(1, entities.size());
                    RPMEntity entity = entities.get(0);
                    assertEquals(1, entity.sessionId);
                    assertEquals(20, entity.value, 0);
                });
        db
                .getVehicleSpeedDao()
                .getAll()
                .observeForever(entities -> {
                    assertEquals(1, entities.size());
                    VehicleSpeedEntity entity = entities.get(0);
                    assertEquals(1, entity.sessionId);
                    assertEquals(10, entity.value, 0);
                });
    }
}