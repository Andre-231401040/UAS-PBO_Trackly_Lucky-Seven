package org.example.trackly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.example.trackly.model.User;
import org.example.trackly.service.UserService;
import org.example.trackly.util.NavigationUtil;
import org.example.trackly.util.Session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SettingController {
    @FXML
    private Hyperlink taskList;
    @FXML
    private Hyperlink todo;
    @FXML
    private Hyperlink completed;
    @FXML
    private Hyperlink setting;
    @FXML
    private ImageView sidebarProfileImageView;
    @FXML
    private Label sidebarNameLabel;
    @FXML
    private ImageView profileImageView;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField usernameField;
    @FXML
    private Text emailField;

    private final UserService userService = new UserService();
    private File selectedImageFile;

    @FXML
    public void initialize() {
        User currentUser = Session.getInstance().getUser();
        if (currentUser != null) {
            sidebarNameLabel.setText(currentUser.getUsername());
            usernameField.setText(currentUser.getUsername());
            phoneNumberField.setText(currentUser.getPhoneNumber());
            emailField.setText(currentUser.getEmail());

            if (currentUser.getProfileImage() != null && !currentUser.getProfileImage().isEmpty()) {
                File imageFile = new File("user_data/profile/" + currentUser.getProfileImage());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    sidebarProfileImageView.setImage(image);
                    profileImageView.setImage(image);
                } else {
                    // fallback ke default
                    Image defaultImage = new Image(getClass().getResourceAsStream("/img/photo.jpg"));
                    sidebarProfileImageView.setImage(defaultImage);
                    profileImageView.setImage(defaultImage);
                }
            } else {
                Image defaultImage = new Image(getClass().getResourceAsStream("/img/photo.jpg"));
                sidebarProfileImageView.setImage(defaultImage);
                profileImageView.setImage(defaultImage);
            }
        }
    }

    @FXML
    public void handleUploadButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImageFile = file;

            Image image = new Image(file.toURI().toString());
            profileImageView.setImage(image);
        }
    }

    @FXML
    public void handleSaveChanges(ActionEvent event) {
        User currentUser = Session.getInstance().getUser();
        if (currentUser == null) return;

        // Update username & phone
        String newUsername = usernameField.getText();
        String newPhone = phoneNumberField.getText();

        currentUser.setUsername(newUsername);
        currentUser.setPhoneNumber(newPhone);

        // Simpan gambar kalau ada yang dipilih
        if (selectedImageFile != null) {
            try {
                String fileName = selectedImageFile.getName();
                File directory = new File("user_data/profile");
                if (!directory.exists()) {
                    directory.mkdirs(); // Buat folder jika belum ada
                }

                File dest = new File(directory, fileName);
                Files.copy(selectedImageFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                currentUser.setProfileImage(fileName); // hanya nama file
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to upload image.");
                return;
            }
        }

        boolean updated = userService.updateUser(currentUser);
        if (updated) {
            showAlert("Success", "Changes saved successfully.");
            initialize();
        } else {
            showAlert("Error", "Failed to save changes.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void navigationHandler(ActionEvent event) {
        NavigationUtil.handleNavigation(event, null, null, taskList, todo, completed, setting, null);
    }
}
