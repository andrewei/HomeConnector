package network.tcpip.speakers;

import controller.MainController;
import network.tcpip.ActionConstants;
import org.json.simple.JSONObject;

public class NetworkSpeakersController {
    public String getLastSong() {
        return lastSong;
    }
    private String lastSong;
    private MainController mainController;
    private static NetworkSpeakers network = new NetworkSpeakers();
    private int delay = 1000;
    private long lastSetTime = 1000000;

    public NetworkSpeakersController(MainController mainController)  {
        this.mainController = mainController;
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
}
