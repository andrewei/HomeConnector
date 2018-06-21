package lights;


import controller.MainController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by andreas on 3/15/2016.
 */
public class Tab1Controller implements Initializable {

    MainController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void init(MainController mainController){
        this.mainController = mainController;
    }

    public void testControllerSetup(){
        mainController.testDeleteMe();
    }
}
