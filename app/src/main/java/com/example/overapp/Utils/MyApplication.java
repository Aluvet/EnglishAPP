package com.example.overapp.Utils;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.overapp.config.ConfigData;

import org.litepal.LitePal;
//继承Application类并重写其onCreate方法，在应用启动时执行一些初始化操作
//根据第一行代码设置,MyApplication需要在AndroidManifest.xml文件中被声明为应用的application标签的name属性
public class MyApplication extends Application {
    //    继承Application使用单例全局使用context，使用的话直接调用
//    设置变量让context调用
//    全局Context
    private static Context context;
    @Override
    public  void  onCreate(){
        super.onCreate();
//        这行代码获取了应用程序的上下文（Context）
//        应用的任何位置获取到这个全局的Context
//        无需在每个Activity或Fragment中传递Context
        context =getApplicationContext();
        //初始化Litepal
        LitePal.initialize(this);
//        如果ConfigData.getToNight()返回true，则应用将使用夜间模式；否则，应用将使用普通模式。



        /*AppCompatDelegate:
          AppCompatDelegate有四种模式可以设置:
          MODE_NIGHT_YES:直接指定夜间模式
          MODE_NIGHT_NO：直接指定日间模式
           MODE_NIGHT_FOLLOW_SYSTEM:根据系统设置决定是否设置夜间模式
          MODE_NIGHT_AUTO:根据当前时间自动切换模式*/
//          夜间
//         AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if (ConfigData.getIsNight()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    /* 获取全局上下文*/
    public static Context getContext(){return context;}
}
