package network.tcpip;

import aplication.ClientObject;
import controller.MainController;
import javafx.collections.ObservableList;
import org.json.simple.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by andreas on 1/16/2016.
 */
public class NetworkController {

    public String getLastSong() {
        return lastSong;
    }

    private String lastSong;

    private ObservableList<ClientObject> observableSpeakersArray;

    private MainController mainController;

    public NetworkController(MainController mainController)  {
        this.mainController = mainController;
        observableSpeakersArray = mainController.observableSpeakersArray;
        //observableSpeakersArray

        try {
            ClientObject obj = new ClientObject("Server", InetAddress.getByName("localhost").getHostAddress(), 1, "on", mainController);
            observableSpeakersArray.add(obj);
            observableSpeakersArray.add(new ClientObject("Stasjonear", "192.168.10.146", 1, "off", mainController ));
            observableSpeakersArray.add(new ClientObject("PC LINE",    "192.168.10.171",   1, "off", mainController));
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static Network network = new Network();

    public void playNewSong(String path){
        lastSong = path;
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", "PLAY_NEWSONG");
        jsonOutput.put("SONG", path);
        for (int i = 0; i < observableSpeakersArray.size(); i++){
            if(observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, observableSpeakersArray.get(i).getIp(), observableSpeakersArray.get(i).getDelay());
        }
    }
    public void playSong(){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", "PLAY_SONG");
        for (int i = 0; i < observableSpeakersArray.size(); i++){
            if(observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, observableSpeakersArray.get(i).getIp(), observableSpeakersArray.get(i).getDelay());
        }
    }

    public void stopSong(){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", "STOP_SONG");
        for (int i = 0; i < observableSpeakersArray.size(); i++){
            if(observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, observableSpeakersArray.get(i).getIp(), observableSpeakersArray.get(i).getDelay());
        }
    }

    //used to stop a song from playing on inactivated speacker.
    public void stopSong(String ip){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", "STOP_SONG");
        network.sendJSON(jsonOutput, ip, 1);
    }

    public void setVolume(Double volume){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", "SET_VOLUME");
        jsonOutput.put("VOLUME", volume);
        for (int i = 0; i < observableSpeakersArray.size(); i++){
            if(observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, observableSpeakersArray.get(i).getIp(), observableSpeakersArray.get(i).getDelay());
        }
    }

    public void setCurrentTime (double time){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", "SET_CURRENT_TIME");
        jsonOutput.put("TIME", time);
        for (int i = 0; i < observableSpeakersArray.size(); i++){
            if(observableSpeakersArray.get(i).getActive().equals("on"))
                network.sendJSON(jsonOutput, observableSpeakersArray.get(i).getIp(), observableSpeakersArray.get(i).getDelay());
        }
    }
}
