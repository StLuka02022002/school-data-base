package org.example.sckooldatabase.service;

import org.example.sckooldatabase.data.RoleLesson;
import org.example.sckooldatabase.database.DataBase;
import org.example.sckooldatabase.object.Classroom;
import org.example.sckooldatabase.object.Lesson;
import org.example.sckooldatabase.object.Subject;
import org.example.sckooldatabase.object.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LessonDetailsService {
    private DataBase dataBase;
    private ClassroomService classroomService;
    private SubjectService subjectService;
    private TeacherService teacherService;
    private LessonService lessonService;

    public LessonDetailsService() {
        this.dataBase = new DataBase();
        this.classroomService = new ClassroomService();
        this.subjectService = new SubjectService();
        this.teacherService = new TeacherService();
        this.lessonService = new LessonService();
    }

    public List<Teacher> getTeachersByLesson(Lesson lesson) {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT t.* FROM teachers t "
                + "JOIN lessons l ON t.id = l.teacher_id "
                + "WHERE l.id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, lesson.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                teachers.add(teacherService.extractTeacherFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    public List<Subject> getSubjectsByLesson(Lesson lesson) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT s.* FROM subjects s "
                + "JOIN lessons l ON s.id = l.subject_id "
                + "WHERE l.id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, lesson.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                subjects.add(subjectService.extractSubjectFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public List<Classroom> getClassroomsByLesson(Lesson lesson) {
        List<Classroom> classrooms = new ArrayList<>();
        String sql = "SELECT c.* FROM classrooms c "
                + "JOIN lessons l ON c.id = l.classroom_id "
                + "WHERE l.id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, lesson.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                classrooms.add(classroomService.extractClassroomFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classrooms;
    }

    public List<Lesson> getLessonsByTeacher(Teacher teacher) {
        List<Lesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM lessons WHERE teacher_id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, teacher.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lessons.add(lessonService.extractLessonFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }

    public int getLessonCountByTeacher(Teacher teacher) {
        String sql = "SELECT COUNT(*) AS lesson_count FROM lessons WHERE teacher_id = ?";
        int lessonCount = 0;

        try (Connection connection = dataBase.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, teacher.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                lessonCount = resultSet.getInt("lesson_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lessonCount;
    }

    public List<Lesson> getLessonsBySubject(Subject subject) {
        List<Lesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM lessons WHERE subject_id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, subject.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lessons.add(lessonService.extractLessonFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }

    public int getClassCountBySubject(Subject subject) {
        String sql = "SELECT COUNT(*) AS lesson_count FROM lessons WHERE subject_id = ? AND role = ?";
        int lessonCount = 0;

        try (Connection connection = dataBase.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, subject.getId());
            preparedStatement.setString(2, RoleLesson.CLASS.name());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                lessonCount = resultSet.getInt("lesson_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lessonCount;
    }

    public int getExamCountBySubject(Subject subject) {
        String sql = "SELECT COUNT(*) AS lesson_count FROM lessons WHERE subject_id = ? AND role = ?";
        int lessonCount = 0;

        try (Connection connection = dataBase.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, subject.getId());
            preparedStatement.setString(2, RoleLesson.EXAM.name());  // Assuming RoleLesson.EXAM.getRole() returns the string representation of the role

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                lessonCount = resultSet.getInt("lesson_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lessonCount;
    }


    public List<Lesson> getLessonsByClassroom(Classroom classroom) {
        List<Lesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM lessons WHERE classroom_id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, classroom.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lessons.add(lessonService.extractLessonFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }
}
