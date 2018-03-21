package com.openthos.fileselector.bean;

import android.content.Context;

import com.openthos.fileselector.R;
import com.openthos.fileselector.app.Constants;

public class DeviceEntity {
    private String name;
    private String devicePath;
    private int iconRes;
    private String size;
    private String usedSize;
    private String freeSize;

    public DeviceEntity() {
    }

    public String getName() {
        if (devicePath.toLowerCase().contains("usb")) {
            name = devicePath.substring(devicePath.lastIndexOf("/") + 1, devicePath.length());
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevicePath() {
        return devicePath;
    }

    public void setDevicePath(String devicePath) {
        this.devicePath = devicePath;
    }

    public int getDeviceIcon() {
        if (devicePath.equals(Constants.ROOT_PATH)) {
            iconRes = R.drawable.left_computer_selector;
        } else if (devicePath.toLowerCase().contains("usb")) {
            iconRes = R.drawable.left_usb_selector;
        }
        return iconRes;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(String usedSize) {
        this.usedSize = usedSize;
    }

    public String getFreeSize() {
        return freeSize;
    }

    public void setFreeSize(String freeSize) {
        this.freeSize = freeSize;
    }
}
