package com.ngxtech.homeautomation.bean;

public class Switch {
    private String _id;
    private String switchName;
    private boolean status;
    private String [] switchNames;

    private String device_id;


    public String[] getSwitchNames() {
        return switchNames;
    }

    public void setSwitchNames(String[] switchNames) {
        this.switchNames = switchNames;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String SwitchName) {
       this.switchName = SwitchName;
    }

    public Switch() {

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Switch(String switchName) {
        this.switchName = switchName;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
