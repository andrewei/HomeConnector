package client;

import org.json.simple.JSONObject;

public class NetworkController {

    private String songLink;
    private Double volume;
    private String startTime;
    private MainController main;
    private Mp3Player player;

    public NetworkController(MainController main) {
        this.main = main;
        player = new Mp3Player(main);
    }

    public void receive(JSONObject jsonObject, String serverIP, String localIP) {
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
            case PING:
                JSONObject jsonOutput = new JSONObject();
                jsonOutput.put("ACTION", ActionConstants.PONG);
                jsonOutput.put("IP", localIP);
                System.out.println("Sending pong");
                main.network.sendJSON(jsonOutput, serverIP);
                break;
            case REBOOT:
                System.out.println("Reboot not implemented");
            default:
                System.out.println("NetworkController in default case");
                break;
        }
    }
}
