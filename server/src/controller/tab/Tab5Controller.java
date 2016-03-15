package controller.tab;

import aplication.LocalDatabase;
import aplication.Mp3Player;
import config.ConfigPath;
import controller.MainController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import network.tcpip.NetworkController;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by andreas on 3/15/2016.
 */
public class Tab5Controller implements Initializable {

    private MainController mainController;
    private NetworkController networkController;
    private Mp3Player player;
    private ConfigPath configPath;
    private List<File> listOfFiles;
    private ListProperty<File> listProperty = new SimpleListProperty<>();
    private Thread thread;
    private Stage stage;

    @FXML private Button btn_stop;
    @FXML private Button btn_play;
    @FXML private Text textInfoRootFolder;
    @FXML private ListView list_music;
    @FXML private Slider slide_vol;
    @FXML private Slider slide_time;
    @FXML private TextField tf_currentSong;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void init(MainController mainController){
        this.mainController = mainController;
        this.configPath = mainController.configPath;
        this.player = mainController.player;
        this.networkController = mainController.networkController;
        this.stage = mainController.myStage;
        slide_vol.adjustValue(50);
        String activeRootFolder = configPath.readKey("rootFolder");
        if(activeRootFolder != null){
            textInfoRootFolder.setText(activeRootFolder);
        }
        slide_vol.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double volume = (double) newValue * .01;
                System.out.println("new volume :" + volume);
                networkController.setVolume(volume);
            }
        });
        getMp3DataThread();
    }

    public void btn_next_click(MouseEvent event){
        Random random = new Random();
        int randInt = random.nextInt(listProperty.getSize());
        File file = listProperty.get(randInt);
        playSong(file);
    }

    public void playSong(MouseEvent event){
        File file = (File)list_music.getSelectionModel().getSelectedItem();
        playSong(file);
    }

    public void playSong(String file){
        player.play(file);
        networkController.playNewSong(file);
    }

    public void btn_stop(MouseEvent event){
        player.stop();
        networkController.stopSong();
    }

    public void btn_play(MouseEvent event){
        player.play();
        networkController.playSong();
    }

    public void rootImport(MouseEvent event){
        initMp3Player();
    }

    public void rootChoose(MouseEvent event){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Resource File");
        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            configPath.storeDirectory("rootFolder", file);
            initMp3Player();
        }
    }

    public void initMp3Player() {
        String activeRootFolder = configPath.readKey("rootFolder");
        if(activeRootFolder != null){
            textInfoRootFolder.setText(activeRootFolder);
            LocalDatabase db = new LocalDatabase();
            listOfFiles = db.getFolderItems(activeRootFolder);
            list_music.itemsProperty().bind(listProperty);
            listProperty.set(FXCollections.observableArrayList(listOfFiles));
        }
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
                    System.out.println("Error in sending json object, fix error msg later");
                }
            }
        }
        thread = new Thread(new mp3UpdateyThread());
        thread.start();
    }
}
