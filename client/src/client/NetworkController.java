package client;

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
                System.out.println("Song to be played " + songLink);
                player.play(songLink);
                break;
            case PLAY_SONG:
                System.out.println("PLAY");
                player.play();
                break;
            case STOP_SONG:
                System.out.println("STOP");
                player.stop();
                break;
            case PAUSE_SONG:
                System.out.println("STOP");
                player.pause();
                break;
            case SET_VOLUME:
                volume = (Double) jsonObject.get("VOLUME");
                System.out.println("Setting volume to " + volume);
                player.setVolume(volume);
                break;
            case SET_CURRENT_TIME:
                double time = (Double)jsonObject.get("TIME");
                System.out.println("time: " + (int)time);
                player.setCurrentTime((int)time);
                break;
            default:
                System.out.println("NetworkController in default case");
                break;
        }
    }
}
