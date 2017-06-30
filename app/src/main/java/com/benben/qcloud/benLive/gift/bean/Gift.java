package com.benben.qcloud.benLive.gift.bean;


public class Gift {
    private String g_id;

    private String g_name;

    private String g_thumb;

    private String g_price;

    public Gift(String g_id, String g_name, String g_thumb, String g_price) {
        this.g_id = g_id;
        this.g_name = g_name;
        this.g_thumb = g_thumb;
        this.g_price = g_price;
    }

    @Override
    public String toString() {
        return "Gift{" +
                "g_id='" + g_id + '\'' +
                ", g_name='" + g_name + '\'' +
                ", g_thumb='" + g_thumb + '\'' +
                ", g_price=" + g_price +
                '}';
    }

    public String getId() {
        return g_id;
    }

    public void setId(String g_id) {
        this.g_id = g_id;
    }

    public String getGname() {
        return g_name;
    }

    public void setGname(String g_name) {
        this.g_name = g_name;
    }

    public String getGurl() {
        return g_thumb;
    }

    public void setGurl(String g_thumb) {
        this.g_thumb = g_thumb;
    }

    public String getGprice() {
        return g_price;
    }

    public Gift() {
    }

    public void setGprice(String g_price) {
        this.g_price = g_price;
    }

}