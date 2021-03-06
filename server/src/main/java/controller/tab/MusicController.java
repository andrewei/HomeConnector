package controller.tab;

import aplication.LocalDatabase;
import aplication.Mp3Player;
import config.ConfigPath;
import controller.MainController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import network.tcpip.speakers.SpeakersController;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Stack;

public class MusicController implements Initializable {

    private MainController mainController;
    private SpeakersController networkSpeakersController;
    public Mp3Player player;
    private ConfigPath configPath;
    private List<File> listOfFiles;
    private ListProperty<File> listProperty = new SimpleListProperty<>();
    private ListProperty<File> queueProperty = new SimpleListProperty<>();
    private Thread thread;
    private Stage stage;
    private static boolean isPlaying;
    private ObservableList<File> observableList;
    private ObservableList<File> observableQueue;
    private Stack<File> history;
    private Stack<File> queue;

    @FXML
    private TextField textSearchFilter;
    @FXML
    private Button btn_play;
    @FXML
    private Text textInfoRootFolder;
    @FXML
    private ListView list_music;
    @FXML
    private ListView list_queue;
    @FXML
    private Slider slide_vol;
    @FXML
    private Slider slide_time;
    @FXML
    private TextField tf_currentSong;
    @FXML
    private Text text_songs_added;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void init(MainController mainController) {
        this.mainController = mainController;
        this.configPath = mainController.configPath;
        this.player = mainController.player;
        this.networkSpeakersController = mainController.networkSpeakersController;
        this.stage = mainController.myStage;
        slide_vol.adjustValue(50);
        String activeRootFolder = configPath.readKey("rootFolder");
        if (activeRootFolder != null) {
            textInfoRootFolder.setText(activeRootFolder);
        }
        slide_vol.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = (double) newValue * .01;
            System.out.println("new volume :" + volume);
            setVolume(volume);
        });
        getMp3DataThread();
        isPlaying = false;
        textSearchFilter.textProperty().addListener((observable, oldValue, newValue) -> search(oldValue, newValue));
        this.history = new Stack<>();
        this.queue = new Stack<>();
    }

    public void search(String oldVal, String newVal) {
        if (oldVal == newVal) {
            System.out.println("same value in search, returning");
            return;
        }
        if (newVal.length() == 0) {
            observableList.clear();
            observableList.setAll(listOfFiles);
        } else {
            System.out.println("OldValue: " + oldVal + " newValue: " + newVal);
            observableList.clear();
            for (int i = 0; i < listOfFiles.size(); i++) {
                if (listOfFiles.get(i).toString().toLowerCase().contains(newVal.toLowerCase())) {
                    observableList.add(listOfFiles.get(i));
                }
            }
        }
    }

    private void setVolume(double volume) {
        networkSpeakersController.setVolume(volume);
    }

    public void setVolumeAndUpdateSlider(double volume) {
        slide_vol.setValue(volume * slide_vol.getMax());
        //setVolume(volume);
    }

    public void btn_next_click(MouseEvent event) {
        Random random = new Random();
        btn_play.setText("Pause");
        File file;
        if(!queue.isEmpty()) {
            file = queue.firstElement();
            queue.remove(file);
            observableQueue.remove(file);
        } else {
            int randInt = random.nextInt(listProperty.getSize());
            file = listProperty.get(randInt);
        }
        playSong(file);
    }

    public void playSong(MouseEvent event) {
        if (event.getClickCount() == 2) {
            File file = (File) list_music.getSelectionModel().getSelectedItem();
            playSong(file);
        }
    }

    public void btn_previous(MouseEvent event) {
        if (!history.isEmpty() && player.getCurrentTime() < 1000) {
            history.pop();
        }
        if (!history.isEmpty()) {
            playSong(history.pop());
        }
    }

    public void playSong(String file) {
        player.play(file);
        networkSpeakersController.playNewSong(file);
    }

    public void btn_stop(MouseEvent event) {
        player.stop();
        btn_play.setText("Play");
        networkSpeakersController.stopSong();
    }

    public void btn_play(MouseEvent event) {
        if (player != null) {
            if (!player.isPlaying()) {
                player.play();
                networkSpeakersController.playSong();
                btn_play.setText("Pause");
            } else {
                player.pause();
                player.setCurrentTime(slide_time.getValue());
                networkSpeakersController.setCurrentTime(slide_time.getValue());
                networkSpeakersController.pauseSong();
                btn_play.setText("Play");
            }
        }
    }

    public void rootImport(MouseEvent event) {
        initMp3Player();
    }

    public void rootChoose(MouseEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Resource File");
        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            configPath.storeDirectory("rootFolder", file);
            initMp3Player();
        }
    }

    public void clearPlayList(MouseEvent event) {
        observableList.clear();
        listOfFiles.clear();
        listProperty.clear();
        queue.clear();
        observableQueue.clear();
        text_songs_added.setText("0");
    }

    public void initMp3Player() {
        String activeRootFolder = configPath.readKey("rootFolder");
        if (activeRootFolder != null) {
            textInfoRootFolder.setText(activeRootFolder);
            LocalDatabase db = new LocalDatabase();
            listOfFiles = db.getFolderItems(activeRootFolder);
            text_songs_added.setText("" + listOfFiles.size());
            list_music.itemsProperty().bind(listProperty);
            list_queue.itemsProperty().bind(queueProperty);
            observableQueue = FXCollections.observableArrayList(queue);
            observableList = FXCollections.observableArrayList(listOfFiles);
            listProperty.set(observableList);
            queueProperty.set(observableQueue);
        }
    }

    public void playSong(File file) {
        history.push(file);
        String selected = "file:///" + file;
        tf_currentSong.setText(file.getName());
        selected = selected.replaceAll(" ", "%20");
        selected = selected.replaceAll("\\\\", "/");
        selected = selected.replaceAll("\\[", "%5B");
        selected = selected.replaceAll("\\]", "%5D");
        System.out.println("clicked on " + selected);
        btn_play.setText("Pause");
        networkSpeakersController.playNewSong(selected);
        player.play(selected);
    }

    public void setSongTime(MouseEvent e) {
        //Add async with timer to avoid to many requests
        networkSpeakersController.setCurrentTime(slide_time.getValue());
        player.setCurrentTime(slide_time.getValue());
        System.out.println("Setting time : " + (int) slide_time.getValue());
    }

    public void getMp3DataThread() {

        class mp3UpdateyThread implements Runnable {

            public void run() {
                try {
                    while (true) {
                        thread.sleep(100);
                        slide_time.setMin(0);
                        slide_time.setMax(player.getTotalDuration());
                        slide_time.setValue(player.getCurrentTime());
                    }
                } catch (Exception e) {
                    System.out.println("Error in mp3UpdateyThread");
                    System.out.println(e.getStackTrace());
                }
            }
        }
        thread = new Thread(new mp3UpdateyThread());
        thread.start();
    }

    public void onListViewKeyPress(KeyEvent e) {
        System.out.println("Keypress: " + e.getCode());
        if(e.getCode() == KeyCode.Q) {
            File file = (File) list_music.getSelectionModel().getSelectedItem();
            if(queue.contains(file)) {
                queue.remove(file);
            } else {
                queue.push(file);
            }
        } else if (e.getCode() == KeyCode.C) {
            queue.clear();
        }
        observableQueue.clear();
        observableQueue.setAll(queue);
        System.out.println("queue");
        System.out.println(queue);
    }

    public void onQueueKeyPress(KeyEvent e) {
        System.out.println("Keypress: " + e.getCode());
        if(e.getCode() == KeyCode.Q) {
            File file = (File) list_queue.getSelectionModel().getSelectedItem();
            if(queue.contains(file)) {
                queue.remove(file);
            } else {
                queue.push(file);
            }
        } else if (e.getCode() == KeyCode.C) {
            queue.clear();
        }
        observableQueue.clear();
        observableQueue.setAll(queue);
        System.out.println("queue");
        System.out.println(queue);
    }
}
