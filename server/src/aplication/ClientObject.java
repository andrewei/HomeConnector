package aplication;

import network.tcpip.NetworkController;

/**
 * Created by andreas on 1/19/2016.
 */
public class ClientObject {
    private static NetworkController networkController = new NetworkController();
    private String name;


    private String ip;
    private int delay;
    private boolean active;

    public ClientObject(String name, String ip, int delay, boolean active){
        this.name = name;
        this.ip = ip;
        this.delay = delay;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if(active == false)
            networkController.stopSong(this.ip);
        //TODO if true, start playing along with rest of speakers
    }
}
