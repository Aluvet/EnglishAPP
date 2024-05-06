package com.example.overapp.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.overapp.R;
import com.example.overapp.Utils.TimeController;
import com.example.overapp.Utils.WordsControllor;
import com.example.overapp.config.ConfigData;
import com.example.overapp.database.UserConfig;

import org.litepal.LitePal;

import java.util.List;
//学习单词前的加载界面
public class LoadActivity extends BaseAcyivity {


//    照片
    private ImageView imgLoading;
//进度条水清
    private ProgressBar progressBar;
//进度条
    int progressRate = 0;
    private Runnable runnable;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        windowExplode();
//初始化
        initLoad();
//        加载图片
        Glide.with(this).load(R.drawable.pic_load).into(imgLoading);
        // 准备数据,开心线程
       Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//准备更新用户设置
                updateUserConfigs();
            }
        });
        thread.start();
//异常抛出
        try {
            thread.join();
        } catch (Exception e) {

        }
//        Handler对象和一个Runnable执行定时任务

       mHandler = new Handler();
        runnable = new Runnable() {

            @Override
            public void run() {
                //每隔10微秒循环执行run方法
                mHandler.postDelayed(this, 10);
//                增加进度条得值
                progressBar.setProgress(++progressRate);
//                进度条到达100跳
                if (progressRate == 100) {
                    stopTime();
                    Intent mIntent = new Intent(LoadActivity.this, LearnActivity.class);
                    startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(LoadActivity.this).toBundle());
                }
            }

        };
        mHandler.postDelayed(runnable, 120);


    }
    private void initLoad() {
        imgLoading = findViewById(R.id.img_loading);
        progressBar = findViewById(R.id.progress_wait);
    }
    private void  updateUserConfigs(){
//        LitePal ORM框架从数据库中查询UserConfig对象
        List<UserConfig> userConfigs = LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserConfig.class);
//                传入userConfigs列表中的第一个元素的lastStartTime作为参数
        WordsControllor.generateDailyLearnWords(userConfigs.get(0).getLastStartTime());
        Log.d("LoadActivity", "run: " + userConfigs.get(0).getLastStartTime());
        WordsControllor.generateDailyReviewWords();
//                设置WordsControllor类的静态变量wordReviewNum为needReviewWords列表的大小
        WordsControllor.ToDayWordReviewNum = WordsControllor.needReviewWords.size();
//                创建一个新的UserConfig对象
        UserConfig userConfig = new UserConfig();
//                设置TimeController类的静态变量todayDate为当前日期的戳。
        userConfig.setLastStartTime(TimeController.getNowTimeStamp());
        TimeController.todayDate = TimeController.getCurrentDateStamp();
//                置为空
        LearnActivity.lastWordMean = "";
        LearnActivity.lastWord = "";
//                更新userconfig中的数据
        userConfig.updateAll("userId = ?", ConfigData.getLoggedNum() + "");
    }

    // 停止计时器
    private void stopTime() {
//        removeCallbacks方法的作用是取消之前通过Handler安排的任务
        mHandler.removeCallbacks(runnable);
   }

    @Override
    public void onBackPressed() {
    }
}