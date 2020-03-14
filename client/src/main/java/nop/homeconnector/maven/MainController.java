package nop.homeconnector.maven;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public MediaView mediaView;

    public Network network;

    public Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new Network(this);
        try {
            network.recive();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void mouse_fullscreen(MouseEvent event) {
        if (stage.isFullScreen())
            stage.setFullScreen(false);
        else {
            stage.setFullScreen(true);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
