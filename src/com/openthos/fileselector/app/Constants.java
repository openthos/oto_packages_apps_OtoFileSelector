package com.openthos.fileselector.app;

import android.os.Environment;

public class Constants {
    public static final String ROOT_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DESKTOP_PATH = ROOT_PATH + "/Desktop";
    public static final String MUSIC_PATH = ROOT_PATH + "/Music";
    public static final String VIDEOS_PATH = ROOT_PATH + "/Movies";
    public static final String PICTURES_PATH = ROOT_PATH + "/Pictures";
    public static final String DOCUMENT_PATH = ROOT_PATH + "/documents";
    public static final String DOWNLOAD_PATH = ROOT_PATH + "/Download";
    public static final String RECYCLE_PATH = ROOT_PATH + "/Recycle";

    public static final int DOUBLE_CLICK_TIME = 500;
    public static final int USB_REFRESH_TIME = 1000;
    public static final int HEIGHT_MASK = 0x3fffffff;
}
