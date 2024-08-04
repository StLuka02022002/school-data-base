package org.example.sckooldatabase.service;

import org.example.sckooldatabase.database.DataBase;
import org.example.sckooldatabase.object.Subject;
import org.example.sckooldatabase.object.Teacher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeacherSubjectService {
    private DataBase dataBase;

    public TeacherSubjectService() {
        this.dataBase = new DataBase();
    }

    public void addSubjectToTeacher(Long teacherId, Long subjectId) {
        String sql = "INSERT INTO teacher_subject (teacher_id, subject_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, teacherId);
            preparedStatement.setLong(2, subjectId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeSubjectFromTeacher(Long teacherId, Long subjectId) {
        String sql = "DELETE FROM teacher_subject WHERE teacher_id = ? AND subject_id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, teacherId);
            preparedStatement.setLong(2, subjectId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Subject> getSubjectsByTeacherId(Long teacherId) {
        String sql = "SELECT s.* FROM subjects s INNER JOIN teacher_subject ts ON s.id = ts.subject_id WHERE ts.teacher_id = ?";
        List<Subject> subjects = new ArrayList<>();
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, teacherId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                subjects.add(new Subject(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public void removeAllSubjectsFromTeacher(Long teacherId) {
        String sql = "DELETE FROM teacher_subject WHERE teacher_id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, teacherId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Teacher> getTeachersBySubjectId(Long subjectId) {
        String sql = "SELECT t.* FROM teachers t INNER JOIN teacher_subject ts ON t.id = ts.teacher_id WHERE ts.subject_id = ?";
        List<Teacher> teachers = new ArrayList<>();
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, subjectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                teachers.add(extractTeacherFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    private Teacher extractTeacherFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String threeName = resultSet.getString("three_name");
        LocalDate birthday = resultSet.getDate("birthday") != null ? resultSet.getDate("birthday").toLocalDate() : null;
        int rank = resultSet.getInt("teacher_rank");
        Teacher teacher = new Teacher(firstName, lastName, threeName, birthday);
        teacher.setId(id);
        teacher.setRank(rank);
        teacher.setSubjects(getSubjectsByTeacherId(id));
        return teacher;
    }
}
