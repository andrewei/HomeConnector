package controller.tab;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.media.MediaView;

public class VideoController {
    @FXML
    public MediaView mediaView;
    private MainController mainController;

    public void init(MainController mainController) {
        this.mainController = mainController;
    }
}
