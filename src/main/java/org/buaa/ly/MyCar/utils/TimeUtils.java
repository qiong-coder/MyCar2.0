package org.buaa.ly.MyCar.utils;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public final static long MILLIS_PER_SECOND = 1000L;
    public final static long MILLIS_PER_MINUTES = 60 * MILLIS_PER_SECOND;
    public final static long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTES;
    public final static long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    private static SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String getDateFormat(Date calendar) {
        return dateFormat.format(calendar);
    }

    public static String dateFormat(Date date, String f) {
        SimpleDateFormat format = new SimpleDateFormat(f);
        return format.format(date);
    }

    public static String getOutFormat(Long expire) {
        Date now  = new Date(System.currentTimeMillis()+expire*MILLIS_PER_SECOND);
        return outFormat.format(now);
    }



    public static int days(Timestamp begin , Timestamp end) {
        return (int)hour(end)/24 - (int)hour(begin)/24;
    }

    public static int daysByTime(Timestamp begin, Timestamp end) {
        return Long.valueOf((end.getTime() - begin.getTime() + MILLIS_PER_DAY - 1) / MILLIS_PER_DAY).intValue();
    }

//    public static int hour(Timestamp timestamp) {
//        return Long.valueOf((timestamp.getTime()-timestamp.getTimezoneOffset()*MILLIS_PER_MINUTES)/MILLIS_PER_HOUR).intValue();
//    }

    public static float hour(Timestamp timestamp) {
        return Double.valueOf((timestamp.getTime()*1.0-timestamp.getTimezoneOffset()*MILLIS_PER_MINUTES)/MILLIS_PER_HOUR).floatValue();
    }

    public static Timestamp downByDay(Timestamp timestamp) {
        return new Timestamp((timestamp.getTime()-timestamp.getTimezoneOffset()*MILLIS_PER_MINUTES)/MILLIS_PER_DAY*MILLIS_PER_DAY + timestamp.getTimezoneOffset()*MILLIS_PER_MINUTES);
    }

    public static Timestamp upByDay(Timestamp timestamp) {
        return new Timestamp( (timestamp.getTime()-timestamp.getTimezoneOffset()*MILLIS_PER_MINUTES + MILLIS_PER_DAY - 1)/MILLIS_PER_DAY*MILLIS_PER_DAY + timestamp.getTimezoneOffset()*MILLIS_PER_MINUTES);
    }
}
