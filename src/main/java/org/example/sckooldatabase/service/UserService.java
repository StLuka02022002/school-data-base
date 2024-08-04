package org.example.sckooldatabase.service;

import org.example.sckooldatabase.data.Role;
import org.example.sckooldatabase.database.DataBase;
import org.example.sckooldatabase.object.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    private DataBase dataBase;

    public UserService() {
        this.dataBase = new DataBase();
    }

    public void addUser(User user) {
        String sql = "INSERT INTO users (login, password, solt, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataBase.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getSolt());
            statement.setString(4, user.getRole().name());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserByLogin(String login) {
        String sql = "SELECT id, login, password, solt, role FROM users WHERE login = ?";
        User user = null;

        try (Connection connection = dataBase.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                String solt = resultSet.getString("solt");
                Role role = Role.valueOf(resultSet.getString("role"));

                user = new User(login, password, solt, role);
                user.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Optional: Method to delete a user by login
    public void deleteUserByLogin(String login) {
        String sql = "DELETE FROM users WHERE login = ?";

        try (Connection connection = dataBase.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, login);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
