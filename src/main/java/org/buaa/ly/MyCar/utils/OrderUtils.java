package org.buaa.ly.MyCar.utils;


import static org.buaa.ly.MyCar.utils.TimeUtils.getOutFormat;

public class OrderUtils {

    public static String oid(int sid, int viid, int id) {
        return String.format("%s%02X%02X%02X",getOutFormat(0L),sid,viid,id);
    }


}
