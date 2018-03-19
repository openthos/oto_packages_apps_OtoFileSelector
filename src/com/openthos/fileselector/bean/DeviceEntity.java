package com.openthos.fileselector.bean;

import android.graphics.drawable.Drawable;

import com.openthos.fileselector.R;

public class DeviceEntity {
    private String name;
    private String devicePath;

    public DeviceEntity() {
    }

    public DeviceEntity(String name) {
        this.name = name;
    }

    public String getName() {
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
        return R.mipmap.computer;
    }
}
