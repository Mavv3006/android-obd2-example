package de.deuschle.androidodb2example.Streams;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

public class BleInputStream extends InputStream {
    private String data;
    private Queue<Integer> integerList = new LinkedList<>();

    @Override
    public int read() {
        return integerList.remove();
    }

    public void setData(String data) {
        this.data = data;
        processData();
    }

    private void processData() {
        String[] dataArray = data.split(" ");

        Queue<Integer> intArray = new LinkedList<>();
        for (String stringData : dataArray) {
            intArray.add(Integer.parseInt(stringData, 16));
        }

        this.integerList = intArray;
    }

}
