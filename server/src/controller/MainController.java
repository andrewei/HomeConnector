package controller;

import aplication.Clock;
import aplication.Mp3Player;
import config.ConfigPath;
import controller.tab.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.serial.FindPort;
import network.serial.SerialConnector;
import network.tcpip.NetworkController;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    private ColorAdjust colorAdjust;
    private Timer clockTimerObject;

    @FXML private Slider slide_brightness;
    @FXML private SplitPane rootObj;

    public static NetworkController networkController;
    public static SerialConnector serialConnector;
    public static ConfigPath configPath;
    public static Mp3Player player;
    public static Clock clock;
    public static Stage myStage;

    @FXML public Text str_date;
    @FXML public Text str_hour;
    @FXML public Text str_minute;
    @FXML public Tab1Controller tab1Controller;
    @FXML public Tab2Controller1 tab2Controller;
    @FXML public Tab3Controller tab3Controller;
    @FXML public Tab4Controller tab4Controller;
    @FXML public Tab5Controller tab5Controller;
    @FXML public Tab6Controller tab6Controller;

    @FXML public void initialize(URL location, ResourceBundle resources) {

        configPath = new ConfigPath();
        player = new Mp3Player(this);
        networkController = new NetworkController();
        initSerial();
        clockTimerObject = new Timer();
        clockTimerObject.schedule(clock = new Clock(this), 0, 1000);

        colorAdjust = new ColorAdjust();
        slide_brightness.setMin(-.85);
        slide_brightness.setMax(0);
        slide_brightness.setValue(0);
        colorAdjust.setBrightness(0);
        colorAdjust.setContrast(0);
        rootObj.setEffect(colorAdjust);

        tab1Controller.init(this);
        tab2Controller.init(this);
        tab3Controller.init(this);
        tab4Controller.init(this);
        tab5Controller.init(this);
        tab6Controller.init(this);
    }

    public void initSerial() {
        try {
            FindPort findPort = new FindPort();
            List availablePorts = findPort.availablePorts();
            for(int i = 0; i < availablePorts.size(); i++){
                serialConnector = new SerialConnector(this);
                serialConnector.connect("" + availablePorts.get(i));
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("could not open serial connection");
        }
    }

    public void btn_fullscreen(MouseEvent event){
        if(myStage.isFullScreen())
            myStage.setFullScreen(false);
        else{
            myStage.setFullScreen(true);
        }
    }

    public void setStage(Stage stage) {
        myStage = stage;
    }

    public void setBrightness(MouseEvent event){
        colorAdjust.setBrightness(slide_brightness.getValue());
    }

    public void testDeleteMe(){
        System.out.println("THIS IS WORKING: TEST OK");
    }
}