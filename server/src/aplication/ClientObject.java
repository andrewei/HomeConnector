package aplication;

import controller.MainController;
import javafx.beans.property.SimpleStringProperty;

public class ClientObject {
    private MainController mainController;
    public SimpleStringProperty name = new SimpleStringProperty();
    public SimpleStringProperty ip = new SimpleStringProperty();
    public SimpleStringProperty active = new SimpleStringProperty();

    private int delay;

    public ClientObject(String name, String ip, int delay, String active, MainController mainController){
        this.mainController = mainController;
        this.name.set(name);
        this.ip.set(ip);
        this.active.set(active);
        this.delay = delay;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getIp() {
        return ip.get();
    }

    public void setIp(String ip) {
        this.ip.set(ip);
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getActive() {
        return active.get();
    }

    public void setActive(String active) {
        this.active.set(active);
        if(!active.equals("on"))
            mainController.networkSpeakersController.stopSong(this.ip.get());
        else{
            //TODO this needs refactoring, store lastSong somewere else
            mainController.tab5Controller.playSong(mainController.networkSpeakersController.getLastSong());
        }
    }
}
