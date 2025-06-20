package org.example.trackly;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Trackly");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void changeScene(String fxmlFile) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}