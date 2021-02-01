package de.deuschle.androidodb2example.Streams;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.deuschle.androidodb2example.BluetoothLeService;

public class BleOutputStream extends OutputStream {

    private BluetoothLeService bleService;
    private final ByteArrayOutputStream valueToWrite = new ByteArrayOutputStream();

    public void setBleService(BluetoothLeService bleService) {
        this.bleService = bleService;
    }

    @Override
    public void write(@NonNull byte[] b) throws IOException {
        this.valueToWrite.write(b);
    }

    @Override
    public void write(int i) throws IOException {
        this.valueToWrite.write((Integer.valueOf(i)).byteValue());
    }

    @Override
    public void flush() throws IOException {
        this.bleService.writeValue(valueToWrite.toByteArray());
        valueToWrite.reset();
    }
}
