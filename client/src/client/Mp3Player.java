package client;

import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Created by andreas on 1/10/2016.
 */
public class Mp3Player {

    Thread thread;
    static MediaPlayer mediaPlayer;
    static Media media;
    static int event = 0;
    double volume = .5;

    public Mp3Player() {
        //must have a file to initiate the mediaplayer

    }

    class Path {
        public String path;
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void play() {

        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
            this.volume = volume;
        }
    }

    public void setCurrentTime(int time){
        mediaPlayer.seek(new Duration(time));
    }

    public void play(String songPath) {

        if (mediaPlayer == null) {
            Media media = new Media(songPath);
            mediaPlayer = new MediaPlayer(media);
        }

        final Path path = new Path();
        path.path = songPath;

        if (thread != null) {
            System.out.println("Resetting thread");
            event = 1;
        }

        class playThread implements Runnable {

            public void run() {
                try {
                    thread.sleep(350);
                    media = new Media(path.path);
                    mediaPlayer = new MediaPlayer(media);
                    setVolume(volume);
                    mediaPlayer.play();
                } catch (Exception e) {
                    System.out.println("Cant play song : " + path.path);
                    //e.printStackTrace();
                }

                while (true) {
                    try {
                        thread.sleep(1);
                        if (event == 1) {
                            System.out.println("Path = " + path.path);
                            mediaPlayer.stop();
                            event = 0;
                            return;
                        }
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                }
            }
        }
        thread = new Thread(new playThread());
        thread.start();
    }
}


