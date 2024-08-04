package org.example.sckooldatabase.object;

import org.example.sckooldatabase.data.RoleLesson;

import java.time.LocalDate;

public class Lesson {
    private Long id;
    private LocalDate date;
    private TimePeriod timePeriod;
    private Classroom classroom;
    private Subject subject;
    private Teacher teacher;
    private RoleLesson roleLesson;

    public Lesson() {

    }

    public Lesson(LocalDate date, TimePeriod timePeriod) {
        this.date = date;
        this.timePeriod = timePeriod;
        this.roleLesson = RoleLesson.CLASS;
    }

    public Lesson(LocalDate date, TimePeriod timePeriod, Classroom classroom, Subject subject, Teacher teacher) {
        this(date, timePeriod);
        this.classroom = classroom;
        this.subject = subject;
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public RoleLesson getRoleLesson() {
        return roleLesson;
    }

    public void setRoleLesson(RoleLesson roleLesson) {
        this.roleLesson = roleLesson;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", date=" + date +
                ", timePeriod=" + timePeriod +
                ", classroom=" + classroom +
                ", subject=" + subject +
                ", teacher=" + teacher +
                ", roleLesson=" + roleLesson +
                '}';
    }
}
