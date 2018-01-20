package com.xuf.www.gobang.db;

/**
 * Created by Admin on 2018/1/9.
 */

public class History {
    private int uid;
    private String condition;
    private String mode;
    private String mycolor;
    private String hiscolor;
    private String date;
    private String time;

    public History(int uid, String condition, String mode, String mycolor, String hiscolor, String date, String time) {
        this.uid = uid;
        this.condition = condition;
        this.mode = mode;
        this.mycolor = mycolor;
        this.hiscolor = hiscolor;
        this.date = date;
        this.time = time;
    }

    public History() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMycolor() {
        return mycolor;
    }

    public void setMycolor(String mycolor) {
        this.mycolor = mycolor;
    }

    public String getHiscolor() {
        return hiscolor;
    }

    public void setHiscolor(String hiscolor) {
        this.hiscolor = hiscolor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
