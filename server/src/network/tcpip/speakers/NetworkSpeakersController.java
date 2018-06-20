package network.tcpip.speakers;

import aplication.ClientObject;
import controller.MainController;
import network.tcpip.ActionConstants;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class NetworkSpeakersController {
    public String getLastSong() {
        return lastSong;
    }
    private String lastSong;
    private MainController mainController;
    private NetworkRecieveController networkRecieveController;
    private static NetworkSpeakers network;
    private int delay = 2000;
    private long lastSetTime = 1000000;

    public NetworkSpeakersController(MainController mainController)  {
        this.mainController = mainController;
        networkRecieveController = new NetworkRecieveController(mainController);
        network = new NetworkSpeakers(networkRecieveController);
        try {
            network.recive();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void playNewSong(String path){
        lastSong = path;
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.PLAY_NEWSONG);
        jsonOutput.put("SONG", path);
        jsonOutput.put("STARTTIME", "" + (System.currentTimeMillis()+delay));
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }
    public void playSong(){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.PLAY_SONG);
        jsonOutput.put("STARTTIME", "" + (System.currentTimeMillis()+delay));
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }

    public void stopSong(){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.STOP_SONG);
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }

    //used to stop a song from playing on inactivated speaker.
    public void stopSong(String ip){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.STOP_SONG);
        network.sendJSON(jsonOutput, ip);
    }

    public void setVolume(Double volume){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.SET_VOLUME);
        jsonOutput.put("VOLUME", volume);
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }

    public void setCurrentTime (double time){
        if((System.currentTimeMillis() - lastSetTime) < 500)
            return;
        lastSetTime = System.currentTimeMillis();
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.SET_CURRENT_TIME);
        jsonOutput.put("TIME", time);
        jsonOutput.put("STARTTIME", "" + (System.currentTimeMillis()+delay));
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }

    public void pauseSong() {
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.PAUSE_SONG);
        jsonOutput.put("STARTTIME", "" + (System.currentTimeMillis()+delay));
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }

    public void ping() {
        mainController.observableSpeakersArray.clear();
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.PING);
        network.ping(jsonOutput, "192.168.0.");
    }
}
