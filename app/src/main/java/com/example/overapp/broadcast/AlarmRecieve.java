package com.example.overapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;


import com.example.overapp.Activity.LearnAlarmActivity;
import com.example.overapp.Worker.LearnAlarmWorker;
import com.example.overapp.config.ConfigData;
//广播接收器
public class AlarmRecieve extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//创建 接受alarmactivity的广播动作并存入action变量中
        String action = intent.getAction();
        Log.d("AlarmRecieve","onReceive: " + action);
//管理任务请求和任务队列，发起的 WorkRequest 会进入它的任务队列。
        // 当接收到特定的广播时，使用WorkManager来调度一个后台任务
      // 这个任务由LearnAlarmWorker类来实现
        WorkManager.getInstance().enqueue(new OneTimeWorkRequest.Builder(LearnAlarmWorker.class).build());
//当广播接收器接收到广播时，它使用WorkManager来调度一个一次性工作请求。这个工作请求会执行LearnAlarmWorker类中定义的后台任务。

        // 重复定时
        // 第二天的这个时间再提醒
        if (ConfigData.getToAlarm()) {
            int hour = Integer.parseInt(ConfigData.getAlarmTime().split("-")[0]);
            int minute = Integer.parseInt(ConfigData.getAlarmTime().split("-")[1]);
//            可重复
            LearnAlarmActivity.startLearnAlarm(hour, minute, true, false);
        }
    }

}
