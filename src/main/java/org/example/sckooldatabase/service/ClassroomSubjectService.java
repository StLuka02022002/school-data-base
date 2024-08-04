package org.example.sckooldatabase.service;

import org.example.sckooldatabase.database.DataBase;
import org.example.sckooldatabase.object.Classroom;
import org.example.sckooldatabase.object.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassroomSubjectService {
    private DataBase dataBase;

    public ClassroomSubjectService() {
        this.dataBase = new DataBase();
    }

    public void addSubjectToClassroom(Long classroomId, Long subjectId) {
        String sql = "INSERT INTO classroom_subject (classroom_id, subject_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, classroomId);
            preparedStatement.setLong(2, subjectId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeSubjectFromClassroom(Long classroomId, Long subjectId) {
        String sql = "DELETE FROM classroom_subject WHERE classroom_id = ? AND subject_id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, classroomId);
            preparedStatement.setLong(2, subjectId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Subject> getSubjectsByClassroomId(Long classroomId) {
        String sql = "SELECT s.* FROM subjects s INNER JOIN classroom_subject cs ON s.id = cs.subject_id WHERE cs.classroom_id = ?";
        List<Subject> subjects = new ArrayList<>();
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, classroomId);
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

    public void removeAllSubjectsFromClassroom(Long classroomId) {
        String sql = "DELETE FROM classroom_subject WHERE classroom_id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, classroomId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Classroom> getClassroomsBySubjectId(Long subjectId) {
        String sql = "SELECT c.* FROM classrooms c INNER JOIN classroom_subject cs ON c.id = cs.classroom_id WHERE cs.subject_id = ?";
        List<Classroom> classrooms = new ArrayList<>();
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, subjectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                classrooms.add(extractClassroomFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classrooms;
    }

    private Classroom extractClassroomFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        int number = resultSet.getInt("number");
        int floor = resultSet.getInt("floor");
        int numberSeat = resultSet.getInt("number_seat");
        Classroom classroom = new Classroom(number, floor, numberSeat);
        classroom.setId(id);
        classroom.setSubjects(getSubjectsByClassroomId(id));
        return classroom;
    }
}
