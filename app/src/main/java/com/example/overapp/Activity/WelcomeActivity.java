package com.example.overapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.overapp.Interface.PermissionListener;
import com.example.overapp.R;
import com.example.overapp.Utils.ActivityCollector;
import com.example.overapp.Utils.BaiduHelper;
import com.example.overapp.Utils.PopupWindow;
import com.example.overapp.Utils.TimeController;
import com.example.overapp.config.ConfigData;
import com.example.overapp.config.ConstantData;
import com.example.overapp.database.DailyData;
import com.example.overapp.database.UserConfig;


import org.litepal.LitePal;

import java.util.List;



//接口口方式实现点击事件 里面onClick
public class WelcomeActivity extends BaseAcyivity implements View.OnClickListener {
    private String rootPath;
    private CardView Agree;
    // 弹出-不同意按钮
    private TextView NotAgree;
    // 壁纸
    private static final String TAG = "WelcomeActivity";
    private ImageView imgBackground;
    // 每日一句卡片
    private CardView cardWelCome;
    // 每日一句文字
    private TextView textWelCome;
      // 缩放动画
    private ScaleAnimation animation;
    private final int FINISH = 1;
    private PopupWindow welWindow;
//    线程
@SuppressLint("HandlerLeak")
private Handler handler = new Handler(){
    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case FINISH:
                List<DailyData> dailyDataList = LitePal.where("dayTime = ?", TimeController.getCurrentDateStamp() + "").find(DailyData.class);
                if (!dailyDataList.isEmpty()) {
                    DailyData dailyData = dailyDataList.get(0);
                    textWelCome.setText(dailyData.getDailyEn());
                    if (dailyData.getPicVertical()!=null) {
                        Glide.with(WelcomeActivity.this).load(dailyData.getPicHorizontal()).into(imgBackground);
                    }
                break;
        }
    }}
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        获取Android设备上的外部存储的根目录路径
//        Environment.getDataDirectory().getPath() :
//        获得根目录/data (内部存储路径)
        rootPath = Environment.getDataDirectory().getPath();
        Log.d(TAG, "路径" + rootPath);
//        检查启动当前Activity的Intent是否设置了FLAG_ACTIVITY_BROUGHT_TO_FRONT标志位。
//        如果设置了，从ActivityCollector中移除这个Activity，并结束
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            ActivityCollector.removeActivity(this);
            finish();
            return;
        }

        //初始化后须初始化需创建方法，更直观
//        弹窗权限
       initwel();
        // 设置透明度
        cardWelCome.getBackground().setAlpha(200);
//new Thread创建新的线程
        // 设置数据显示
        new Thread(() -> {
            prepareDailyData();
            Message message = new Message();
            message.what = FINISH;
            handler.sendMessage(message);
            BaiduHelper.getAccessToken();
        }).start();
//        判断是否为第一次登陆
//        如果是第一次登陆
        if (ConfigData.getisFirst()) {
//        弹出权限狂
            /*设置PopupWindow弹出时是否模糊背景。  在使用模糊背景前，可以通过setBlurOption(PopupBlurOption)传入模糊配置。
            * */
            welWindow.setClipChildren(false)

                    .setBlurBackgroundEnable(true)
//            是否允许点击PopupWindow外部时触发窗口是否关闭
                    .setOutSideDismiss(false)
//                    调用这个方法时，将会展示PopupWindow。
                    .showPopupWindow();
//            打印boolen值并转化为字符串
       Log.d("WelcomeActivity","第一次运行："+(ConfigData.getisFirst()));
        }else{
//            将显示封面动画
//            延迟
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgBackground.startAnimation(animation);
                }
            }, 600);
        }
//设置学习提醒
//        检查是否设置了闹钟或提醒，设置，则获取闹钟或提醒的时间，并启动一个闹钟或提醒活动来设置这个时间
        if (ConfigData.getToAlarm()) {
            int hour = Integer.parseInt(ConfigData.getAlarmTime().split("-")[0]);
            int minute = Integer.parseInt(ConfigData.getAlarmTime().split("-")[1]);
            LearnAlarmActivity.startLearnAlarm(hour, minute, false, false);
        }
    }
    private  void  initwel(){
        welWindow = new PopupWindow(this);
        Agree = welWindow.findViewById(R.id.agree);
        Agree.setOnClickListener(this);
        NotAgree = welWindow.findViewById(R.id.not_agree);
        NotAgree.setOnClickListener(this);
        cardWelCome = findViewById(R.id.card_wel);
        textWelCome = findViewById(R.id.text_wel);
        imgBackground = findViewById(R.id.img_wel);
        animationConfig();
    }
//    对提示弹出点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            点击同意
            case R.id.agree:
//                请求对应权限
                requestPemission();
                break;
            case R.id.not_agree:
//                不同意,弹出提示框并调用finishall直接退出程序
                Toast.makeText(this, "抱歉，程序将会退出", Toast.LENGTH_SHORT).show();
                ActivityCollector.finishALL();
                break;
        }
    }
//    base中封装权限，this调用
//    请求应用程序所需权限
    private void requestPemission(){
//        调用BaseActivity中的requestRunPermission方法进行权限请求，权限列表，以及回调接口
        requestRunPermission(ConstantData.permissions, new PermissionListener() {
            @Override
//       PermissionListener方法中的接口     所有请求的权限都被授予时被调用
            public void onGranted() {
//授权操作将弹窗关闭
                welWindow.dismiss();
//                申请完权限将第一次启动去掉
                ConfigData.setIsFirst(false);
                Log.d("WelcomeActivity","修改第一次登陆："+(ConfigData.getisFirst()));
                // 延迟时间再开始动画
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgBackground.startAnimation(animation);
                    }
                }, PopupWindow.animatTime);
            }
//拒绝退出，并调用ActivityColector中的finishall方法结束应用的所有活动
            @Override
            public void onDenied(List<String> deniedPermission) {
                if (!deniedPermission.isEmpty()) {
                    Toast.makeText(WelcomeActivity.this, "无法获得权限，程序即将退出", Toast.LENGTH_SHORT).show();
                    ActivityCollector.finishALL();
                }
            }
        });
    }

    // 缩放动画配置
    private void animationConfig() {
        // 从原图大小，放大到1.5倍
        animation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, 1, 0.5f);


        // 设置持续时间
        animation.setDuration(4000);
        // 设置动画结束之后的状态是否是动画的最终状态
        animation.setFillAfter(true);
        // 设置循环次数
        animation.setRepeatCount(0);
        // 设置动画结束后事件
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//              // 动画结束时执行的代码，启动新的Activity
         if (ConfigData.getIsLogged()) {
             // 已登录，进入首页/选择词书
             if (ConfigData.getIsLogged()) {
//                 在用户数据库使用litepal查询与登录num相同的用户id
                 List<UserConfig> userConfigs = LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserConfig.class);
//                 判断如果用户没有选择书
                 if (userConfigs.get(0).getCurrentBookId() == -1) {
//                     跳转到选择书籍进行操作
                     Intent intent = new Intent(WelcomeActivity.this, ChooseWordBookActivity.class);
                     startActivity(intent);
                 }
//                 如果选择了书记，但是还没有进行相关在操作跳转到计划进行数据操作
                 else if (userConfigs.get(0).getCurrentBookId() != -1 && userConfigs.get(0).getWordNeedReciteNum() == 0) {
                     Intent intent = new Intent(WelcomeActivity.this, ChangeLearnActivity.class);
                     startActivity(intent);
                 } else {
//已经学过直接进入主页面
                     Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                     startActivity(intent);
                 }
             }
           } else {
               // 用户未登录，跳转到登录界面
               Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
               startActivity(intent);
           }
       }


            @Override
            public void onAnimationRepeat(Animation animation) {


            }
        });
    }
}