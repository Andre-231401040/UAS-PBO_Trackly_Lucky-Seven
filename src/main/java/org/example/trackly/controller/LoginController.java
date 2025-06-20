package org.example.trackly.controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import org.example.trackly.model.User;
import org.example.trackly.service.AuthService;
import org.example.trackly.util.NavigationUtil;
import org.example.trackly.util.Session;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Hyperlink registerLink;

    private final AuthService authService = new AuthService();

    public void goToRegister(ActionEvent event) {
        NavigationUtil.handleNavigation(event, registerLink, null, null, null, null, null, null);
    }

    public void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email dan password tidak boleh kosong!", Alert.AlertType.ERROR);
            return;
        }

        try {
            User user = authService.login(email, password);
            if (user != null) {
                showAlert("Success", "Login berhasil!", Alert.AlertType.INFORMATION);
                Session.login(user);
                redirectToTaskList();
            } else {
                showAlert("Error", "Email atau password salah!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "Terjadi kesalahan: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void redirectToTaskList() {
        NavigationUtil.handleNavigation("task_list_view.fxml");
    }


    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}