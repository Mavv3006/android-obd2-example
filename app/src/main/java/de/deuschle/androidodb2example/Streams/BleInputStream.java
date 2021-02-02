package de.deuschle.androidodb2example.Streams;

import android.util.Log;

import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import de.deuschle.androidodb2example.LogTags.LogTags;

public class BleInputStream extends InputStream {
    private String inputData;
    private final Queue<Integer> integerList = new LinkedList<>();

    @Override
    public int read() {
        return integerList.remove();
    }

    public void setData(String data) {
        this.inputData = data;
        processData();
    }

    private void processData() {
        String[] dataArray = inputData.split(" |\n|\r");

        Log.d(LogTags.INPUT_STREAM_DATA, "Splitted Data: " + Arrays.toString(dataArray));

        for (String s : dataArray) {
            if (s.equals(">")) {
                integerList.add(62);
                break;
            }
            if (!s.equals("")) {
                integerList.add(Integer.parseInt(s, 16));
            }
        }

        Log.d(LogTags.INPUT_STREAM_DATA, "Integer Array: " + this.integerList.toString());
    }

}
