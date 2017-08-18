package network.tcpip.speakers;

import aplication.ClientObject;
import controller.MainController;
import javafx.collections.ObservableList;
import network.tcpip.ActionConstants;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by andreas on 1/16/2016.
 */
public class NetworkSpeakersController {
    public String getLastSong() {
        return lastSong;
    }
    private String lastSong;
    private ObservableList<ClientObject> observableSpeakersArray;
    private MainController mainController;
    private static NetworkSpeakers network = new NetworkSpeakers();

    public NetworkSpeakersController(MainController mainController)  {
        this.mainController = mainController;
        observableSpeakersArray = mainController.observableSpeakersArray;

        //observableSpeakersArray

        try {
            ClientObject obj = new ClientObject("Server", InetAddress.getByName("localhost").getHostAddress(), 1, "on", mainController);
            observableSpeakersArray.add(obj);
            observableSpeakersArray.add(new ClientObject("Stasjonear", "192.168.0.106", 1, "off", mainController ));
            observableSpeakersArray.add(new ClientObject("PC LINE",    "192.168.10.171",   1, "off", mainController));
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void playNewSong(String path){
        lastSong = path;
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.PLAY_NEWSONG);
        jsonOutput.put("SONG", path);
        for (int i = 0; i < observableSpeakersArray.size(); i++){
            if(observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, observableSpeakersArray.get(i).getIp(), observableSpeakersArray.get(i).getDelay());
        }
    }
    public void playSong(){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.PLAY_SONG);
        for (int i = 0; i < observableSpeakersArray.size(); i++){
            if(observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, observableSpeakersArray.get(i).getIp(), observableSpeakersArray.get(i).getDelay());
        }
    }

    public void stopSong(){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.STOP_SONG);
        for (int i = 0; i < observableSpeakersArray.size(); i++){
            if(observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, observableSpeakersArray.get(i).getIp(), observableSpeakersArray.get(i).getDelay());
        }
    }

    //used to stop a song from playing on inactivated speacker.
    public void stopSong(String ip){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.STOP_SONG);
        network.sendJSON(jsonOutput, ip, 1);
    }

    public void setVolume(Double volume){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.SET_VOLUME);
        jsonOutput.put("VOLUME", volume);
        for (int i = 0; i < observableSpeakersArray.size(); i++){
            if(observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, observableSpeakersArray.get(i).getIp(), observableSpeakersArray.get(i).getDelay());
        }
    }

    public void setCurrentTime (double time){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", ActionConstants.SET_CURRENT_TIME);
        jsonOutput.put("TIME", time);
        for (int i = 0; i < observableSpeakersArray.size(); i++){
            if(observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, observableSpeakersArray.get(i).getIp(), observableSpeakersArray.get(i).getDelay());
        }
    }
}
