package com.wzes.huddle.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {
    public static boolean ltTwo(String d1, String d2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        try {
            if ((simpleDateFormat.parse(d2).getTime() - simpleDateFormat.parse(d1).getTime()) / 60000 <= 3) {
                return true;
            }
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
