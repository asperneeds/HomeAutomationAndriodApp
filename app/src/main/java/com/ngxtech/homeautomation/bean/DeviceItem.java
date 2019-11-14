package com.ngxtech.homeautomation.bean;

import java.io.Serializable;

public class DeviceItem implements Serializable {

    private String deviceMACID;
    private String _id;
    private int switchCount;
    private String ssid;
    private String date;
    private String deviceType;
    private String deviceName;
    private String device_id;
    private String category_id;


    public DeviceItem() {
    }

    public DeviceItem(String deviceMACID, int switchCount) {
        this.deviceMACID = deviceMACID;
        this.switchCount = switchCount;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public int getSwitchCount() {
        return switchCount;
    }

    public void setSwitchCount(int switchCount) {
        this.switchCount = switchCount;
    }

    public String getDeviceMACID() {
        return deviceMACID;
    }

    public void setDeviceMACID(String deviceMACID) {
        this.deviceMACID = deviceMACID;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
