package controller;

import aplication.ClientObject;
import aplication.Clock;
import aplication.Mp3Player;
import config.ConfigPath;
import controller.tab.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.serial.FindPort;
import network.serial.SerialConnector;
import network.tcpip.remote.NetworkRemoteController;
import network.tcpip.speakers.NetworkSpeakersController;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;

public class MainController implements Initializable {

    private ColorAdjust colorAdjust;
    private Timer clockTimerObject;
    public ObservableList<ClientObject> observableSpeakersArray;

    @FXML
    private Slider slide_brightness;
    @FXML
    private SplitPane rootObj;
    @FXML
    private TableView<ClientObject> tableSpeakers;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colIP;
    @FXML
    private TableColumn colActive;
    @FXML
    private TableColumn colVolume;

    public void onAddItem(MouseEvent event) {
    }

    private void initTable() {

        colName.setCellValueFactory(
                new PropertyValueFactory<ClientObject, SimpleStringProperty>("name")
        );
        colIP.setCellValueFactory(
                new PropertyValueFactory<ClientObject, SimpleStringProperty>("ip")
        );
        colActive.setCellValueFactory(
                new PropertyValueFactory<ClientObject, SimpleStringProperty>("active")
        );
        colVolume.setCellValueFactory(
                new PropertyValueFactory<ClientObject, SimpleStringProperty>("volume")
        );
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colIP.setCellFactory(TextFieldTableCell.forTableColumn());
        colActive.setCellFactory(TextFieldTableCell.forTableColumn());
        colVolume.setCellFactory(TextFieldTableCell.forTableColumn());

        observableSpeakersArray = FXCollections.observableArrayList(); // create the data
        tableSpeakers.setItems(observableSpeakersArray); // assign the data to the table
    }

    public void colNameCommit(TableColumn.CellEditEvent<ClientObject, String> t) {
        System.out.println("commit name");
        (t.getTableView().getItems().get(
                t.getTablePosition().getRow())
        ).setName(t.getNewValue());
    }

    public void colIPCommit(TableColumn.CellEditEvent<ClientObject, String> t) {
        System.out.println("commit ip");
        (t.getTableView().getItems().get(
                t.getTablePosition().getRow())
        ).setIp(t.getNewValue());
    }

    public void colActiveCommit(TableColumn.CellEditEvent<ClientObject, String> t) {
        System.out.println("commit active");
        (t.getTableView().getItems().get(
                t.getTablePosition().getRow())
        ).setActive(t.getNewValue());
    }

    public void colVolumeCommit(TableColumn.CellEditEvent<ClientObject, String> t) {
        System.out.println("commit active");
        (t.getTableView().getItems().get(
                t.getTablePosition().getRow())
        ).setVolume(t.getNewValue());
    }

    public static NetworkSpeakersController networkSpeakersController;
    public static NetworkRemoteController networkRemoteController;
    public static SerialConnector serialConnector;
    public static ConfigPath configPath;
    public static Mp3Player player;
    public static Clock clock;
    public static Stage myStage;

    @FXML
    public Text str_date;
    @FXML
    public Text str_hour;
    @FXML
    public Text str_minute;
    @FXML
    public LightsController lightsController;
    @FXML
    public TemperatureController temperatureController;
    @FXML
    public RemotesController remotesController;
    @FXML
    public DoorBellController doorBellController;
    @FXML
    public MusicController musicController;
    @FXML
    public AlarmController alarmController;
    @FXML
    public VideoController videoController;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        configPath = new ConfigPath();
        player = new Mp3Player(this);
        networkSpeakersController = new NetworkSpeakersController(this);
        networkRemoteController = new NetworkRemoteController(this);
        //initSerial();
        clockTimerObject = new Timer();
        clockTimerObject.schedule(clock = new Clock(this), 0, 1000);

        colorAdjust = new ColorAdjust();
        slide_brightness.setMin(-.85);
        slide_brightness.setMax(0);
        slide_brightness.setValue(0);
        colorAdjust.setBrightness(0);
        colorAdjust.setContrast(0);
        rootObj.setEffect(colorAdjust);

        lightsController.init(this);
        temperatureController.init(this);
        remotesController.init(this);
        doorBellController.init(this);
        musicController.init(this);
        alarmController.init(this);
        videoController.init(this);

        try {
            ClientObject obj = new ClientObject("Server", InetAddress.getByName("localhost").getHostAddress(), "on", this);
            observableSpeakersArray.add(obj);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void initSerial() {
        try {
            FindPort findPort = new FindPort();
            List availablePorts = findPort.availablePorts();
            for (int i = 0; i < availablePorts.size(); i++) {
                serialConnector = new SerialConnector(this);
                serialConnector.connect("" + availablePorts.get(i));
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("could not open serial connection");
        }
    }

    public void btn_fullscreen(MouseEvent event) {
        if (myStage.isFullScreen())
            myStage.setFullScreen(false);
        else {
            myStage.setFullScreen(true);
        }
    }

    public void setStage(Stage stage) {
        myStage = stage;
    }

    public void setBrightness(MouseEvent event) {
        colorAdjust.setBrightness(slide_brightness.getValue());
    }

    public void testDeleteMe() {
        System.out.println("THIS IS WORKING: TEST OK");
    }

    public void addSpeaker(ActionEvent event) {
        networkSpeakersController.ping();
    }

    public void removeSelected(ActionEvent event) {
        tableSpeakers.getSelectionModel();
        if (tableSpeakers.getSelectionModel().getSelectedItem() != null) {
            TableView.TableViewSelectionModel selectionModel = tableSpeakers.getSelectionModel();
            ObservableList selectedCells = selectionModel.getSelectedCells();
            TablePosition tablePosition = (TablePosition) selectedCells.get(0);
            observableSpeakersArray.remove(tablePosition.getRow());
        }
    }
}