module main.c482 {
    requires javafx.controls;
    requires javafx.fxml;


    opens main.c482 to javafx.fxml;
    opens model to javafx.base;
    exports main.c482;
    exports model;
    exports controller;
    opens controller to javafx.fxml;
}