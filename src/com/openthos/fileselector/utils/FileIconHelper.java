package com.openthos.fileselector.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.openthos.fileselector.R;

public class FileIconHelper {
    private static final String SUFFIX_APE = "ape";
    private static final String SUFFIX_AVI = "avi";
    private static final String SUFFIX_DOC = "doc";
    private static final String SUFFIX_HTML = "html";
    private static final String SUFFIX_MP3 = "mp3";
    private static final String SUFFIX_MP4 = "mp4";
    private static final String SUFFIX_PPT = "ppt";
    private static final String SUFFIX_TXT = "txt";
    private static final String SUFFIX_WAV = "wav";
    private static final String SUFFIX_WMV = "wmv";
    private static final String SUFFIX_PDF = "pdf";
    private static final String SUFFIX_RM = "rm";
    private static final String SUFFIX_XLS = "xls";
    private static final String SUFFIX_RMVB = "rmvb";
    private static final String SUFFIX_TAR = "tar";
    private static final String SUFFIX_BZ2 = "bz2";
    private static final String SUFFIX_GZ = "gz";
    private static final String SUFFIX_ZIP = "zip";
    private static final String SUFFIX_RAR = "rar";
    private static final String SUFFIX_XLSX = "xlsx";
    private static final String SUFFIX_DOCX = "docx";
    private static final String SUFFIX_PPTX = "pptx";
    private static final String SUFFIX_3GP = "3gp";
    private static final String SUFFIX_BMP = "bmp";
    private static final String SUFFIX_Gif = "gif";
    private static final String SUFFIX_PNG = "png";
    private static final String SUFFIX_ISO = "iso";
    private static final String SUFFIX_JPG = "jpg";
    private static final String SUFFIX_JPEG = "jpeg";

    public static int getFileIcon(String path) {
        if (path.endsWith(SUFFIX_APE)) {
            return R.mipmap.suffix_ape;
        } else if (path.endsWith(SUFFIX_AVI)) {
            return R.mipmap.suffix_avi;
        } else if (path.endsWith(SUFFIX_DOC) || path.endsWith(SUFFIX_DOCX)) {
            return R.mipmap.suffix_doc;
        } else if (path.endsWith(SUFFIX_HTML)) {
            return R.mipmap.suffix_html;
        } else if (path.endsWith(SUFFIX_MP3)) {
            return R.mipmap.suffix_mp3;
        } else if (path.endsWith(SUFFIX_MP4)) {
            return R.mipmap.suffix_mp4;
        } else if (path.endsWith(SUFFIX_PPT) || path.endsWith(SUFFIX_PPTX)) {
            return R.mipmap.suffix_ppt;
        } else if (path.endsWith(SUFFIX_TXT)) {
            return R.mipmap.suffix_txt;
        } else if (path.endsWith(SUFFIX_WAV)) {
            return R.mipmap.suffix_wav;
        } else if (path.endsWith(SUFFIX_WMV)) {
            return R.mipmap.suffix_wmv;
        } else if (path.endsWith(SUFFIX_XLS) || path.endsWith(SUFFIX_XLSX)) {
            return R.mipmap.suffix_xls;
        } else if (path.endsWith(SUFFIX_PDF)) {
            return R.mipmap.suffix_pdf;
        } else if (path.endsWith(SUFFIX_RM)) {
            return R.mipmap.suffix_rm;
        } else if (path.endsWith(SUFFIX_RMVB)) {
            return R.mipmap.suffix_rmvb;
        } else if (path.endsWith(SUFFIX_TAR)) {
            return R.mipmap.suffix_tar;
        } else if (path.endsWith(SUFFIX_BZ2)) {
            return R.mipmap.suffix_bz2;
        } else if (path.endsWith(SUFFIX_GZ)) {
            return R.mipmap.suffix_gz;
        } else if (path.endsWith(SUFFIX_ZIP)) {
            return R.mipmap.suffix_zip;
        } else if (path.endsWith(SUFFIX_RAR)) {
            return R.mipmap.suffix_rar;
        } else if (path.endsWith(SUFFIX_3GP)) {
            return R.mipmap.suffix_3gp;
        } else if (path.endsWith(SUFFIX_BMP)) {
            return R.mipmap.suffix_bmp;
        } else if (path.endsWith(SUFFIX_PNG)) {
            return R.mipmap.suffix_png;
        } else if (path.endsWith(SUFFIX_Gif)) {
            return R.mipmap.suffix_gif;
        } else if (path.endsWith(SUFFIX_JPG)) {
            return R.mipmap.suffix_jpg;
        } else if (path.endsWith(SUFFIX_JPEG)) {
            return R.mipmap.suffix_jpeg;
        } else if (path.endsWith(SUFFIX_ISO)) {
            return R.mipmap.suffix_iso;
        } else {
            return R.mipmap.suffix_default;
        }
    }

    public static Drawable getAppDrawable(Context context, String filePath) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(filePath,
                    PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                final ApplicationInfo appInfo = packageInfo.applicationInfo;
                appInfo.sourceDir = filePath;
                appInfo.publicSourceDir = filePath;
                return pm.getDrawable(appInfo.packageName, appInfo.icon, appInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getResources().getDrawable(R.mipmap.suffix_default);
    }
}
