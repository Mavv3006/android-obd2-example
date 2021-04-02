package de.deuschle.androidodb2example.Database;

import androidx.lifecycle.LiveData;

import org.junit.Test;

import java.util.List;

import de.deuschle.androidodb2example.Session.SessionData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AmbientTemperatureDaoTest extends DatabaseTest {

    @Test
    public void getById() {
        final int sessionId = 1;
        final double value = 10.4;
        final SessionData data = new SessionData();
        data.sessionId = sessionId;
        data.value = value;

        ambientTemperatureDao.insert(data);

        AmbientTemperatureEntity entity = ambientTemperatureDao.getAmbientTemperatureById(sessionId);
        assertEquals(sessionId, entity.sessionId);
        assertEquals(value, entity.value, 0);
    }

    @Test
    public void delete() {
        final int sessionId = 10;
        final SessionData data = new SessionData();
        data.sessionId = sessionId;
        final AmbientTemperatureEntity entity = new AmbientTemperatureEntity();
        entity.sessionId = sessionId;

        ambientTemperatureDao.insert(data);
        ambientTemperatureDao.delete(entity);

        LiveData<List<AmbientTemperatureEntity>> all = ambientTemperatureDao.getAll();
        all.observeForever(entities -> assertEquals(0, entities.size()));
        all.observeForever(entities -> assertTrue(entities.isEmpty()));

    }

    @Test
    public void insert() {
        final SessionData data = new SessionData();
        data.sessionId = 0;

        ambientTemperatureDao.insert(data);
        LiveData<List<AmbientTemperatureEntity>> all = ambientTemperatureDao.getAll();
        all.observeForever(entities -> assertEquals(1, entities.size()));

    }
}