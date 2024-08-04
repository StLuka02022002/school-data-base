package org.example.sckooldatabase.service;

import org.example.sckooldatabase.database.DataBase;
import org.example.sckooldatabase.object.Subject;
import org.example.sckooldatabase.object.Teacher;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeacherService {
    private DataBase dataBase;
    private TeacherSubjectService teacherSubjectService;

    public TeacherService() {
        this.dataBase = new DataBase();
        this.teacherSubjectService = new TeacherSubjectService();
    }

    public Teacher addTeacher(Teacher teacher) {
        String sql = "INSERT INTO teachers (first_name, last_name, three_name, short_name, birthday, teacher_rank) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, teacher.getFirstName());
            preparedStatement.setString(2, teacher.getLastName());
            preparedStatement.setString(3, teacher.getThreeName());
            preparedStatement.setString(4, teacher.getShortName());
            preparedStatement.setDate(5, teacher.getBirthday() != null ? Date.valueOf(teacher.getBirthday()) : null);
            preparedStatement.setInt(6, teacher.getRank());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                teacher.setId(generatedKeys.getLong(1));
                updateTeacherSubjects(teacher);
                return teacher;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Teacher updateTeacher(Teacher teacher) {
        String sql = "UPDATE teachers SET first_name = ?, last_name = ?, three_name = ?, short_name = ?, birthday = ?, teacher_rank = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setString(1, teacher.getFirstName());
            preparedStatement.setString(2, teacher.getLastName());
            preparedStatement.setString(3, teacher.getThreeName());
            preparedStatement.setString(4, teacher.getShortName());
            preparedStatement.setDate(5, teacher.getBirthday() != null ? Date.valueOf(teacher.getBirthday()) : null);
            preparedStatement.setInt(6, teacher.getRank());
            preparedStatement.setLong(7, teacher.getId());
            preparedStatement.executeUpdate();
            updateTeacherSubjects(teacher);
            return teacher;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteTeacher(Long id) {
        String sql = "DELETE FROM teachers WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            teacherSubjectService.removeAllSubjectsFromTeacher(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Teacher getTeacherById(Long id) {
        String sql = "SELECT * FROM teachers WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractTeacherFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Teacher getTeacherByName(String lastName, String firstName) {
        String sql = "SELECT * FROM teachers WHERE first_name = ? AND last_name = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractTeacherFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Teacher getTeacherByName(String lastName, String firstName, String threeName) {
        String sql = "SELECT * FROM teachers WHERE first_name = ? AND last_name = ? AND three_name = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, threeName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractTeacherFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Teacher> getAllTeachers() {
        String sql = "SELECT * FROM teachers ORDER BY last_name, first_name, three_name";
        List<Teacher> teachers = new ArrayList<>();
        try (Statement statement = dataBase.connect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                teachers.add(extractTeacherFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    public Teacher extractTeacherFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String threeName = resultSet.getString("three_name");
        LocalDate birthday = resultSet.getDate("birthday") != null ? resultSet.getDate("birthday").toLocalDate() : null;
        int rank = resultSet.getInt("teacher_rank");
        Teacher teacher = new Teacher(firstName, lastName, threeName, birthday);
        teacher.setId(id);
        teacher.setRank(rank);
        teacher.setSubjects(teacherSubjectService.getSubjectsByTeacherId(id));
        return teacher;
    }

    private void updateTeacherSubjects(Teacher teacher) {
        teacherSubjectService.removeAllSubjectsFromTeacher(teacher.getId());
        for (Subject subject : teacher.getSubjects()) {
            teacherSubjectService.addSubjectToTeacher(teacher.getId(), subject.getId());
        }
    }
}
