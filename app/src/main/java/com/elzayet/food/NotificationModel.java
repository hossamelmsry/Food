package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class NotificationModel {
    private String id, msg,date,time,status;

    public NotificationModel() { }

    public NotificationModel(String id, String msg, String date, String time, String status) {
        this.id = id;
        this.msg = msg;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
