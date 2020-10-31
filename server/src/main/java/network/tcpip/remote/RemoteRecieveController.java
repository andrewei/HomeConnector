package network.tcpip.remote;

import controller.MainController;
import network.tcpip.helpers.RemoteReceiveActionEnum;
import network.tcpip.helpers.IReceiveController;
import network.tcpip.core.NetworkReceive;
import org.json.simple.JSONObject;

import java.io.IOException;

public class RemoteRecieveController implements IReceiveController {
    MainController mainController;
    NetworkReceive networkRemote;

    public RemoteRecieveController(MainController mainController){
        this.mainController = mainController;
        this.networkRemote = new NetworkReceive();

        try {
            networkRemote.receive(this, 9870);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void receive(JSONObject jsonObject) {
        RemoteReceiveActionEnum actionSwitch = RemoteReceiveActionEnum.valueOf((String) jsonObject.get("ACTION"));

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
                double volume = Double.parseDouble(jsonObject.get("VOLUME").toString());
                mainController.musicController.setVolumeAndUpdateSlider(volume);
                break;
            case PAUSE_SONG:
                mainController.musicController.btn_play(null);
                break;
            default:
                System.out.println("NetworkController in default case");
                break;
        }
    }
}
