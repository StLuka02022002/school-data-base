package org.example.sckooldatabase.data;

import org.example.sckooldatabase.object.TimePeriod;

public enum TimePeriods {
    LESSON_1("08:00", "08:45"),
    LESSON_2("09:00", "09:45"),
    LESSON_3("10:00", "10:45"),
    LESSON_4("11:00", "11:45"),
    LESSON_5("12:00", "12:45"),
    LESSON_6("13:00", "13:45"),
    LESSON_7("14:00", "14:45");

    private TimePeriod timePeriod;

    TimePeriods(String startTime, String endTime) {
        this.timePeriod = new TimePeriod(startTime, endTime);
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    @Override
    public String toString() {
        return name() + " (" + timePeriod.getStartTime() + " - " + timePeriod.getEndTime() + ")";
    }
}


