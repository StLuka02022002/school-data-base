package org.example.sckooldatabase.service;

import com.mysql.cj.xdevapi.SqlStatement;
import org.example.sckooldatabase.data.RoleLesson;
import org.example.sckooldatabase.database.DataBase;
import org.example.sckooldatabase.object.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LessonService {
    private DataBase dataBase;
    private ClassroomService classroomService;
    private SubjectService subjectService;
    private TeacherService teacherService;

    public LessonService() {
        this.dataBase = new DataBase();
        this.classroomService = new ClassroomService();
        this.subjectService = new SubjectService();
        this.teacherService = new TeacherService();
    }

    public Lesson addLesson(Lesson lesson) {
        String sql = "INSERT INTO lessons (date, start_time, end_time, classroom_id, subject_id, teacher_id, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(lesson.getDate()));
            preparedStatement.setTime(2, java.sql.Time.valueOf(lesson.getTimePeriod().getStartTime()));
            preparedStatement.setTime(3, java.sql.Time.valueOf(lesson.getTimePeriod().getEndTime()));
            preparedStatement.setLong(4, lesson.getClassroom() != null ? lesson.getClassroom().getId() : null);
            preparedStatement.setLong(5, lesson.getSubject() != null ? lesson.getSubject().getId() : null);
            preparedStatement.setLong(6, lesson.getTeacher() != null ? lesson.getTeacher().getId() : null);
            preparedStatement.setString(7, lesson.getRoleLesson().name());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                lesson.setId(generatedKeys.getLong(1));
                return lesson;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeLesson(Long lessonId) {
        String sql = "DELETE FROM lessons WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, lessonId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Lesson updateLesson(Lesson lesson) {
        String sql = "UPDATE lessons SET date = ?, start_time = ?, end_time = ?, classroom_id = ?, subject_id = ?, teacher_id = ?, role = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(lesson.getDate()));
            preparedStatement.setTime(2, java.sql.Time.valueOf(lesson.getTimePeriod().getStartTime()));
            preparedStatement.setTime(3, java.sql.Time.valueOf(lesson.getTimePeriod().getEndTime()));
            preparedStatement.setLong(4, lesson.getClassroom() != null ? lesson.getClassroom().getId() : null);
            preparedStatement.setLong(5, lesson.getSubject() != null ? lesson.getSubject().getId() : null);
            preparedStatement.setLong(6, lesson.getTeacher() != null ? lesson.getTeacher().getId() : null);
            preparedStatement.setString(7, lesson.getRoleLesson().name());
            preparedStatement.setLong(8, lesson.getId());
            preparedStatement.executeUpdate();
            return lesson;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Lesson getLessonById(Long lessonId) {
        String sql = "SELECT * FROM lessons WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, lessonId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractLessonFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Lesson> getLessonsByDateAndTimePeriod(LocalDate date, TimePeriod timePeriod) {
        String sql = "SELECT * FROM lessons WHERE date = ? AND start_time = ? AND end_time = ?";
        List<Lesson> lessons = new ArrayList<>();
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setTime(2, Time.valueOf(timePeriod.getStartTime()));
            preparedStatement.setTime(3, Time.valueOf(timePeriod.getEndTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lessons.add(extractLessonFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }

    public List<Lesson> getLessonsByDate(LocalDate date) {
        String sql = "SELECT * FROM lessons WHERE date = ?";
        List<Lesson> lessons = new ArrayList<>();
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(date));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lessons.add(extractLessonFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }

    public List<Lesson> getLessonsByTimePeriod(TimePeriod timePeriod) {
        String sql = "SELECT * FROM lessons WHERE start_time = ? AND end_time = ?";
        List<Lesson> lessons = new ArrayList<>();
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setTime(1, Time.valueOf(timePeriod.getStartTime()));
            preparedStatement.setTime(2, Time.valueOf(timePeriod.getEndTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lessons.add(extractLessonFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }

    public List<Lesson> getLessonsByTimePeriodBetween(TimePeriod timePeriod) {
        String sql = "SELECT * FROM lessons WHERE start_time >= ? AND end_time <= ?";
        List<Lesson> lessons = new ArrayList<>();
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setTime(1, Time.valueOf(timePeriod.getStartTime()));
            preparedStatement.setTime(2, Time.valueOf(timePeriod.getEndTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lessons.add(extractLessonFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }

    public List<Lesson> getAllLessons() {
        String sql = "SELECT * FROM lessons";
        List<Lesson> lessons = new ArrayList<>();
        try (Statement statement = dataBase.connect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                lessons.add(extractLessonFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }

    public Lesson extractLessonFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        LocalTime startTime = resultSet.getTime("start_time").toLocalTime();
        LocalTime endTime = resultSet.getTime("end_time").toLocalTime();
        Classroom classroom = classroomService.getClassroomById(resultSet.getLong("classroom_id"));
        Subject subject = subjectService.getSubjectById(resultSet.getLong("subject_id"));
        Teacher teacher = teacherService.getTeacherById(resultSet.getLong("teacher_id"));
        RoleLesson roleLesson = RoleLesson.valueOf(resultSet.getString("role"));

        Lesson lesson = new Lesson(date, new TimePeriod(startTime, endTime), classroom, subject, teacher);
        lesson.setId(id);
        lesson.setRoleLesson(roleLesson);
        return lesson;
    }
}
