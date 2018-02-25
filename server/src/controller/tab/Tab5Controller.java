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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import network.tcpip.speakers.NetworkSpeakersController;

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
    private NetworkSpeakersController networkSpeakersController;
    private Mp3Player player;
    private ConfigPath configPath;
    private List<File> listOfFiles;
    private ListProperty<File> listProperty = new SimpleListProperty<>();
    private Thread thread;
    private Stage stage;
    private static boolean isPlaying;
    private ObservableList<File> observableList;

    @FXML private TextField textSearchFilter;
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
        this.networkSpeakersController = mainController.networkSpeakersController;
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
                setVolume(volume);
            }
        });
        getMp3DataThread();
        isPlaying = false;

        textSearchFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                search(oldValue, newValue);
            }
        });
    }


    public void search( String oldVal, String newVal) {
        if(oldVal == newVal){
            System.out.println("same value in search, returning");
            return;
        }
        if(newVal.length() == 0){
            observableList.clear();
            observableList.setAll(listOfFiles);
        }
        else {
            System.out.println("OldValue: " + oldVal + " newValue: " + newVal );
            observableList.clear();
            for (int i = 0; i < listOfFiles.size(); i++){
                if(listOfFiles.get(i).toString().toLowerCase().contains(newVal.toLowerCase())){
                    observableList.add(listOfFiles.get(i));
                }
            }
        }
    }

    private void setVolume(double volume){
        networkSpeakersController.setVolume(volume);
    }

    public void setVolumeAndUpdateSlider(double volume){
        slide_vol.setValue(volume*slide_vol.getMax());
        //setVolume(volume);
    }

    public void btn_next_click(MouseEvent event){
        Random random = new Random();
        btn_play.setText("Pause");
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
        networkSpeakersController.playNewSong(file);
    }

    public void btn_stop(MouseEvent event){
        player.stop();
        btn_play.setText("Play");
        networkSpeakersController.stopSong();
    }

    public void btn_play(MouseEvent event){
        if(player != null){
            if(!player.isPlaying()){
                player.play();
                networkSpeakersController.playSong();
                btn_play.setText("Pause");
            }
            else{
                player.pause();
                networkSpeakersController.pauseSong();
                btn_play.setText("Play");
            }
        }
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
            observableList = FXCollections.observableArrayList(listOfFiles);
            listProperty.set(observableList);
        }
    }

    public void playSong(File file){
        String selected = "file:///" + file;
        tf_currentSong.setText(file.getName());
        selected = selected.replaceAll(" ", "%20");
        selected = selected.replaceAll("\\\\", "/");
        System.out.println("clicked on " + selected);
        btn_play.setText("Pause");
        player.play(selected);
        networkSpeakersController.playNewSong(selected);
    }

    public void setSongTime(MouseEvent e){
        networkSpeakersController.setCurrentTime(slide_time.getValue());
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
                    //TODO fix better error message
                    System.out.println("Error in mp3UpdateyThread");
                }
            }
        }
        thread = new Thread(new mp3UpdateyThread());
        thread.start();
    }
}
