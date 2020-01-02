package aplication;

import controller.MainController;
import javafx.beans.property.SimpleStringProperty;

public class ClientObject {
    private MainController mainController;
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty ip = new SimpleStringProperty();
    private SimpleStringProperty active = new SimpleStringProperty();

    public ClientObject(String name, String ip, String active, MainController mainController){
        this.mainController = mainController;
        this.name.set(name);
        this.ip.set(ip);
        this.active.set(active);
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

    public String getActive() {
        return active.get();
    }

    public void setActive(String active) {
        this.active.set(active);
        if(!active.equals("on"))
            mainController.networkSpeakersController.stopSong(this.ip.get());
        else{
            //TODO this needs refactoring, store lastSong somewere else
            mainController.musicController.playSong(mainController.networkSpeakersController.getLastSong());
        }
    }

    public void setVolume(String volume) {
        mainController.networkSpeakersController.setVolume(Double.parseDouble(volume), this.ip.get());
    }
}
