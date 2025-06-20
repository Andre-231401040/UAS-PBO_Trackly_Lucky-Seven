package org.example.trackly.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.trackly.model.Task;
import org.example.trackly.model.User;
import org.example.trackly.service.TaskService;
import org.example.trackly.util.NavigationUtil;
import org.example.trackly.util.Session;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CompletedController {
    @FXML
    protected Hyperlink taskList;
    @FXML
    protected Hyperlink todo;
    @FXML
    protected Hyperlink completed;
    @FXML
    protected Hyperlink setting;
    @FXML
    private ImageView sidebarProfileImageView;
    @FXML
    private Label sidebarNameLabel;
    @FXML
    private VBox completedListContainer;

    private final TaskService taskService = new TaskService();

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

        loadCompleted();
    }

    public void loadCompleted() {
        completedListContainer.getChildren().clear(); // bersihkan isi lama

        List<Task> tasks = taskService.getTasksByStatus("Completed");

        for (Task task : tasks) {
            VBox taskBox = new VBox();
            taskBox.setStyle("-fx-background-color: #F0FFF4; -fx-background-radius: 10;");
            taskBox.setPadding(new Insets(8));
            VBox.setMargin(taskBox, new Insets(0, 0, 20, 0));
            taskBox.setSpacing(20);
            taskBox.setPrefHeight(152);

            // Title
            Label title = new Label(task.getTitle());
            title.setStyle("-fx-text-fill: #10B981;");
            title.setPrefHeight(60);
            title.setFont(Font.font("Segoe UI Bold", 18));

            // Description
            Label desc = new Label(task.getDescription());
            desc.setWrapText(true);
            desc.setFont(Font.font("Segoe UI", 14));

            // Deadline
            HBox deadlineBox = new HBox(5);
            deadlineBox.setAlignment(Pos.CENTER_LEFT);
            deadlineBox.setPrefWidth(750);

            ImageView calendarIcon = new ImageView(new Image(getClass().getResourceAsStream("/img/calendar.png")));
            calendarIcon.setFitWidth(30);
            calendarIcon.setFitHeight(30);

            if(task.getDeadline() != null) {
                String formattedDeadline = task.getDeadline().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm"));
                Label deadlineLabel = new Label(formattedDeadline);
                deadlineLabel.setTextFill(Color.web("#9ca3af"));
                deadlineLabel.setFont(Font.font("Segoe UI Bold", 14));

                deadlineBox.getChildren().addAll(calendarIcon, deadlineLabel);
            }

            // Status Icon
            Hyperlink uncompleteLink = new Hyperlink();
            uncompleteLink.setStyle("-fx-border-color: transparent;");
            ImageView undoIcon = new ImageView(new Image(getClass().getResourceAsStream("/img/undo_1.png")));
            undoIcon.setFitWidth(40);
            undoIcon.setFitHeight(40);
            uncompleteLink.setGraphic(undoIcon);
            uncompleteLink.setPrefWidth(200);
            uncompleteLink.setAlignment(Pos.TOP_RIGHT);
            uncompleteLink.setOnAction(event -> {
                taskService.updateTaskStatus(task.getId(), "To Do");
                loadCompleted();
            });

            HBox actionRow = new HBox(deadlineBox, uncompleteLink);
            HBox.setHgrow(deadlineBox, Priority.ALWAYS);

            // Add to task box
            taskBox.getChildren().addAll(title, desc, actionRow);

            // Tambahkan ke container utama
            completedListContainer.getChildren().add(taskBox);
        }
    }

    public void navigationHandler(ActionEvent event) {
        NavigationUtil.handleNavigation(event, null, null, taskList, todo, completed, setting, null);
    }
}
