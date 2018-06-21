package aplication;

import controller.MainController;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.InputStream;

public class Mp3Player {

    private static int event = 0;
    private MainController mainController;
    Media media;

    public Mp3Player(MainController main){
        this.mainController = main;
        //must have a file to initiate the mediaplayer
        Media media = new Media("file:///D:/test.mp3");
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0);
        setListeners();
    }

    public double getCurrentTime(){
        Duration test =  mediaPlayer.getCurrentTime();
        //System.out.println("currentTime: " + test.toString());
        return test.toMillis();
    }

    public double getTotalDuration(){
        Duration test =  mediaPlayer.getTotalDuration();
       // System.out.println("TotalTime:" + test.toString());
        return test.toMillis();
    }

    MediaPlayer mediaPlayer;

    double volume = 0;

    public void stop() {
        mediaPlayer.stop();
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public boolean isPlaying(){
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
            this.volume = volume;
        }
    }

    public void setCurrentTime(int time) {
        mediaPlayer.seek(Duration.millis(time));
    }

    public void play(String songPath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        try {
        media = new Media(songPath);
        mediaPlayer = new MediaPlayer(media);
        setListeners();
        setVolume(volume);
        mediaPlayer.play();
        }
        catch (Exception e) {
            System.out.println("Exception in play, could not play song, skipping");
            mainController.tab5Controller.btn_next_click(null);
        }
    }

    private void setListeners() {
        mediaPlayer.setOnEndOfMedia(() -> {
            System.out.println("End of media file");
            mainController.tab5Controller.btn_next_click(null);
        });

        mediaPlayer.setOnError(() -> {
            System.out.println("Error when playing file, skipping");
            mainController.tab5Controller.btn_next_click(null);
        });
    }

}

