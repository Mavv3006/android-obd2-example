package de.deuschle.androidodb2example.Database;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import de.deuschle.androidodb2example.Session.SessionData;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RPMDaoTest extends DatabaseTest {

    @Test
    public void insertDataAndReadFromSessionId() {
        final SessionData data = new SessionData();
        data.sessionId = 1;

        rpmDao.insert(data);

        RPMEntity entity = rpmDao.getRPMById(1);
        assertEquals(data.sessionId, entity.sessionId);
        assertEquals(data.n, entity.n);
        assertEquals(data.value, entity.value, 0);
    }

    @Test
    public void insertAndDeleteData() {
        final SessionData data = new SessionData();
        data.sessionId = 1;
        data.n = 0;
        data.value = 0;
        final RPMEntity rpmEntity = new RPMEntity();
        rpmEntity.sessionId = 1;
        rpmEntity.n = 0;
        rpmEntity.value = 0;

        rpmDao.insert(data);

        rpmDao.delete(rpmEntity);

        List<RPMEntity> all = rpmDao.getAll();

        assertEquals(0, all.size());
    }

    @Test
    public void retrievingAllData() {
        int entitiesCount = 10;
        for (int i = 0; i < entitiesCount; i++) {
            final SessionData data = new SessionData();
            data.sessionId = i;
            rpmDao.insert(data);
        }

        List<RPMEntity> all = rpmDao.getAll();

        assertEquals(entitiesCount, all.size());
    }
}