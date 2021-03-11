package de.deuschle.androidodb2example.Streams;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.deuschle.androidodb2example.Services.BluetoothLeService;

public class BleOutputStream extends OutputStream implements SetBleService {

    private BluetoothLeService bleService;
    private final ByteArrayOutputStream valueToWrite = new ByteArrayOutputStream();

    @Override
    public void setBleService(BluetoothLeService bleService) {
        this.bleService = bleService;
    }

    @Override
    public void write(@NonNull byte[] b) throws IOException {
        this.valueToWrite.write(b);
    }

    @Override
    public void write(int i) {
        this.valueToWrite.write((Integer.valueOf(i)).byteValue());
    }

    @Override
    public void write(@NonNull byte[] b, int off, int len) {
        this.valueToWrite.write(b, off, len);
    }

    @Override
    public void flush() {
        this.bleService.writeValue(valueToWrite.toByteArray());
        valueToWrite.reset();
    }

    /**
     * Closing a {@code BleOutputStream} has no effect.
     */
    @Override
    public void close() {
    }
}
