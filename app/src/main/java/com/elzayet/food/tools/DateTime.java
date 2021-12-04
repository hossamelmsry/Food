package com.elzayet.food.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {

    public static String nameDayInWeek     = new SimpleDateFormat("E").format(new Date());
    public static String numberOfDayInMonth= new SimpleDateFormat("d").format(new Date());
    public static String numberOfDayInTheYear= new SimpleDateFormat("D").format(new Date());
    public static String date              = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    public static String time              = new SimpleDateFormat("hh:mm:ss a").format(new Date());
    public static String dateTime          = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss a").format(new Date());

    public DateTime() { }

}