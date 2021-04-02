package de.deuschle.androidodb2example.Database;

import org.junit.Test;

import de.deuschle.androidodb2example.Session.SessionData;

import static org.junit.Assert.assertEquals;

public class VehicleSpeedDaoTest extends DatabaseTest {

    @Test
    public void getVehicleSpeedById() {
        final int sessionId = 10;
        final double value = 10.3;
        final SessionData data = new SessionData();
        data.sessionId = sessionId;
        data.value = value;

        vehicleSpeedDao.insert(data);
        VehicleSpeedEntity entity = vehicleSpeedDao.getVehicleSpeedById(sessionId);

        assertEquals(value, entity.value, 0);
        assertEquals(sessionId, entity.sessionId, 0);
    }

    @Test
    public void insert() {
        final SessionData data = new SessionData();
        data.sessionId = 1;

        vehicleSpeedDao.insert(data);

        vehicleSpeedDao.getAll().observeForever(entities -> assertEquals(1, entities.size()));
    }

    @Test
    public void delete() {
        final int sessionId = 10;
        final SessionData data = new SessionData();
        data.sessionId = sessionId;
        final VehicleSpeedEntity entity = new VehicleSpeedEntity();
        entity.sessionId = sessionId;

        vehicleSpeedDao.insert(data);
        vehicleSpeedDao.delete(entity);

        vehicleSpeedDao.getAll().observeForever(entities -> assertEquals(0, entities.size()));
    }

    @Test
    public void getAll() {
        final int count = 10;
        for (int i = 0; i < count; ) {
            final SessionData data = new SessionData();
            data.sessionId = ++i;
            vehicleSpeedDao.insert(data);
        }

        vehicleSpeedDao.getAll().observeForever(entities -> assertEquals(count, entities.size()));
    }
}