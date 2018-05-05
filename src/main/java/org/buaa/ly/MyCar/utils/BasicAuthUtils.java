package org.buaa.ly.MyCar.utils;


import org.apache.commons.codec.binary.Base64;

public class BasicAuthUtils {

    public static String basicAuth(String username, String password) {
        String credential = username+":"+password;
        return new String(Base64.encodeBase64(credential.getBytes()));
    }

}
