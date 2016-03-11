package aplication;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.serial.FindPort;
import network.serial.SerialConnector;
import network.tcpip.NetworkController;

import java.io.File;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable{

    public ListProperty<File> listProperty = new SimpleListProperty<>();
    public SerialConnector serialConnector;

    private Stage myStage;
    private ColorAdjust colorAdjust;
    private static NetworkController networkController = new NetworkController();
    private Thread thread;
    private Timer clock;
    private List<File> listOfFiles;
    private static Mp3Player player;

    @FXML
    public Text str_date;
    @FXML
    public Text str_hour;
    @FXML
    public Text str_minute;

    @FXML
    private ImageView btn_pwr;
    @FXML
    private ImageView btn_vup;
    @FXML
    private ImageView btn_vdown;
    @FXML
    private ImageView btn_source;
    @FXML
    private ListView list_music;
    @FXML
    private Slider slide_vol;
    @FXML
    private Slider slide_time;
    @FXML
    private Slider slide_brightness;
    @FXML
    private Button btn_stop;
    @FXML
    private Button btn_play;
    @FXML
    private TextField tf_currentSong;
    @FXML
    private SplitPane rootObj;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSerial();
        clock = new Timer();
        clock.schedule(new Clock(this), 0, 1000);
        initMp3Player();
        //setSound initial startvalue
        slide_vol.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double volume = (double) newValue * .01;
                System.out.println("new volume :" + volume);
                networkController.setVolume(volume);
            }
        });
        colorAdjust = new ColorAdjust();
        slide_brightness.setMin(-.85);
        slide_brightness.setMax(0);
        slide_brightness.setValue(0);
        colorAdjust.setBrightness(0);
        colorAdjust.setContrast(0);
        rootObj.setEffect(colorAdjust);
        //gets info from mp3Player, like where in the song are you now
        getMp3DataThread();
    }

    public void setStage(Stage stage) {
        myStage = stage;
    }

    public void btn_stop(MouseEvent event){
        player.stop();
        networkController.stopSong();
    }

    public void btn_play(MouseEvent event){
        player.play();
        networkController.playSong();
    }

    public void btn_next_click(MouseEvent event){
        Random random = new Random();
        int randInt = random.nextInt(listProperty.getSize());
        File file = listProperty.get(randInt);
        playSong(file);
    }

    public void initMp3Player() {
        LocalDatabase db = new LocalDatabase();
        //listOfFiles = db.getFolderItems("\\\\FENRIZ-PC\\mp32");
        listOfFiles = db.getFolderItems("f:\\mp3");
        list_music.itemsProperty().bind(listProperty);
        listProperty.set(FXCollections.observableArrayList(listOfFiles));
        player = new Mp3Player(this);
        slide_vol.adjustValue(50);
    }

    public void playSong(MouseEvent event){
        File file = (File)list_music.getSelectionModel().getSelectedItem();
        playSong(file);
    }

    public void playSong(String file){
        player.play(file);
        networkController.playNewSong(file);
    }

    public void playSong(File file){
        String selected = "file:///" + file;//list_music.getSelectionModel().getSelectedItem();
        tf_currentSong.setText(file.getName());
        selected = selected.replaceAll(" ", "%20");
        selected = selected.replaceAll("\\\\", "/");
        System.out.println("clicked on " + selected);
        player.play(selected);
        networkController.playNewSong(selected);
    }

    public void btn_fullscreen(MouseEvent event){
        if(myStage.isFullScreen())
            myStage.setFullScreen(false);
        else{
            myStage.setFullScreen(true);
        }
    }

    public void initSerial() {
        try {
            FindPort findPort = new FindPort();
            List availablePorts = findPort.availablePorts();
            //TODO test if this code works with remote and doorbell, fix exeption, add test
            for(int i = 0; i < availablePorts.size(); i++){
                serialConnector = new SerialConnector(this);
                serialConnector.connect("" + availablePorts.get(i));
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("could not open serial connection");
        }
    }

    public void setBrightness(MouseEvent event){
        colorAdjust.setBrightness(slide_brightness.getValue());
    }

    public void btn_pwr_click(MouseEvent event){
        serialConnector.setWrite(49);
        ImageView obj = (ImageView) event.getSource();
        obj.setStyle("-fx-text-fill: #c4d8de;");
    }
    public void btn_vdown_click(MouseEvent event){
        serialConnector.setWrite(50);
        ImageView obj = (ImageView) event.getSource();
        obj.setStyle("-fx-text-fill: #c4d8de;");
    }
    public void btn_vup_click(MouseEvent event){
        serialConnector.setWrite(51);
        ImageView obj = (ImageView) event.getSource();
        obj.setStyle("-fx-text-fill: #c4d8de;");
    }
    public void btn_source_click(MouseEvent event){
        serialConnector.setWrite(52);
        ImageView obj = (ImageView) event.getSource();
        obj.setStyle("-fx-text-fill: #c4d8de;");
    }
    public void resetColor(MouseEvent event){
        System.out.println("Is this running");
        ImageView obj = (ImageView) event.getSource();
        obj.setStyle("-fx-text-fill: #000000;");
    }

    public void setSongTime(MouseEvent e){
        networkController.setCurrentTime(slide_time.getValue());
        player.setCurrentTime((int) slide_time.getValue());
        System.out.println("Setting time : " + (int) slide_time.getValue());
    }

    public void getMp3DataThread() {

        class mp3UpdateyThread implements Runnable {

            public void run(){
                try{
                    while(true){
                        thread.sleep(100);
                        slide_time.setMin(0);
                        slide_time.setMax(player.getTotalDuration());
                        slide_time.setValue(player.getCurrentTime());
                    }
                }
                catch (Exception e){
                    System.out.println("Error in sending json object, fix error msg latewr");
                }
            }
        }
        thread = new Thread(new mp3UpdateyThread());
        thread.start();
    }
}