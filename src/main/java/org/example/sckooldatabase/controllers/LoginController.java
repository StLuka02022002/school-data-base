package org.example.sckooldatabase.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.sckooldatabase.data.Configuration;
import org.example.sckooldatabase.object.User;
import org.example.sckooldatabase.service.UserService;
import org.example.sckooldatabase.utils.PasswordUtil;
import org.example.sckooldatabase.utils.WindowUtil;

public class LoginController {

    @FXML
    private Label labelError;

    @FXML
    private TextField textFieldLogin;

    @FXML
    private PasswordField textFieldPassword;

    private UserService userService;

    @FXML
    public void initialize() {
        textFieldLogin.textProperty().addListener((observable, oldValue, newValue) ->
                clearError()
        );
        userService = new UserService();
    }

    @FXML
    void logIn(ActionEvent event) {
        WindowUtil.openWindow("fxml/authorization.fxml", ((Node) event.getSource()).getScene());
    }

    @FXML
    void signIn(ActionEvent event) {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        if (isValid(login, password)) {
            User user = userService.getUserByLogin(login);
            if (user == null) {
                setError("Логин или пароль введены неправильно");
                return;
            }
            String newPassword = PasswordUtil.hashString(password + user.getSolt());
            if (!newPassword.equals(user.getPassword())) {
                setError("Логин или пароль введены неправильно");
                return;
            }
            Configuration.setRole(user.getRole());
            WindowUtil.openWindow("fxml/main-window.fxml", ((Node) event.getSource()).getScene());
        } else {
            setError("Длина логина и пароля должна быть не менее 6 символов");
        }
    }

    private boolean isValid(String login, String password) {
        if (login == null || login.length() < 6
                || password == null || password.length() < 6) {
            return false;
        }
        return true;
    }

    private void clearError() {
        labelError.setText(null);
        labelError.setVisible(false);
    }

    private void setError(String error) {
        labelError.setText(error);
        labelError.setVisible(true);
    }

}
