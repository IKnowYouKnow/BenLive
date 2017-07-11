package com.benben.qcloud.benLive.bean;

/**
 * Created by Administrator on 2017/7/11.
 */

public class Customer {
    private String s_id;
    private String wx_name;
    private String wx_code;
    private String ali_code;
    private String status;

    public String getSid() {
        return s_id;
    }

    public void setSid(String s_id) {
        this.s_id = s_id;
    }

    public String getWxname() {
        return wx_name;
    }

    public void setWxname(String wx_name) {
        this.wx_name = wx_name;
    }

    public String getWxcode() {
        return wx_code;
    }

    public void setWxcode(String wx_code) {
        this.wx_code = wx_code;
    }

    public String getAlicode() {
        return ali_code;
    }

    public void setAlicode(String ali_code) {
        this.ali_code = ali_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "s_id='" + s_id + '\'' +
                ", wx_name='" + wx_name + '\'' +
                ", wx_code='" + wx_code + '\'' +
                ", ali_code='" + ali_code + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Customer() {
    }
}
