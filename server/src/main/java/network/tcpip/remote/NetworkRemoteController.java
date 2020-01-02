package network.tcpip.remote;

import controller.MainController;
import network.tcpip.ActionEnum;
import org.json.simple.JSONObject;

import java.io.IOException;

public class NetworkRemoteController {
    MainController mainController;

    public NetworkRemoteController(MainController mainController){
        this.mainController = mainController;

        NetworkRemote networkRemote = new NetworkRemote();
        try {
            networkRemote.receive(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void receive(JSONObject jsonObject) {
        ActionEnum actionSwitch = ActionEnum.valueOf((String) jsonObject.get("ACTION"));

        switch (actionSwitch) {
            case PLAY_SONG:
                mainController.musicController.btn_play(null);
                break;
            case STOP_SONG:
                mainController.musicController.btn_stop(null);
                break;
            case PLAY_NEXTSONG:
                mainController.musicController.btn_next_click(null);
                break;
            case SET_VOLUME:
                setVolume(jsonObject);
                break;
            case PAUSE_SONG:
                mainController.musicController.btn_play(null);
                break;
            default:
                System.out.println("NetworkController in default case");
                break;
        }
    }

    public void setVolume(JSONObject jsonObject) {
        double volume = Double.parseDouble(jsonObject.get("VOLUME").toString());
        System.out.println("SETTING VOLUME");
        mainController.musicController.setVolumeAndUpdateSlider(volume);
    }
}
