package de.deuschle.androidodb2example.Session;

import java.time.LocalDate;

public interface Metadata {
    void setDate(LocalDate date);

    LocalDate getDate();
}
