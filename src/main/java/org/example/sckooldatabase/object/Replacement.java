package org.example.sckooldatabase.object;

public class Replacement {
    private Long id;
    private Long lessonId;
    private Teacher teacherFrom;
    private Teacher teacherTo;

    public Replacement() {
    }

    public Replacement(Long lessonId, Teacher teacherFrom, Teacher teacherTo) {
        this.lessonId = lessonId;
        this.teacherFrom = teacherFrom;
        this.teacherTo = teacherTo;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public Teacher getTeacherFrom() {
        return teacherFrom;
    }

    public void setTeacherFrom(Teacher teacherFrom) {
        this.teacherFrom = teacherFrom;
    }

    public Teacher getTeacherTo() {
        return teacherTo;
    }

    public void setTeacherTo(Teacher teacherTo) {
        this.teacherTo = teacherTo;
    }
}
