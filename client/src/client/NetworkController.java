package client;

import org.json.simple.JSONObject;

/**
 * Created by andreas on 1/16/2016.
 */
public class NetworkController {

    String songLink;
    Double volume;
    String startTime;
    long startTimeLong;

    static Mp3Player player = new Mp3Player();

    public void receive(JSONObject jsonObject) {
        Action actionSwitch = Action.valueOf((String) jsonObject.get("ACTION"));

        switch (actionSwitch) {
            case PLAY_NEWSONG:
                songLink = (String) jsonObject.get("SONG");
                startTime = (String) jsonObject.get("STARTTIME");
                System.out.println("Song to be played " + songLink);
                player.play(songLink, Long.valueOf(startTime));
                break;
            case PLAY_SONG:
                startTime = (String) jsonObject.get("STARTTIME");
                System.out.println("PLAY");
                player.play(Long.valueOf(startTime));
                break;
            case STOP_SONG:
                System.out.println("STOP");
                player.stop();
                break;
            case PAUSE_SONG:
                System.out.println("PAUSE");
                startTime = (String) jsonObject.get("STARTTIME");
                player.pause(Long.valueOf(startTime));
                break;
            case SET_VOLUME:
                volume = (Double) jsonObject.get("VOLUME");
                System.out.println("Setting volume to " + volume);
                player.setVolume(volume);
                break;
            case SET_CURRENT_TIME:
                double time = (Double)jsonObject.get("TIME");
                startTime = (String) jsonObject.get("STARTTIME");
                System.out.println("time: " + (int)time);
                player.setCurrentTime((int)time, Long.valueOf(startTime));
                break;
            default:
                System.out.println("NetworkController in default case");
                break;
        }
    }
}
