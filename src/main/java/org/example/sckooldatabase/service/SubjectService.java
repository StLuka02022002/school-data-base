package org.example.sckooldatabase.service;

import org.example.sckooldatabase.database.DataBase;
import org.example.sckooldatabase.object.Subject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SubjectService {

    private DataBase dataBase;

    public SubjectService() {
        this.dataBase = new DataBase();
    }

    public Subject addSubject(Subject subject) {
        return addSubject(subject.getName(), subject.getDescription());
    }

    public Subject addSubject(String name, String description) {
        String sql = "INSERT INTO subjects (name, description) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Subject subject = new Subject(name, description);
                subject.setId(generatedKeys.getLong(1));
                return subject;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Subject updateSubject(Subject subject) {
        return updateSubject(subject.getId(), subject.getName(), subject.getDescription());
    }

    public Subject updateSubject(Long id, String name, String description) {
        String sql = "UPDATE subjects SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
            return new Subject(id, name, description);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteSubject(Long id) {
        String sql = "DELETE FROM subjects WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Subject getSubjectById(long id) {
        String sql = "SELECT * FROM subjects WHERE id = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                return new Subject(id, name, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Subject getSubjectByName(String name) {
        String sql = "SELECT * FROM subjects WHERE name = ?";
        try (PreparedStatement preparedStatement = dataBase.connect().prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                return new Subject(id, name, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Subject> getAllSubjects() {
        String sql = "SELECT * FROM subjects ORDER BY name";
        List<Subject> subjects = new ArrayList<>();
        try (Statement statement = dataBase.connect().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                subjects.add(extractSubjectFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public Subject extractSubjectFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        return new Subject(id, name, description);
    }

}
