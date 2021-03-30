package de.deuschle.androidodb2example.Session;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import de.deuschle.obd.commands.ObdCommand;

public class StreamingMetadata implements Metadata {
    private LocalTime startingTime;
    private Duration drivingDuration;
    private LocalDate date;
    private List<ObdCommand> usedCommands;

    @Override
    public Duration getDuration() {
        return drivingDuration;
    }

    @Override
    public List<ObdCommand> getUsedCommands() {
        return usedCommands;
    }

    @Override
    public void setUsedCommands(List<ObdCommand> usedCommands) {
        this.usedCommands = usedCommands;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public void calcDrivingTime(LocalTime end) {
        drivingDuration = Duration.between(startingTime, end);
    }

    @Override
    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }
}
