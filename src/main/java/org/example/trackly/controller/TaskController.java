package org.example.trackly.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.trackly.model.Task;
import org.example.trackly.model.User;
import org.example.trackly.service.TaskService;
import org.example.trackly.util.NavigationUtil;
import org.example.trackly.util.Session;

import java.io.File;
import java.util.List;

public class TaskController {
    @FXML
    protected Hyperlink taskList;
    @FXML
    protected Hyperlink todo;
    @FXML
    protected Hyperlink completed;
    @FXML
    protected Hyperlink setting;
    @FXML
    protected ImageView sidebarProfileImageView;
    @FXML
    protected Label sidebarNameLabel;
    @FXML
    private Button addTask;
    @FXML
    private VBox taskListContainer;

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

        loadTask();
    }

    public void loadTask() {
        ObservableList<Node> allChildren = taskListContainer.getChildren();

        // Simpan header (asumsi index 0 adalah header)
        Node header = allChildren.get(0);

        List<Task> tasks = taskService.getTasks();
        taskListContainer.getChildren().clear();
        taskListContainer.getChildren().add(header);

        int counter = 1;
        for (Task task : tasks) {
            HBox taskRow = new HBox();
            taskRow.setSpacing(10);
            taskRow.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: #F9FAFB;");
            taskRow.setAlignment(Pos.CENTER_LEFT);
            taskRow.setPadding(new Insets(12, 16, 12, 16));

            // No
            Label noLabel = new Label(String.valueOf(counter++));
            noLabel.setPrefWidth(140);
            noLabel.setFont(Font.font("Segoe UI", 16));

            // Title
            Label titleLabel = new Label(task.getTitle());
            titleLabel.setPrefWidth(450);
            titleLabel.setFont(Font.font("Segoe UI", 16));

            // Status
            HBox status = new HBox();
            status.setPrefWidth(230);
            status.setAlignment(Pos.CENTER_LEFT);
            Label statusLabel = new Label(task.getStatus());
            statusLabel.setTextFill(Color.WHITE);
            statusLabel.setFont(Font.font("Segoe UI Bold", 16));
            statusLabel.setPadding(new Insets(4, 10, 4, 10));
            statusLabel.setStyle(getStatusStyle(task.getStatus()));
            status.getChildren().add(statusLabel);

            // Action buttons
            HBox actions = new HBox();
            actions.setAlignment(Pos.CENTER_RIGHT);
            actions.setSpacing(8);

            Hyperlink editLink = new Hyperlink();
            editLink.setStyle("-fx-border-color: transparent;");
            ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/img/pencil.png")));
            editIcon.setFitWidth(32);
            editIcon.setFitHeight(32);
            editLink.setGraphic(editIcon);
            editLink.setOnAction(event -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/trackly/edit_task_view.fxml"));
                    Parent root = fxmlLoader.load();

                    EditTaskController controller = fxmlLoader.getController();
                    controller.setTaskToEdit(task);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setMaximized(true);
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch(Exception e) {
                    System.err.println("Failed to load application.");
                }
            });

            Hyperlink deleteLink = new Hyperlink();
            deleteLink.setStyle("-fx-border-color: transparent;");
            ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/img/trash.png")));
            deleteIcon.setFitWidth(40);
            deleteIcon.setFitHeight(40);
            deleteLink.setGraphic(deleteIcon);
            deleteLink.setOnAction(event -> {
                TaskService taskService = new TaskService();
                boolean success = taskService.deleteTaskNotification(task);

                if (success) {
                    showAlert("Task deleted successfully!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Delete Task Failed.", Alert.AlertType.WARNING);
                }
            });

            actions.getChildren().addAll(editLink, deleteLink);

            taskRow.getChildren().addAll(noLabel, titleLabel, status, actions);
            taskListContainer.getChildren().add(taskRow);
        }
    }

    private String getStatusStyle(String status) {
        return switch (status.toLowerCase()) {
            case "completed" -> "-fx-background-color: #10B981; -fx-background-radius: 4;";
            case "to do" -> "-fx-background-color: #F59E0B; -fx-background-radius: 4;";
            default -> "-fx-background-color: #6B7280; -fx-background-radius: 4;";
        };
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
    public void navigationHandler(ActionEvent event) {
        NavigationUtil.handleNavigation(event, null, null, taskList, todo, completed, setting, addTask);
    }
}
