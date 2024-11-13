package dev.demo.coindeskDemo.utils;

import java.text.SimpleDateFormat;

public class DateUtils {
    public static String formatTime(String isoTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return outputFormat.format(inputFormat.parse(isoTime));
        } catch (Exception e) {
            return isoTime;
        }
    }
}
