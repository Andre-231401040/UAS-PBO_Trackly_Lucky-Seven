package org.example.trackly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import org.example.trackly.model.User;
import org.example.trackly.service.TaskService;
import org.example.trackly.util.NavigationUtil;
import org.example.trackly.util.Session;

import java.io.File;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddTaskController extends TaskController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker deadlineDatePicker;
    @FXML
    private TextField hourField;
    @FXML
    private TextField minuteField;
    @FXML
    private RadioButton withDeadlineRadio;
    @FXML
    private RadioButton noDeadlineRadio;
    @FXML
    private HBox deadlineContainer;

    @FXML
    public void initialize() {
        User currentUser = Session.getInstance().getUser();
        if(currentUser != null) {
            sidebarNameLabel.setText(currentUser.getUsername());

            if (currentUser.getProfileImage() != null && !currentUser.getProfileImage().isEmpty()) {
                File imageFile = new File("user_data/profile/" + currentUser.getProfileImage());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    sidebarProfileImageView.setImage(image);
                } else {
                    // fallback ke default
                    Image defaultImage = new Image(getClass().getResourceAsStream("/img/photo.jpg"));
                    sidebarProfileImageView.setImage(defaultImage);
                }
            } else {
                Image defaultImage = new Image(getClass().getResourceAsStream("/img/photo.jpg"));
                sidebarProfileImageView.setImage(defaultImage);
            }
        }

        ToggleGroup toggleGroup = new ToggleGroup();
        withDeadlineRadio.setToggleGroup(toggleGroup);
        noDeadlineRadio.setToggleGroup(toggleGroup);

        withDeadlineRadio.setSelected(true);
        updateDeadlineVisibility();

        withDeadlineRadio.setOnAction(event -> updateDeadlineVisibility());
        noDeadlineRadio.setOnAction(event -> updateDeadlineVisibility());
    }

    public void updateDeadlineVisibility() {
        boolean isWithDeadlineRadio = withDeadlineRadio.isSelected();
        deadlineContainer.setVisible(isWithDeadlineRadio);
        deadlineContainer.setManaged(isWithDeadlineRadio);
    }

    public void handleAddTask(ActionEvent event) {
        String title = titleField.getText();
        String description = descriptionField.getText();
        boolean withDeadline = withDeadlineRadio.isSelected();
        Timestamp deadline = withDeadline ? getDeadlineTimestamp() : null;
        String status = "To Do";

        boolean success;
        TaskService taskService = new TaskService();
        if(withDeadline && deadline != null) {
            success = taskService.addTask(title, description, withDeadline, deadline, status);
        } else {
            success = taskService.addTask(title, description, false, null, status);
        }

        if(success) {
            NavigationUtil.handleNavigation("task_list_view.fxml");
        } else {
            System.err.println("Failed to add new task.");
        }
    }

    private Timestamp getDeadlineTimestamp() {
        if (deadlineDatePicker.getValue() == null ||
                hourField.getText().isEmpty() || minuteField.getText().isEmpty()) {
            return null;
        }

        try {
            LocalDate date = deadlineDatePicker.getValue();
            int hour = Integer.parseInt(hourField.getText());
            int minute = Integer.parseInt(minuteField.getText());

            LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, minute));
            return Timestamp.valueOf(dateTime);
        } catch (NumberFormatException | DateTimeException e) {
            e.printStackTrace();
            return null;
        }
    }
}
