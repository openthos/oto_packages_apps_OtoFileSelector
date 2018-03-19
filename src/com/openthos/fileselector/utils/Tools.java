package com.openthos.fileselector.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {
    public static String transformTimeStr(long time) {
        try {
            return getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
        } catch (Exception e) {
            return null;
        }
    }

    private static SimpleDateFormat getSimpleDateFormat(String format) {
        return new SimpleDateFormat(format);
    }
}
