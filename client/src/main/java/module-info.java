module nop.homeconnector.maven {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires json.simple;

    opens nop.homeconnector.maven to javafx.fxml;
    exports nop.homeconnector.maven;
}