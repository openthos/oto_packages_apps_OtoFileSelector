package com.openthos.fileselector.utils;

import android.text.TextUtils;

import com.openthos.fileselector.bean.DeviceEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DeviceUtils {
    private static String ONE_OR_MORE_SPACE = "\\s+";

    public static List<DeviceEntity> getUsbList() {
        List<DeviceEntity> usbs = new ArrayList<>();
        BufferedReader reader = null;
        String line = "";
        try {
            Process pro = Runtime.getRuntime().exec("df");
            reader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().startsWith("/storage/usb")) {
                    DeviceEntity deviceEntity = parseDeviceEntity(line);
                    if (deviceEntity != null) {
                        usbs.add(deviceEntity);
                    }
                }
            }
            return usbs;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return usbs;
    }

    private static DeviceEntity parseDeviceEntity(String line) {
        try {
            if (!TextUtils.isEmpty(line)) {
                DeviceEntity deviceEntity = new DeviceEntity();
                String[] split = line.split(ONE_OR_MORE_SPACE);
                deviceEntity.setDevicePath(split[0]);
                deviceEntity.setSize(split[1]);
                deviceEntity.setUsedSize(split[2]);
                deviceEntity.setFreeSize(split[3]);
                return deviceEntity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
