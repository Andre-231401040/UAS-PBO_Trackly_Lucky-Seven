package org.example.trackly.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import org.example.trackly.service.AuthService;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.example.trackly.model.User;
import org.example.trackly.util.NavigationUtil;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Hyperlink loginLink;

    private final AuthService authService = new AuthService();

    public void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Validasi field kosong
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Semua field wajib diisi!", Alert.AlertType.WARNING);
            return;
        }

        // Validasi format email yang lebih baik
        if (!email.matches("^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$")) {
            showAlert("Format email tidak valid!", Alert.AlertType.WARNING);
            return;
        }

        try {
            User user = new User(username, email, password, null, null);

            if (authService.userExists(user.getUsername(), user.getEmail())) {
                showAlert("Username atau email sudah terdaftar!", Alert.AlertType.WARNING);
                return;
            }

            boolean success = authService.register(user);
            if (success) {
                showAlert("Registrasi berhasil!", Alert.AlertType.INFORMATION);
                NavigationUtil.handleNavigation("login_view.fxml");
            } else {
                showAlert("Registrasi gagal! Silakan coba lagi.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Terjadi kesalahan: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void goToLogin(ActionEvent event) {
        NavigationUtil.handleNavigation(event, null, loginLink, null, null, null, null, null);
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
}
