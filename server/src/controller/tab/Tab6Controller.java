package controller.tab;

import aplication.Clock;
import config.ConfigPath;
import controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import network.serial.SerialConnector;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by andreas on 3/15/2016.
 */
public class Tab6Controller implements Initializable {

    private MainController mainController;
    private SerialConnector serialConnector;
    private ConfigPath configPath;
    private Stage stage;
    private Clock clock;

    @FXML private Text textInfoAlarmSong;
    @FXML private TextField alarmHour;
    @FXML private TextField alarmMinute;
    @FXML private ToggleButton alarmToggle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void init(MainController mainController){
        this.mainController = mainController;
        this.serialConnector = mainController.serialConnector;
        this.configPath = mainController.configPath;
        this.stage = mainController.myStage;
        this.clock = mainController.clock;

        String activeAlarmSong = configPath.readKey("alarmSong");
        if(activeAlarmSong != null){
            textInfoAlarmSong.setText(activeAlarmSong);
        }
    }

    public void alarmChoose(MouseEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose alarm song");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            configPath.storeSong("alarmSong", file);
            String path = configPath.readKey("doorbellSong");
            textInfoAlarmSong.setText(path);
        }
    }

    public void setAlarm(MouseEvent event){
        System.out.println("SetAlarm is running and it is " + alarmToggle.isSelected());
        int hour, minute;
        if(alarmHour.getText().length() == 0){hour = 0;}
        else{hour = Integer.parseInt(alarmHour.getText());}
        if(alarmMinute.getText().length() == 0){minute = 0;}
        else{minute = Integer.parseInt(alarmMinute.getText());}
        clock.setAlarm(hour, minute, alarmToggle.isSelected());
    }
}
