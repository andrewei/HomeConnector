package aplication;

import controller.MainController;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

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
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
        media = new Media(songPath);
        mediaPlayer = new MediaPlayer(media);
        setVolume(volume);
        mediaPlayer.play();
    }
}