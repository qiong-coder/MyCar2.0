package org.buaa.ly.MyCar.utils;


import static org.buaa.ly.MyCar.utils.TimeUtils.getOutFormat;

public class OrderUtils {

    public static String oid(int sid, int viid, String id) {
        int len = id.length();
        return String.format("%s%02X%02X%s",getOutFormat(0L),sid,viid,id.substring(len-4));
    }


}
