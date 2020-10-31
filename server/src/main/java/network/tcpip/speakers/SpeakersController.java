package network.tcpip.speakers;

import controller.MainController;
import network.tcpip.core.NetworkReceive;
import network.tcpip.core.NetworkSend;
import network.tcpip.helpers.PlayerActionsConstants;
import org.json.simple.JSONObject;
import utils.UtilsNetwork;

import java.io.IOException;

public class SpeakersController {
    private String lastSong;
    private MainController mainController;
    private SpeakersReceiveController networkRecieveController;
    private NetworkReceive networkReceive;
    private int delay = 2000;
    private long lastSetTime = 1000000;
    private String hostBaseIP;
    private NetworkSend networkSend;

    public SpeakersController(MainController mainController)  {
        this.mainController = mainController;
        this.networkRecieveController = new SpeakersReceiveController(mainController);
        this.networkReceive = new NetworkReceive();
        this.networkSend = new NetworkSend();
        this.hostBaseIP = UtilsNetwork.getHostIP(true);

        try {
            networkReceive.receive(this.networkRecieveController, 8555);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getLastSong() {
        return lastSong;
    }

    public void playNewSong(String path){
        lastSong = path;
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", PlayerActionsConstants.PLAY_NEWSONG);
        jsonOutput.put("SONG", path);
        jsonOutput.put("STARTTIME", "" + (System.currentTimeMillis()+delay));
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                networkSend.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }
    public void playSong(){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", PlayerActionsConstants.PLAY_SONG);
        jsonOutput.put("STARTTIME", "" + (System.currentTimeMillis()+delay));
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                networkSend.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }

    public void stopSong(){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", PlayerActionsConstants.STOP_SONG);
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                networkSend.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }

    public void stopSong(String ip){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", PlayerActionsConstants.STOP_SONG);
        networkSend.sendJSON(jsonOutput, ip);
    }

    public void setVolume(Double volume, String ip){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", PlayerActionsConstants.SET_VOLUME);
        jsonOutput.put("VOLUME", volume);
        networkSend.sendJSON(jsonOutput, ip);
    }

    public void setVolume(Double volume){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", PlayerActionsConstants.SET_VOLUME);
        jsonOutput.put("VOLUME", volume);
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                networkSend.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }

    public void setCurrentTime (double time){
        if((System.currentTimeMillis() - lastSetTime) < 500)
            return;
        lastSetTime = System.currentTimeMillis();
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", PlayerActionsConstants.SET_CURRENT_TIME);
        jsonOutput.put("TIME", time);
        jsonOutput.put("STARTTIME", "" + (System.currentTimeMillis()+delay));
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                networkSend.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }

    public void pauseSong() {
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", PlayerActionsConstants.PAUSE_SONG);
        jsonOutput.put("STARTTIME", "" + (System.currentTimeMillis()+delay));
        for (int i = 0; i < mainController.observableSpeakersArray.size(); i++){
            if(mainController.observableSpeakersArray.get(i).getActive().equals("on"))
                networkSend.sendJSON(jsonOutput, mainController.observableSpeakersArray.get(i).getIp());
        }
    }

    public void ping() {
        mainController.observableSpeakersArray.clear();
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("ACTION", PlayerActionsConstants.PING);
        this.ping(jsonOutput, hostBaseIP);
    }

    public void ping(JSONObject inn, final String baseIp) {
        class pingThread implements Runnable {

            @Override
            public void run() {
                for (int i = 1; i < 265; i++) {
                    String ip = baseIp + "." + String.valueOf(i);
                    networkSend.sendJSON(inn, ip);
                }
            }
        }
        Thread thread = new Thread(new pingThread());
        thread.start();
    }
}
