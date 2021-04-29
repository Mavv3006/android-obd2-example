package de.deuschle.androidodb2example.Database.VinDatabase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VinDaoTestVin extends VinDatabaseTest {

    @Test
    public void isVinInDbTrue() {
        final String data = "test";
        final VinEntity entity = new VinEntity();
        entity.vin = data;

        vinDao.insert(entity);

        assertEquals(1, vinDao.isVinInDb(data), 0);
    }

    @Test
    public void isVinInDbFalse() {
        assertEquals(0, vinDao.isVinInDb("test"), 0);
    }

    @Test
    public void insert() {
        final String data = "test";
        final VinEntity entity = new VinEntity();
        entity.vin = data;

        vinDao.insert(entity);

        vinDao.getAll().observeForever(vinEntities -> {
            assertEquals(1, vinEntities.size());
            assertEquals(data, vinEntities.get(0).vin);
            assertEquals(1, vinEntities.get(0).id);
        });
    }

    @Test
    public void insertMultiple() {
        final int count = 5;
        final List<VinEntity> entityList = new ArrayList<>();
        VinEntity entity;
        for (int i = 0; i < count; i++) {
            entity = new VinEntity();
            entity.vin = String.valueOf(i);
            entityList.add(entity);
        }

        for (VinEntity entity1 : entityList) {
            vinDao.insert(entity1);
        }

        vinDao.getAll().observeForever(vinEntities -> assertEquals(count, vinEntities.size()));
    }
}