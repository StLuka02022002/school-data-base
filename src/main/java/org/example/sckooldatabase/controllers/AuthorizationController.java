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
import org.example.sckooldatabase.data.Configuration;
import org.example.sckooldatabase.data.Role;
import org.example.sckooldatabase.object.User;
import org.example.sckooldatabase.service.UserService;
import org.example.sckooldatabase.utils.PasswordUtil;
import org.example.sckooldatabase.utils.WindowUtil;

import java.util.List;

public class AuthorizationController {

    @FXML
    private ComboBox<Role> comboBoxRole;

    @FXML
    private Label labelError;

    @FXML
    private TextField textFieldLogin;

    @FXML
    private PasswordField textFieldPassword;
    private UserService userService;

    @FXML
    void initialize() {
        comboBoxRole.setItems(FXCollections.observableList(List.of(Role.values())));
        comboBoxRole.setValue(Role.USER);
        textFieldLogin.textProperty().addListener((observable, oldValue, newValue) ->
                clearError()
        );
        userService = new UserService();
    }

    @FXML
    void logIn(ActionEvent event) {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        if (LoginController.isValid(login, password)) {
            User user = userService.getUserByLogin(login);
            if (user != null) {
                setError("Пользователь с таким логином уже существует");
                return;
            }
            Role role = comboBoxRole.getValue();
            if (role == null) {
                setError("Выберите роль");
                return;
            }
            authorize(login, password, role);
        } else {
            setError(Configuration.LOGIN_AND_PASSWORD_MESSAGE);
        }

    }

    public void authorize(String login, String password, Role role) {
        String solt = PasswordUtil.generateRandomString(Configuration.SOLT_SIZE);
        String newPassword = PasswordUtil.hashString(password + solt);
        User user = new User(login, newPassword, solt, role);
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
