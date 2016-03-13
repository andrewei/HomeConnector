package aplication;

import java.io.File;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.TimerTask;

public class Clock extends TimerTask{

    private MainController mainController;
    private boolean isAlarmSet;
    private int alarmHour;
    private int alarmMinute;
    private File alarmSong;
    public Clock(MainController mainController) {
        this.alarmSong = null;
        this.mainController = mainController;
        this.isAlarmSet = false;
        this.alarmHour = 0;
        this.alarmMinute = 0;
    }

    @Override
    public void run() {

        mainController.str_date.setText(String.valueOf(LocalDate.now()));
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);

        if (hour < 10){
            mainController.str_hour.setText("0" + hour);
        } else{
            mainController.str_hour.setText("" + hour);
        }

        if(minute < 10){
            mainController.str_minute.setText("0" + minute);
        } else{
            mainController.str_minute.setText("" + minute);
        }
        checkAlarm(hour,minute);

    }

    public void setAlarmsong(File alarmSong){
        this.alarmSong = alarmSong;
    }

    public void checkAlarm(int nowHour, int nowMinute){
        if (this.alarmHour == nowHour && this.alarmMinute == nowMinute && isAlarmSet){
            System.out.printf("ALARM");
            if(alarmSong == null){
                mainController.playSong("file:///F:/mp3/doorbell/1.mp3");
            }
            else {
                mainController.playSong(alarmSong);
            }
            isAlarmSet = false;
        }
    }

    public void setAlarm(int hour, int minute, boolean setAlarm){
        this.alarmHour = hour;
        this.alarmMinute = minute;
        this.isAlarmSet = setAlarm;
    }


}
