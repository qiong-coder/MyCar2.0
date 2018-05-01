package org.buaa.ly.MyCar.utils;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public final static long MILLIS_PER_SECOND = 1000L;

    private static SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String getDateFormat(Calendar calendar) {
        return dateFormat.format(calendar);
    }

    public static String getOutFormat(Long expire) {
        Date now  = new Date(System.currentTimeMillis()+expire*MILLIS_PER_SECOND);
        return outFormat.format(now);
    }

    public static int days(Timestamp begin , Timestamp end) {
        return Long.valueOf((end.getTime() - begin.getTime()) / (24 * 60 * 3600 * MILLIS_PER_SECOND)).intValue();
    }
}
