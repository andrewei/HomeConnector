package network.tcpip.speakers;

import aplication.ClientObject;
import controller.MainController;
import network.tcpip.Action;
import org.json.simple.JSONObject;

public class NetworkRecieveController {
    MainController mainController;

    public NetworkRecieveController(MainController mainController) {
        this.mainController = mainController;
    }

    public void receive(JSONObject jsonObject) {
        Action actionSwitch = Action.valueOf((String) jsonObject.get("ACTION"));

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
