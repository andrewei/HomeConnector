package nop.homeconnector.maven;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Mp3Player {
    private MediaPlayer mediaPlayer;
    private Media media;
    private double volume = .5;
    private MainController main;

    public Mp3Player(MainController main) {
        this.main = main;
    }

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
        if(main.mediaView != null)
            main.mediaView.setMediaPlayer(mediaPlayer);
        setVolume(volume);
        System.out.println("WIll START IN: " + (startTime  - System.currentTimeMillis()) + "ms");
        while(System.currentTimeMillis() < startTime) {}
        mediaPlayer.play();
    }
}