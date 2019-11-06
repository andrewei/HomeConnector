package controller.tab;

import config.ConfigPath;
import controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import network.serial.SerialConnector;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Tab4Controller implements Initializable {

    private MainController mainController;
    private SerialConnector serialConnector;
    private ConfigPath configPath;
    private Stage stage;

    @FXML
    private Text textInfoDoorbellSong;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void init(MainController mainController) {
        this.mainController = mainController;
        this.serialConnector = mainController.serialConnector;
        this.configPath = mainController.configPath;
        this.stage = mainController.myStage;
        String activeDoorbellSong = configPath.readKey("doorbellSong");
        if (activeDoorbellSong != null) {
            textInfoDoorbellSong.setText(activeDoorbellSong);
        }
    }

    public void doorbellChoose(MouseEvent event) {
        if (serialConnector != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose doorbell song");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                configPath.storeSong("doorbellSong", file);
                String path = configPath.readKey("doorbellSong");
                textInfoDoorbellSong.setText(path);
            }
        } else {
            System.out.println("Serial connector is null, is the doorbell connected?");
        }
    }
}


