package org.example.sckooldatabase.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.example.sckooldatabase.data.Role;
import org.example.sckooldatabase.object.User;
import org.example.sckooldatabase.service.UserService;
import org.example.sckooldatabase.utils.PasswordUtil;
import org.example.sckooldatabase.utils.WindowUtil;

public class AuthorizationController {

    @FXML
    private ComboBox<String> comboBoxRole;

    @FXML
    private Label labelError;

    @FXML
    private TextField textFieldLogin;

    @FXML
    private PasswordField textFieldPassword;
    private UserService userService;

    @FXML
    void initialize() {
        comboBoxRole.setItems(FXCollections.observableList(Role.getRoles()));
        textFieldLogin.textProperty().addListener((observable, oldValue, newValue) ->
                clearError()
        );
        userService = new UserService();
    }

    @FXML
    void logIn(ActionEvent event) {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        if (isValid(login, password)) {
            User user = userService.getUserByLogin(login);
            if (user != null) {
                setError("Пользователь с таким логином уже существует");
                return;
            }
            String role = comboBoxRole.getValue();
            if (role == null) {
                setError("Выберите роль");
                return;
            }
            authorize(login, password, role);
        } else {
            setError("Длина логина и пароля должна быть не менее 6 символов");
        }

    }

    public void authorize(String login, String password, String role) {
        String solt = PasswordUtil.generateRandomString(5);
        String newPassword = PasswordUtil.hashString(password + solt);
        User user = new User(login, newPassword, solt, Role.getRole(role));
        authorize(user);
    }

    public void authorize(User user) {
        userService.addUser(user);
        textFieldLogin.setText(null);
        textFieldPassword.setText(null);
        comboBoxRole.setValue(null);
        labelError.setText("Авторизация успешна");
        labelError.setTextFill(Color.BLUE);
        labelError.setVisible(true);
    }

    @FXML
    public void signIn(ActionEvent event) {
        WindowUtil.openWindow("fxml/login-window.fxml", ((Node) event.getSource()).getScene());
    }


    private boolean isValid(String login, String password) {
        if (login == null || login.length() < 6
                || password == null || password.length() < 6) {
            return false;
        }
        return true;
    }

    private void clearError() {
        labelError.setTextFill(Color.RED);
        labelError.setText(null);
        labelError.setVisible(false);
    }

    private void setError(String error) {
        labelError.setText(error);
        labelError.setVisible(true);
    }

}
