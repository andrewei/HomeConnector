package nop.homeconnector.maven;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/nop/homeconnector/main.fxml"));
        Parent root = (Parent) loader.load();
        MainController controller = (MainController) loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("client");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
