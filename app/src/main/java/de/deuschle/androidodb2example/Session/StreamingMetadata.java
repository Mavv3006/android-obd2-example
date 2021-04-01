package de.deuschle.androidodb2example.Session;

import java.time.LocalDate;

public class StreamingMetadata implements Metadata {
    private LocalDate date;

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
