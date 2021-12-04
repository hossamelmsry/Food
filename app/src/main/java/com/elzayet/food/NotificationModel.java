package com.elzayet.food;

public class NotificationModel {
    private String id, msg,date,time;

    public NotificationModel() { }

    public NotificationModel(String id, String msg, String date, String time) {
        this.id = id;
        this.msg = msg;
        this.date = date;
        this.time = time;
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
}
