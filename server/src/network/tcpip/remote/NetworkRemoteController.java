package network.tcpip.remote;

import controller.MainController;
import network.tcpip.ActionEnum;
import org.json.simple.JSONObject;
import java.io.IOException;

/**
 * Created by weise on 17/08/2017.
 */
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
                mainController.tab5Controller.btn_play(null);
                break;
            case STOP_SONG:
                mainController.tab5Controller.btn_stop(null);
                break;
            case PLAY_NEXTSONG:
                mainController.tab5Controller.btn_next_click(null);
                break;
            case SET_VOLUME:
                setVolume(jsonObject);
                break;
            default:
                System.out.println("NetworkController in default case");
                break;
        }
    }

    public void setVolume(JSONObject jsonObject) {
        double volume = Double.parseDouble(jsonObject.get("VOLUME").toString());
        System.out.println("SETTING VOLUME");
        mainController.tab5Controller.setVolume(volume);
    }
}
