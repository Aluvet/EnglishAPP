package com.example.overapp.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.overapp.Utils.MyApplication;

public class ConfigData {
//    SharedPreferences是Android平台上一个轻量级的存储辅助类，用来保存应用的一些常用配置
//    全局调用存放的
//    存放名称
    public static String sharedPfName = "configData";
    public static boolean ReChooseBook = false;
    //    是否是第一次登陆

    public  static boolean isFirst;
    public  static  String  isFirstName ="isFIrst";

    // 是否已登录
    public static boolean isLogged;
    public static String isLoggedName = "isLogged";


//当前一登陆
    public  static int loggedNum;
    public  static String loggednumName= "loggedNum";

    // 是否为夜间模式
    public static boolean isNight;
    public static String isNightName = "isNight";

    // 是否需要学习提醒
    public static boolean isAlarm;
    public static String isAlarmName = "isAlarm";

    // 学习提醒的时间
    public static String alarmTime;
    public static String alarmTimeName = "alarmTime";
    // 是否为修改计划
    // 0为否，1为是
    public static final String UPDATE_NAME = "update";
    public static final int isUpdate = 1;
    public static final int notUpdate = 0;


    // Z在修改计划部分设置单词匹配的数量
    public static int matchPlanNum;
    public static String matchPlanNumName = "matchPlanNum";

//    同上，设置单词速过的个数
// 当前单词速过的数量
    public static int speedPlanNum;
    public static String speedPlanNumName = "speedPlanNum";


//    welcome中设置相关数据
//    读取数据，现检索如果没有则第一次执行，默认true
    public static boolean getisFirst(){
        SharedPreferences preferences =MyApplication.getContext().getSharedPreferences(sharedPfName,Context.MODE_PRIVATE);

        isFirst =preferences.getBoolean(isFirstName,true);
        return isFirst;
    }
//    向sharename中写入数据 ，在执行set后将isfirst写为false
    public static void setIsFirst(boolean isFirst){
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences(sharedPfName,Context.MODE_PRIVATE).edit();
        editor.putBoolean(isFirstName,isFirst);
//        commit和apply都是提交，对shareparents的提交删除等操作都需要apply和commit
//        apply异步进行，
        editor.apply();
    }
    public static boolean getIsLogged() {
//        getSharedPreferences(SharedDataName, Context.MODE_PRIVATE);获取数据
//        Context.MODE_PRIVATE：为默认操作模式,代表该文件是私有数据,只能被应用本身访问,在该模式下,写入的内容会覆盖原文件的内容

        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE);
//        get读取boolean类型，最初调用无对应键值对默认为false
        isLogged = preferences.getBoolean(isLoggedName, false);

        return isLogged;
    }

    // 设置isLogged值
    public static void setIsLogged(boolean isLogged) {
        //获取Editor对象的引用读取数据
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE).edit();
        editor.putBoolean(isLoggedName, isLogged);
        editor.apply();
    }

//    获取已登陆用户的值
    public static int getLoggedNum(){
        SharedPreferences sharedPreferences =MyApplication.getContext().getSharedPreferences(sharedPfName,Context.MODE_PRIVATE);
        loggedNum= sharedPreferences.getInt(loggednumName,0);
        return loggedNum;
    }
//    设置修改
    public static void setLoggedNum(int loggedNum){
        SharedPreferences.Editor editor =MyApplication.getContext().getSharedPreferences(sharedPfName,Context.MODE_PRIVATE).edit();
        editor.putInt(loggednumName,loggedNum);
        editor.apply();
    }


    // 获得当前是否需要学习提醒
    public static boolean getToAlarm() {
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE);
        isAlarm = preferences.getBoolean(isAlarmName, false);
        return isAlarm;
    }

    // 设置当前是否需要学习提醒
    public static void setToAlarm(boolean isAlarm) {
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE).edit();
        editor.putBoolean(isAlarmName, isAlarm);
        editor.apply();
    }
    // 获得学习提醒的时间
    public static String getAlarmTime() {
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE);
        alarmTime = preferences.getString(alarmTimeName, "null");
        return alarmTime;
    }

    // 设置学习提醒的时间
    public static void setAlarmTime(String alarmTime) {
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE).edit();
        editor.putString(alarmTimeName, alarmTime);
        editor.apply();
    }
    // 获得单词匹配的数量
    public static int getMatchPlanNum() {
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE);
        matchPlanNum = preferences.getInt(matchPlanNumName, 5);
        return matchPlanNum;
    }

    // 设置单词匹配的数量
    public static void setMatchPlanNum(int matchPlanNum) {
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE).edit();
        editor.putInt(matchPlanNumName, matchPlanNum);
        editor.apply();
    }

    // 获得单词速过的数量
    public static int getSpeedPlanNum() {
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE);
        speedPlanNum = preferences.getInt(speedPlanNumName, 5);
        return speedPlanNum;
    }

    // 设置单词匹配的数量
    public static void setSpeedPlanNum(int speedPlanNum) {
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE).edit();
        editor.putInt(speedPlanNumName, speedPlanNum);
        editor.apply();
    }
    // 获得当前是否为夜间模式
    public static boolean getIsNight() {
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE);
        isNight = preferences.getBoolean(isNightName, false);
        return isNight;
    }

    // 设置当前是否为夜间模式
    public static void setIsNight(boolean isNight) {
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences(sharedPfName, Context.MODE_PRIVATE).edit();
        editor.putBoolean(isNightName, isNight);
        editor.apply();
    }
}
