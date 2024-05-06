package com.example.overapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.overapp.R;
import com.example.overapp.Utils.ActivityCollector;
import com.example.overapp.Utils.TimeController;
import com.example.overapp.Utils.WordsControllor;
import com.example.overapp.config.ConfigData;
import com.example.overapp.database.User;
import com.example.overapp.database.UserConfig;
import com.example.overapp.database.UserData;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;

//学完后的负责展示打卡结束画面
public class FinishActivity extends BaseAcyivity {
    //    更新用户信息
    private List<UserConfig> userConfigs;
    private TextView LearnOverWordNum, learnedDay;

    private Button Back;

    private int wordNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
//      初始化
        initFinish();
        windowExplode();

        userConfigs = LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserConfig.class);
        wordNum = userConfigs.get(0).getWordNeedReciteNum() + WordsControllor.ToDayWordReviewNum;
        LearnOverWordNum.setText(wordNum + "");
        List<UserData> myDateList = LitePal.findAll(UserData.class);
        learnedDay.setText((myDateList.size() + 1) + "");
    Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initFinish() {
//        学习单词数 天数，以及按钮
        LearnOverWordNum=findViewById(R.id.text_finish_word_num);
        learnedDay=findViewById(R.id.text_finish_days);
//        功能
        Back=findViewById(R.id.btn_finish_back);

    }

    @Override
    public void onBackPressed() {
        saveData();
        ActivityCollector.startOtherActivity(FinishActivity.this, MainActivity.class);
    }

    private void saveData() {
        Calendar calendar = Calendar.getInstance();
        List<UserData> myDates = LitePal.where("year = ? and month = ? and date = ? and userId = ?",
                calendar.get(Calendar.YEAR) + "",
                (calendar.get(Calendar.MONTH) + 1) + "",
                calendar.get(Calendar.DATE) + "",
                ConfigData.getLoggedNum() + "").find(UserData.class);
        if (myDates.isEmpty()) {
            dataControl();
        } else {
            int result = LitePal.deleteAll("year = ? and month = ? and date = ? and userId = ?",
                    calendar.get(Calendar.YEAR) + "",
                    (calendar.get(Calendar.MONTH) + 1) + "",
                    calendar.get(Calendar.DATE) + "",
                    ConfigData.getLoggedNum() + "");
            if (result != 0) {
                dataControl();
            }
        }
    }

    private void dataControl() {
        String[] s = TimeController.getStringDate(TimeController.todayDate).split("-");
        UserData myDate = new UserData();
        myDate.setWordLearnNumber(userConfigs.get(0).getWordNeedReciteNum());
        myDate.setWordReviewNumber(WordsControllor.ToDayWordReviewNum);
        myDate.setYear(Integer.valueOf(s[0]));
        myDate.setMonth(Integer.valueOf(s[1]));
        myDate.setDate(Integer.valueOf(s[2]));
        myDate.setUserId(ConfigData.getLoggedNum());
        myDate.save();
        // 增加10金币
        List<User> users = LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(User.class);
        User user = new User();
        user.setUserMoney(users.get(0).getUserMoney() + 10);
        user.setUserWordNumber(users.get(0).getUserWordNumber() + userConfigs.get(0).getWordNeedReciteNum());
        user.updateAll("userId = ?", ConfigData.getLoggedNum() + "");
    }
}