package com.example.overapp.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.overapp.R;
import com.example.overapp.Utils.MyApplication;
import com.example.overapp.broadcast.AlarmRecieve;
import com.example.overapp.config.ConfigData;

import java.util.Calendar;
//时间提醒,alarmworker
public class LearnAlarmActivity extends BaseAcyivity {
private TimePicker learn_TimePick;
private Switch alarm_Switch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_alarm);

//        初始化学习提醒界面的数据
        initAlarm();

//反回true
        if (ConfigData.getToAlarm()) {
//            将相关设置设置为true,即可选中以及可编入状态，时钟接受用户输入
            alarm_Switch.setChecked(true);
//            设置可用的视图状态。可用的视图状态的解释在子类中改变。
//                 enabled 如果可用为true，否则为false。
            learn_TimePick.setEnabled(true);
            int hour = Integer.parseInt(ConfigData.getAlarmTime().split("-")[0]);
            int minute = Integer.parseInt(ConfigData.getAlarmTime().split("-")[1]);
            learn_TimePick.setHour(hour);
            learn_TimePick.setMinute(minute);
        } else {
//            设置为不可选中
            alarm_Switch.setChecked(false);
            learn_TimePick.setEnabled(false);
        }
//        对开关等空间设置相关时间
        alarm_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
//                    如果将相关设置打开,激活空间
                    learn_TimePick.setEnabled(true);
//                    设置学习提醒
                    ConfigData.setToAlarm(true);

                }else {
                    learn_TimePick.setEnabled(false);
//                    将学习提醒设置为false‘
                    ConfigData.setToAlarm(false);
//                    并取消闹钟
                    stopLearnAlarm();

                }

            }
        });
//        给picker控件设置相关设置，当开关打开时，开启picker的
learn_TimePick.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        if (alarm_Switch.isChecked()){
            startLearnAlarm(hourOfDay, minute, false, true);
        }

    }
});
    }
    public void initAlarm(){
        learn_TimePick=findViewById(R.id.timePicker_learn);
        alarm_Switch=findViewById(R.id.switch_alarm_learn);

    }
//    创建开启方法
public static void startLearnAlarm(int hour, int minute, boolean isRepeat, boolean isTip) {
//        PendingIntent：绑定了闹钟的执行动作，比如发送一个广播、给出提示等等。不过需要注意：
//如果是通过启动服务来实现闹钟提示的话，PendingIntent对象的获取就应该采用Pending.getService (Context c,int i,Intent intent,int j)方法；
//如果是通过广播来实现闹钟提示的话，PendingIntent对象的获取就应该采用PendingIntent.getBroadcast (Context c,int i,Intent intent,int j)方法；
//如果是采用Activity的方式来实现闹钟提示的话，PendingIntent对象的获取就应该采用 PendingIntent.getActivity(Context c,int i,Intent intent,int j)方法。

//像创建intent来触发receive广播接收器
    Intent intent = new Intent(MyApplication.getContext(), AlarmRecieve.class);
//    使用setaction来为Intent对象设置动作，在广播接收器中接受，标识
    intent.setAction("intent_alarm_learn");
    PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getContext(), 0, intent, 0);
//    系统服务ALARM_SERVICE的实例
    AlarmManager alarmManager = (AlarmManager) MyApplication.getContext().getSystemService(ALARM_SERVICE);
//获取一个Calendar对象，用于设置闹钟的具体时间，并调用set设置小时分钟以及秒
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);
    calendar.set(Calendar.SECOND, 0);

    // 精确触发
    AlarmManager.AlarmClockInfo alarmClockInfo;
//    判断是否重复，
//当前时间
if (!isRepeat) {
//    当前时间比早于calandar的时间
        if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
//            则创建一个新的AlarmManager.AlarmClockInfo对象，
            alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent);
            if (isTip)
//                需要提示则弹出
                Toast.makeText(MyApplication.getContext(), "已设置" + hour + "时" + minute + "分进行学习提醒", Toast.LENGTH_SHORT).show();
        } else {
//            设置闹钟在明天的指定时间触发。
            alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000, pendingIntent);
            if (isTip)
                Toast.makeText(MyApplication.getContext(), "已设置明日" + hour + "时" + minute + "分进行学习提醒", Toast.LENGTH_SHORT).show();
        }
        ConfigData.setAlarmTime(hour + "-" + minute);
    } else {
//    设置时间重复也在第二天出发
        alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000, pendingIntent);
    }
//使用AlarmManager设置闹钟，创建的AlarmClockInfo和PendingIntent来触发。
    alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
}
public void stopLearnAlarm(){
//        通开启先建意图,出发reciever接收器
    Intent intent =new Intent(MyApplication.getContext(),AlarmRecieve.class);
//    Intent 设置了一个动作字符串 "intent_alarm_learn"
    intent.setAction("intent_alarm_learn");
//    创建挂起意图
    PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getContext(), 0, intent, 0);
//    获取alarm服务,设置取消定时任务
    AlarmManager alarmManager = (AlarmManager) MyApplication.getContext().getSystemService(ALARM_SERVICE);
//   上述创建完成后，调用cancel取消
    alarmManager.cancel(pendingIntent);
    Toast.makeText(MyApplication.getContext(),"取消学习提醒功能",Toast.LENGTH_SHORT).show();
}
}