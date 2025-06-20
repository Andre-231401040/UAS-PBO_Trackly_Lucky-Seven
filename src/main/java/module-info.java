module org.example.trackly {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;

    opens org.example.trackly to javafx.fxml;
    exports org.example.trackly;

    opens org.example.trackly.controller to javafx.fxml;
    exports org.example.trackly.controller;

    opens org.example.trackly.model to javafx.fxml;
    exports org.example.trackly.model;
}