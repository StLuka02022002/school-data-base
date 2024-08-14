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
        WindowUtil.openWindowOnThisScene("fxml/authorization.fxml", ((Node) event.getSource()).getScene());
    }

    @FXML
    void signIn(ActionEvent event) {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        if (isValid(login, password)) {
            String message = "Логин или пароль введены неправильно";
            User user = userService.getUserByLogin(login);
            if (user == null) {
                setError(message);
                return;
            }
            String newPassword = PasswordUtil.hashString(password + user.getSolt());
            if (!newPassword.equals(user.getPassword())) {
                setError(message);
                return;
            }
            Configuration.setRole(user.getRole());
            WindowUtil.openWindowOnThisScene("fxml/main-window.fxml", ((Node) event.getSource()).getScene());
        } else {
            setError(Configuration.LOGIN_AND_PASSWORD_MESSAGE);
        }
    }

    public static boolean isValid(String login, String password) {
        if (login == null || login.length() < Configuration.LENGTH_LOGIN_AND_PASSWORD
                || password == null || password.length() < Configuration.LENGTH_LOGIN_AND_PASSWORD) {
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
