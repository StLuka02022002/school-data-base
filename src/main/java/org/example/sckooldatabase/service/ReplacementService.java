package org.example.sckooldatabase.service;

import org.example.sckooldatabase.database.DataBase;
import org.example.sckooldatabase.object.Lesson;
import org.example.sckooldatabase.object.Replacement;
import org.example.sckooldatabase.object.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReplacementService {

    private DataBase dataBase;
    private TeacherService teacherService;

    public ReplacementService() {
        this.dataBase = new DataBase();
        teacherService = new TeacherService();
    }

    // Method to add a new replacement
    public void addReplacement(Replacement replacement) {
        String sql = "INSERT INTO replacements (lesson_id, teacher_from, teacher_to) VALUES (?, ?, ?)";

        try (Connection connection = dataBase.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, replacement.getLessonId());
            preparedStatement.setLong(2, replacement.getTeacherFrom().getId());
            preparedStatement.setLong(3, replacement.getTeacherTo().getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating replacement failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    replacement.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating replacement failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update an existing replacement
    public void updateReplacement(Replacement replacement) {
        String sql = "UPDATE replacements SET lesson_id = ?, teacher_from = ?, teacher_to = ? WHERE id = ?";

        try (Connection connection = dataBase.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, replacement.getLessonId());
            preparedStatement.setLong(2, replacement.getTeacherFrom().getId());
            preparedStatement.setLong(3, replacement.getTeacherTo().getId());
            preparedStatement.setLong(4, replacement.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a replacement by ID
    public void deleteReplacement(Long replacementId) {
        String sql = "DELETE FROM replacements WHERE id = ?";

        try (Connection connection = dataBase.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, replacementId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get a replacement by ID
    public Replacement getReplacementById(Long replacementId) {
        String sql = "SELECT * FROM replacements WHERE id = ?";
        Replacement replacement = null;

        try (Connection connection = dataBase.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, replacementId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                replacement = extractReplacementFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return replacement;
    }

    public Replacement getReplacementByLesson(Lesson lesson) {
        String sql = "SELECT * FROM replacements WHERE lesson_id = ?";
        Replacement replacement = null;

        try (Connection connection = dataBase.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, lesson.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                replacement = extractReplacementFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return replacement;
    }

    // Method to get all replacements
    public List<Replacement> getAllReplacements() {
        String sql = "SELECT * FROM replacements";
        List<Replacement> replacements = new ArrayList<>();

        try (Connection connection = dataBase.connect();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                replacements.add(extractReplacementFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return replacements;
    }

    public int getReplacementCountByTeacherTo(Teacher teacherTo) {
        String sql = "SELECT COUNT(*) AS replacement_count FROM replacements WHERE teacher_to = ?";
        int replacementCount = 0;

        try (Connection connection = dataBase.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, teacherTo.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                replacementCount = resultSet.getInt("replacement_count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return replacementCount;
    }

    // Helper method to extract Replacement object from ResultSet
    private Replacement extractReplacementFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long lessonId = resultSet.getLong("lesson_id");
        Long teacherFromId = resultSet.getLong("teacher_from");
        Long teacherToId = resultSet.getLong("teacher_to");

        Teacher teacherFrom = teacherService.getTeacherById(teacherFromId);
        Teacher teacherTo = teacherService.getTeacherById(teacherToId);

        Replacement replacement = new Replacement(lessonId, teacherFrom, teacherTo);
        replacement.setId(id);

        return replacement;
    }

}
