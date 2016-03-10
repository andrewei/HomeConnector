package aplication;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Created by andreas on 1/10/2016.
 */
public class Mp3Player {

    private Thread thread;
    private static MediaPlayer mediaPlayer;
    private static int event = 0;
    private double volume = .5;
    private MainController main;

    public Mp3Player(MainController main){
        this.main = main;
        //must have a file to initiate the mediaplayer
        Media media = new Media("file:///F:/mp3/mp3z/Xzibit-X.mp3");
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0);

    }

    class Path{
        public String path;
    }

    public double getCurrentTime(){
        Duration test =  mediaPlayer.getCurrentTime();
        //System.out.println("currentTime: " + test.toString());
        return test.toMillis();
    }

    public void setCurrentTime(int time){
        mediaPlayer.seek(new Duration(time));
    }


    public double getTotalDuration(){
        Duration test =  mediaPlayer.getTotalDuration();
       // System.out.println("TotalTime:" + test.toString());
        return test.toMillis();
    }


    public void stop(){
        mediaPlayer.stop();
    }
    public void play(){
        mediaPlayer.play();
    }

    public void play(String songPath){

        final Path path = new Path();
        path.path = songPath;

        if(thread != null){
            System.out.println("Resetting thread");
            event = 1;
        }

        class playThread implements Runnable {

            public void run(){
                try {
                    thread.sleep(350);
                    System.out.println("playThread is running");
                    Media media = new Media(path.path);
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("END OF FILE");
                            main.btn_next_click(null);
                        }
                    });
                    mediaPlayer.setOnError(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("ERROR WHEN PLAYNG FILE, SKIPS");
                            main.btn_next_click(null);
                        }
                    });
                    mediaPlayer.setVolume(0);
                    mediaPlayer.play();
                } catch (Exception e) {
                    System.out.println("Cant play song : " + path.path);
                    //e.printStackTrace();
                }

                while(true){
                    try {
                        thread.sleep(1);
                        if(event == 1){
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