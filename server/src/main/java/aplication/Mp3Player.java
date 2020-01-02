package aplication;

import controller.MainController;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Mp3Player {

    private static int event = 0;
    private MainController mainController;
    private Media media;
    private MediaPlayer mediaPlayer;
    private double volume = 0;


    public Mp3Player(MainController main) {
        this.mainController = main;
        //HACK: must have a file to initiate the mediaplayer
        createNewMediaPlayer("file:///D:/test.mp3");
        mediaPlayer.setVolume(0);
    }

    public double getCurrentTime() {
        Duration test = mediaPlayer.getCurrentTime();
        return test.toMillis();
    }

    public double getTotalDuration() {
        Duration test = mediaPlayer.getTotalDuration();
        return test.toMillis();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public boolean isPlaying() {
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

    public void setCurrentTime(double time) {
        mediaPlayer.seek(Duration.millis(time));
    }

    public void play(String songPath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        try {
            createNewMediaPlayer(songPath);
            setVolume(volume);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Exception in play, could not play song, skipping");
            mainController.musicController.btn_next_click(null);
        }
    }

    private void setListeners() {
        mediaPlayer.setOnEndOfMedia(() -> {
            System.out.println("End of media file");
            mainController.musicController.btn_next_click(null);
        });

        mediaPlayer.setOnError(() -> {
            System.out.println("Error when playing file, skipping");
            mainController.musicController.btn_next_click(null);
        });
    }

    private void createNewMediaPlayer(String songpath) {
        media = new Media(songpath);
        mediaPlayer = new MediaPlayer(media);
        setListeners();
        if (mainController.videoController.mediaView != null)
            mainController.videoController.mediaView.setMediaPlayer(mediaPlayer);
    }
}

