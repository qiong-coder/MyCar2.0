package org.buaa.ly.MyCar.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {

     static private String byteArr2hexString(byte[] bys){
         StringBuffer hexVal=new StringBuffer();
         int val=0;
         for (int i = 0; i < bys.length; i++) {
             //将byte转化为int  如果byte是一个负数就必须要和16进制的0xff做一次与运算
             val=((int)bys[i]) & 0xff;
             if(val<16) {
                 hexVal.append("0");
             }
             hexVal.append(Integer.toHexString(val));
         }

         return hexVal.toString();

     }

    public static String md5(String text) throws UnsupportedEncodingException, NoSuchAlgorithmException
    {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return byteArr2hexString(md5.digest(text.getBytes("utf-8")));
    }

}
