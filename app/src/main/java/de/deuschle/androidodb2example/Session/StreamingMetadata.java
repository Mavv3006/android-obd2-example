package de.deuschle.androidodb2example.Session;

import java.time.LocalDateTime;

public class StreamingMetadata implements Metadata {
    private LocalDateTime date;

    @Override
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
