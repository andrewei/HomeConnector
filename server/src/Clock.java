import java.time.LocalDate;
import java.util.Calendar;
import java.util.TimerTask;

/**
 * Created by andreas on 3/10/2016.
 */
public class Clock extends TimerTask{
    MainController mainController;

    public Clock(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void run() {
        mainController.str_date.setText(String.valueOf(LocalDate.now()));
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        if (hour < 10){
            mainController.str_hour.setText("0" + hour);
        }
        else{
            mainController.str_hour.setText("" + hour);
        }
        int minute = rightNow.get(Calendar.MINUTE);
        if(minute < 10){
            mainController.str_minute.setText("0" + minute);
        }
        else{
            mainController.str_minute.setText("" + minute);
        }
    }
}
