package com.example.weise.androidhc.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by weise on 18/08/2017.
 */

public class NetworkController {

    public void playSong() {
        JSONObject jsonObject = new JSONObject();
        put(jsonObject, ActionConstants.KEY_ACTION, ActionConstants.PLAY_SONG);
        Network network = new Network(jsonObject.toString());
    }

    public void stopSong(){
        JSONObject jsonObject = new JSONObject();
        put(jsonObject, ActionConstants.KEY_ACTION, ActionConstants.STOP_SONG);
        Network network = new Network(jsonObject.toString());
    }

    public void nextSong(){
        JSONObject jsonObject = new JSONObject();
        put(jsonObject, ActionConstants.KEY_ACTION, ActionConstants.PLAY_NEXTSONG);
        Network network = new Network(jsonObject.toString());
    }

    public void setVolume(double volume){
        JSONObject jsonObject = new JSONObject();
        put(jsonObject, ActionConstants.KEY_ACTION, ActionConstants.SET_VOLUME);
        put(jsonObject, ActionConstants.KEY_VOLUME, "" + volume);
        Network network = new Network(jsonObject.toString());
    }

    void put(JSONObject jsonObject, String key, String value){
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}