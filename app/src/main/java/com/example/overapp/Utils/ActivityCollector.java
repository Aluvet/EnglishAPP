package com.example.overapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.overapp.Activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

//第一行代码，用于控制活动
public class ActivityCollector  {
    //    创建活动列表，存储所有活动的Activity
    public static List<Activity> activities =new ArrayList<>();
    //    ADDactivity 和removeActivity直接在baseactivity中调用
//    像列表中添加活动，新的活动创建时，将活动添加到列表中
    public static void addActivity(Activity activity){activities.add(activity);}
    //    列表中移除活动，activity不需要，将活动移除列表
    public static void removeActivity(Activity activity){activities.remove(activity);}
    //    毁掉所有活动
//    finishall在有退出活动中调用
    public static void finishALL(){
//        通知著活动刷新
        MainActivity.FragmentRefresh = true;
//        先for循环查找活动列表
        for (Activity activity:activities)
//        如果还有没销毁的全杀了
            if (!activity.isFinishing()){
                activity.finish();
            }
    }
    // 启动新的Activity
    /*
     * 注意：
     * Context中有一个startActivity方法，Activity继承自Context，重载了startActivity方法
     * 如果使用Activity的startActivity方法，不会有任何限制
     * 而如果使用Context的startActivity方法的話，就需要开启一个新的的task
     * 遇到这个异常，是因为使用了Context的startActivity方法。解决办法是，加一个flag
     *
     */
//    没有当前活动任务的情况下启动新的活动
    public static void startOtherActivity(Context context, Class cls) {
        Intent intent = new Intent();
        intent.setClass(MyApplication.getContext(), cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
