package com.example.overapp.database;

import org.litepal.crud.LitePalSupport;
//后台服务器用
public class LearnTime extends LitePalSupport {

    // 日期
    private String date;

    // 学习时间
    private String time;

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
