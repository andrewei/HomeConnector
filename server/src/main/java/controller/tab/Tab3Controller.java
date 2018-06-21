package controller.tab;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import network.serial.SerialConnector;

import java.net.URL;
import java.util.ResourceBundle;

public class Tab3Controller implements Initializable {

    private SerialConnector serialConnector;
    private MainController mainController;

    @FXML private ImageView btn_pwr;
    @FXML private ImageView btn_vup;
    @FXML private ImageView btn_vdown;
    @FXML private ImageView btn_source;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void init(MainController mainController){
        this.mainController = mainController;
        this.serialConnector = mainController.serialConnector;
    }

    public void btn_pwr_click(MouseEvent event){
        serialConnector.setWrite(49);
        //ImageView obj = (ImageView) event.getSource();
        //obj.setStyle("-fx-text-fill: #c4d8de;");
    }
    public void btn_vdown_click(MouseEvent event){
        serialConnector.setWrite(50);
        //ImageView obj = (ImageView) event.getSource();
        //obj.setStyle("-fx-text-fill: #c4d8de;");
    }
    public void btn_vup_click(MouseEvent event){
        serialConnector.setWrite(51);
        //ImageView obj = (ImageView) event.getSource();
        //obj.setStyle("-fx-text-fill: #c4d8de;");
    }
    public void btn_source_click(MouseEvent event){
        serialConnector.setWrite(52);
        //ImageView obj = (ImageView) event.getSource();
        //obj.setStyle("-fx-text-fill: #c4d8de;");
    }

    public void resetColor(MouseEvent event){
        //ImageView obj = (ImageView) event.getSource();
        //obj.setStyle("-fx-text-fill: #000000;");
    }
}
