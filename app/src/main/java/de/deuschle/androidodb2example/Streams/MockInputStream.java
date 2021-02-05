package de.deuschle.androidodb2example.Streams;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MockInputStream extends MyInputStream {

    private final List<Integer> values = new ArrayList<>();

    public MockInputStream() {
        this.values.add(0x0131);
        this.values.add(0x41);
        this.values.add(0x31);
        this.values.add(0x06);
        this.values.add(0xf4);
        this.values.add(62);
    }

    @Override
    public int read() {
        Log.d("EngineRPM", values.toString());
        return values.remove(0);
    }

    @Override
    public void setData(String data) {
    }
}
