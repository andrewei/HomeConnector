package controller.tab;

import controller.MainController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Tab2Controller implements Initializable {

    private MainController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void init(MainController mainController) {
        this.mainController = mainController;
    }
}
