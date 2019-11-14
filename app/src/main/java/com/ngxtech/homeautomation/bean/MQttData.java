package com.ngxtech.homeautomation.bean;

public class MQttData {

    private String public_id;
    private String port;
    private String user;
    private String password;
    private String topic;
    private String payroll;

    public String getPublic_id() {
        return public_id;
    }

    public String getPassword() {
        return password;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPayroll() {
        return payroll;
    }

    public String getTopic() {
        return topic;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPublic_id(String public_id) {
        this.public_id = public_id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPayroll(String payroll) {
        this.payroll = payroll;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
