package network.tcpip;

import aplication.ClientObject;
import network.tcpip.Network;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by andreas on 1/16/2016.
 */
public class NetworkController {

    ArrayList<ClientObject> clientObjects;

    public NetworkController(){
        clientObjects = new ArrayList<ClientObject>();
        clientObjects.add(new ClientObject("Server",     "127.0.0.1",      230, true));
        clientObjects.add(new ClientObject("Stasjonear", "192.168.10.146", 190, true));
        clientObjects.add(new ClientObject("PC LINE",    "192.168.10.171",   1, true));
    }

    private static Network network = new Network();

    public void playNewSong(String path){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", "PLAY_NEWSONG");
        jsonOutput.put("SONG", path);
        for (int i = 0; i < clientObjects.size(); i++){
            if(clientObjects.get(i).isActive())
                network.sendJSON(jsonOutput, clientObjects.get(i).getIp(), clientObjects.get(i).getDelay());
        }
    }
    public void playSong(){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", "PLAY_SONG");
        for (int i = 0; i < clientObjects.size(); i++){
            if(clientObjects.get(i).isActive())
                network.sendJSON(jsonOutput, clientObjects.get(i).getIp(), clientObjects.get(i).getDelay());
        }
    }

    public void stopSong(){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", "STOP_SONG");
        for (int i = 0; i < clientObjects.size(); i++){
            if(clientObjects.get(i).isActive())
                network.sendJSON(jsonOutput, clientObjects.get(i).getIp(), clientObjects.get(i).getDelay());
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
        for (int i = 0; i < clientObjects.size(); i++){
            if(clientObjects.get(i).isActive())
                network.sendJSON(jsonOutput, clientObjects.get(i).getIp(), clientObjects.get(i).getDelay());
        }
    }

    public void setCurrentTime (double time){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", "SET_CURRENT_TIME");
        jsonOutput.put("TIME", time);
        for (int i = 0; i < clientObjects.size(); i++){
            if(clientObjects.get(i).isActive())
                network.sendJSON(jsonOutput, clientObjects.get(i).getIp(), clientObjects.get(i).getDelay());
        }
    }
}
