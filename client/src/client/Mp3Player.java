package client;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Created by andreas on 1/10/2016.
 */
public class Mp3Player {
    MediaPlayer mediaPlayer;
    Media media;
    double volume = .5;

    public void stop() {
        mediaPlayer.stop();
    }

    public void play(long startTime) {
        if (mediaPlayer != null) {
            System.out.println("WIll START IN: " + (startTime  - System.currentTimeMillis()) + "ms");
            while(System.currentTimeMillis() < startTime) {}
            mediaPlayer.play();
        }
    }

    public void pause(long startTime) {
        if (mediaPlayer != null) {
            System.out.println("WIll START IN: " + (startTime  - System.currentTimeMillis()) + "ms");
            while(System.currentTimeMillis() < startTime) {}
            mediaPlayer.pause();
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
            this.volume = volume;
        }
    }

    public void setCurrentTime(int time, long startTime) {
        System.out.println("WIll START IN: " + (startTime  - System.currentTimeMillis()) + "ms");
        while(System.currentTimeMillis() < startTime) {}
        mediaPlayer.seek(Duration.millis(time));
    }

    public void play(String songPath, Long startTime) {
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
        media = new Media(songPath);
        mediaPlayer = new MediaPlayer(media);
        setVolume(volume);
        System.out.println("WIll START IN: " + (startTime  - System.currentTimeMillis()) + "ms");
        while(System.currentTimeMillis() < startTime) {}
        mediaPlayer.play();
    }
}