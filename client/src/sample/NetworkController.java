package sample;

import javafx.util.Duration;
import org.json.simple.JSONObject;

/**
 * Created by andreas on 1/16/2016.
 */
public class NetworkController {

    String songLink;
    Double volume;

    static Mp3Player player = new Mp3Player();

    public void receive(JSONObject jsonObject) {
        Action actionSwitch = Action.valueOf((String) jsonObject.get("ACTION"));

        switch (actionSwitch) {
            case PLAY_NEWSONG:
                songLink = (String) jsonObject.get("SONG");
                player.play(songLink);
                System.out.println("Song to be played " + songLink);
                break;
            case PLAY_SONG:
                player.play();
                System.out.println("PLAY");
                break;
            case STOP_SONG:
                player.stop();
                System.out.println("STOP");
                break;
            case SET_VOLUME:
                volume = (Double) jsonObject.get("VOLUME");
                player.setVolume(volume);
                System.out.println("Setting volume to " + volume);
                break;
            case SET_CURRENT_TIME:
                double time = (Double)jsonObject.get("TIME");
                System.out.println("time: " + (int)time);
                player.setCurrentTime((int)time);
                System.out.println("Setting current time to " + volume);
            default:
                System.out.println("NetworkController in default case");
                break;
        }
    }
}
