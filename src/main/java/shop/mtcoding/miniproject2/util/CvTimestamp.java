package shop.mtcoding.miniproject2.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CvTimestamp {
    public static Timestamp convertStringToTimestamp(String strDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(strDate);
            return new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer ChangeDDay(Timestamp time) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String data = sd.format(time);
        String today = sd.format(new Date(System.currentTimeMillis()));
        try {
            Date date2 = new Date(sd.parse(data).getTime());
            Date today2 = new Date(sd.parse(today).getTime());
            long gap = date2.getTime() - today2.getTime();
            int dDay = (int) (gap / (24 * 60 * 60 * 1000));
            return dDay;
        } catch (Exception e) {
            return -1;
        }
    }

}
