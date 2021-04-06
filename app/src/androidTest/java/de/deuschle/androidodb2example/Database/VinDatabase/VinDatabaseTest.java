package de.deuschle.androidodb2example.Database.VinDatabase;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;

public abstract class VinDatabaseTest {
    private VinDatabase db;

    protected VinDao vinDao;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @After
    public void tearDown() {
        db.close();
    }

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, VinDatabase.class).build();
        vinDao = db.getVinDao();
    }
}
