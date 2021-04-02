package de.deuschle.androidodb2example.Database;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SessionDaoTest extends DatabaseTest {

    @Test
    public void insertAndRetrieveById() {
        final SessionEntity entity = new SessionEntity();
        entity.date = LocalDateTime.now();
        entity.sessionId = 0;

        sessionDao.insert(entity);

        assertEquals(1, sessionDao.getLatestSessionId());
    }

    @Test
    public void testingLatestSessionIdByInsertingMultipleEntities() {
        int entitiesCount = 10;
        for (int i = 0; i < entitiesCount; i++) {
            final SessionEntity entity = new SessionEntity();
            entity.date = LocalDateTime.now();
            entity.sessionId = 0;

            sessionDao.insert(entity);
        }

        assertEquals(entitiesCount, sessionDao.getLatestSessionId());
        assertEquals(entitiesCount, sessionDao.getAll().size());
    }

    @Test
    public void insertingWithSessionId() {
        final SessionEntity entity = new SessionEntity();
        entity.date = LocalDateTime.now();
        entity.sessionId = 2;

        sessionDao.insert(entity);

        assertEquals(2, sessionDao.getLatestSessionId());
        assertEquals(1, sessionDao.getAll().size());
    }

    @Test
    public void getSessionById() {
        final int sessionId = 2;
        final LocalDateTime dateTime = LocalDateTime.now();
        final SessionEntity entity = new SessionEntity();
        entity.date = dateTime;
        entity.sessionId = sessionId;

        sessionDao.insert(entity);

        final SessionEntity result = sessionDao.getSessionById(sessionId);

        assertEquals(sessionId, result.sessionId);
        assertEquals(dateTime, result.date);
    }

    @Test
    public void delete() {
        final int sessionId = 10;
        final LocalDateTime dateTime = LocalDateTime.now();
        final SessionEntity entity = new SessionEntity();
        entity.date = dateTime;
        entity.sessionId = sessionId;

        sessionDao.insert(entity);
        int result = sessionDao.delete(entity);

        assertEquals(1, result);
        assertEquals(0, sessionDao.getAll().size());
    }
}