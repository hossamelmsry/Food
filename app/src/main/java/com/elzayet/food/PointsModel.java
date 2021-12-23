package com.elzayet.food;

import androidx.annotation.Keep;

@Keep
public class PointsModel {
    private String points;
    private String dayOfWeek;
    private int dayLocker;

    public PointsModel() { }

    public PointsModel(String points) {
        this.points = points;
    }

    public PointsModel(String dayOfWeek, int dayLocker) {
        this.dayOfWeek = dayOfWeek;
        this.dayLocker = dayLocker;
    }

    public String getPoints() {
        return points;
    }

    public String getDayOfWeek() { return dayOfWeek; }

    public int getDayLocker() { return dayLocker;
   }
}
