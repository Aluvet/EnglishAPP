package com.example.overapp.Worker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.overapp.R;
import com.example.overapp.Utils.MyApplication;
import com.example.overapp.config.ConstantData;

//创建后台发送通知来通知用户
public class LearnAlarmWorker extends Worker {


    public LearnAlarmWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
// work类的核心方法  执行后台工作
    @NonNull
    @Override
    public  Result doWork() {
//      在worker中设置dowork运行
//        设置notificationmanager实例,用于发布和管理通知的系统服务
        NotificationManager notificationManager = (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        创建通知渠道
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//创建渠道,id name 渠道重要性
           notificationManager.createNotificationChannel(new NotificationChannel(ConstantData.channelId, ConstantData.channelName, NotificationManager.IMPORTANCE_HIGH));
        }
//创建通知实例 通知的标题,文本,时间,点击通知是自动取消,小图标,优先级,以及设置图标大图标
        Notification notification = new NotificationCompat.Builder(MyApplication.getContext(), ConstantData.channelId).setContentTitle("到背单词时间了！")
                .setContentText("").setWhen(System.currentTimeMillis()).setAutoCancel(true).setSmallIcon(R.drawable.icon_tip).setPriority(Notification.PRIORITY_MAX)
                .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.ic_launcher))
                .build();
//         发布通知，使用通知
        notificationManager.notify(1, notification);
//返回后台任务已完成
        return Result.success();
    }
}
