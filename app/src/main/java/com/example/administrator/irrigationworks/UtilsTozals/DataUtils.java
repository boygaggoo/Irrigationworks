package com.example.administrator.irrigationworks.UtilsTozals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/13.
 */
public class DataUtils {
    /**
     * 日期转为Calendar
     * @param date
     * @return
     */
    public static Calendar dataToCalendar(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    public static java.util.Date  dataToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(("yyyy-MM-dd"));
        java.util.Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
