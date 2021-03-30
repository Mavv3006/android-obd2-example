package de.deuschle.androidodb2example.Session;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import de.deuschle.obd.commands.ObdCommand;

public interface Metadata {
    Duration getDuration();

    void setStartingTime(LocalTime startingTime);

    void calcDrivingTime(LocalTime end);

    void setDate(LocalDate date);

    List<ObdCommand> getUsedCommands();

    LocalDate getDate();

    void setUsedCommands(List<ObdCommand> commands);
}
