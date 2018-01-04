package com.AttendanceSimplified.vaibhav.dtuattendance;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt3 {
    public static String key = "nevertellkd12345";

    public static byte[] encrypt2(String text) {
        String encrypt = "";
        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, aesKey);
            return cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static String decrypt2(byte[] encrypted2) {
        String decrypt = "";
        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, aesKey);
            return new String(cipher.doFinal(encrypted2));
        } catch (Exception e) {
            System.out.println(e);
            return decrypt;
        }
    }
}
