package org.example.sckooldatabase.object;

import java.util.List;
import java.util.Objects;

public class Classroom {
    private Long id;
    private int number;
    private int floor;
    private int numberSeat;
    private List<Subject> subjects;

    public Classroom(int number, int floor, int numberSeat) {
        this.number = number;
        this.floor = floor;
        this.numberSeat = numberSeat;
    }

    public Classroom(int number, int floor, int numberSeat, List<Subject> subjects) {
        this(number, floor, numberSeat);
        this.subjects = subjects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getNumberSeat() {
        return numberSeat;
    }

    public void setNumberSeat(int numberSeat) {
        this.numberSeat = numberSeat;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classroom classroom = (Classroom) o;
        return number == classroom.number && floor == classroom.floor && numberSeat == classroom.numberSeat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, floor, numberSeat);
    }
}

