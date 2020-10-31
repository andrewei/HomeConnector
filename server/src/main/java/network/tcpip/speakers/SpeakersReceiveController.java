package network.tcpip.speakers;

import aplication.ClientObject;
import controller.MainController;
import network.tcpip.helpers.IReceiveController;
import network.tcpip.helpers.SpeakerReceiveActionEnum;
import org.json.simple.JSONObject;

public class SpeakersReceiveController implements IReceiveController {
    MainController mainController;

    public SpeakersReceiveController(MainController mainController) {
        this.mainController = mainController;
    }

    public void receive(JSONObject jsonObject) {
        SpeakerReceiveActionEnum actionSwitch = SpeakerReceiveActionEnum.valueOf((String) jsonObject.get("ACTION"));

        switch (actionSwitch) {
            case PONG:
                String ip = (String) jsonObject.get("IP");
                System.out.println("Pong received");
                int i = mainController.observableSpeakersArray.size();
                mainController.observableSpeakersArray.add( new ClientObject( "" + i, ip, "on", mainController));
                break;
            default:
                System.out.println("NetworkRecieveController in default case");
                break;
        }
    }
}
