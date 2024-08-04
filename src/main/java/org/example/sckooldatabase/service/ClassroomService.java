package org.example.sckooldatabase.service;

import org.example.sckooldatabase.database.DataBase;
import org.example.sckooldatabase.object.Classroom;
import org.example.sckooldatabase.object.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassroomService {
    private DataBase dataBase;
    private ClassroomSubjectService classroomSubjectService;

    public ClassroomService() {
        this.dataBase = new DataBase();
        this.classroomSubjectService = new ClassroomSubjectService();
    }

    public Classroom addClassroom(Classroom classroom) {
        String sql = "INSERT INTO classrooms (number, floor, number_seat) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, classroom.getNumber());
            preparedStatement.setInt(2, classroom.getFloor());
            preparedStatement.setInt(3, classroom.getNumberSeat());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                classroom.setId(generatedKeys.getLong(1));
                updateClassroomSubjects(classroom);
                return classroom;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Classroom updateClassroom(Classroom classroom) {
        String sql = "UPDATE classrooms SET number = ?, floor = ?, number_seat = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setInt(1, classroom.getNumber());
            preparedStatement.setInt(2, classroom.getFloor());
            preparedStatement.setInt(3, classroom.getNumberSeat());
            preparedStatement.setLong(4, classroom.getId());
            preparedStatement.executeUpdate();
            updateClassroomSubjects(classroom);
            return classroom;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteClassroom(Long id) {
        String sql = "DELETE FROM classrooms WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            classroomSubjectService.removeAllSubjectsFromClassroom(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Classroom getClassroomById(Long id) {
        String sql = "SELECT * FROM classrooms WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractClassroomFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Classroom getClassroomByNumber(int number) {
        String sql = "SELECT * FROM classrooms WHERE number = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setInt(1, number);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractClassroomFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Classroom> getAllClassrooms() {
        String sql = "SELECT * FROM classrooms ORDER BY number";
        List<Classroom> classrooms = new ArrayList<>();
        try (Statement statement = dataBase.connect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                classrooms.add(extractClassroomFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classrooms;
    }

    public Classroom extractClassroomFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        int number = resultSet.getInt("number");
        int floor = resultSet.getInt("floor");
        int numberSeat = resultSet.getInt("number_seat");
        Classroom classroom = new Classroom(number, floor, numberSeat);
        classroom.setId(id);
        classroom.setSubjects(classroomSubjectService.getSubjectsByClassroomId(id));
        return classroom;
    }

    private void updateClassroomSubjects(Classroom classroom) {
        classroomSubjectService.removeAllSubjectsFromClassroom(classroom.getId());
        for (Subject subject : classroom.getSubjects()) {
            classroomSubjectService.addSubjectToClassroom(classroom.getId(), subject.getId());
        }
    }
}
