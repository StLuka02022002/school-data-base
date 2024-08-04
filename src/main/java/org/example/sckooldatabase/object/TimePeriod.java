package org.example.sckooldatabase.object;

import org.example.sckooldatabase.utils.DateUtil;

import java.time.LocalTime;

public class TimePeriod {
    private LocalTime startTime;
    private LocalTime endTime;

    public TimePeriod(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimePeriod(String startTime, String endTime) {
        this.startTime = DateUtil.parseTime(startTime);
        this.endTime = DateUtil.parseTime(endTime);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

