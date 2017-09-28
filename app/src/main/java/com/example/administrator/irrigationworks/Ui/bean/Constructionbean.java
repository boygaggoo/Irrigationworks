package com.example.administrator.irrigationworks.Ui.bean;

import com.example.administrator.irrigationworks.Ui.litepal.InspectorDataLitepal;

/**
 * Created by Administrator on 2017/9/5.
 */
public class Constructionbean {
    private  String timestamp;
    private  long machineIdentifier;
    private  long processIdentifier;
    private  long counter;
    private  long time;
    private  String date;
    private  long timeSecond;

    public Constructionbean() {

    }

    public Constructionbean(String timestamp, long machineIdentifier, long processIdentifier, long counter, long time, String date, long timeSecond) {
        this.timestamp = timestamp;
        this.machineIdentifier = machineIdentifier;
        this.processIdentifier = processIdentifier;
        this.counter = counter;
        this.time = time;
        this.date = date;
        this.timeSecond = timeSecond;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getMachineIdentifier() {
        return machineIdentifier;
    }

    public void setMachineIdentifier(long machineIdentifier) {
        this.machineIdentifier = machineIdentifier;
    }

    public long getProcessIdentifier() {
        return processIdentifier;
    }

    public void setProcessIdentifier(long processIdentifier) {
        this.processIdentifier = processIdentifier;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimeSecond() {
        return timeSecond;
    }

    public void setTimeSecond(long timeSecond) {
        this.timeSecond = timeSecond;
    }

    @Override
    public String toString() {
        return "Constructionbean{" +
                "timestamp='" + timestamp + '\'' +
                ", machineIdentifier=" + machineIdentifier +
                ", processIdentifier=" + processIdentifier +
                ", counter=" + counter +
                ", time=" + time +
                ", date='" + date + '\'' +
                ", timeSecond=" + timeSecond +
                '}';
    }
}
