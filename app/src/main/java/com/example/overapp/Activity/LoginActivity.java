package com.example.overapp.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.overapp.R;
import com.example.overapp.Utils.ActivityCollector;
import com.example.overapp.config.ConfigData;
import com.example.overapp.database.User;
import com.example.overapp.database.UserConfig;
import com.lihang.ShadowLayout;

import org.litepal.LitePal;

import java.util.List;

public class LoginActivity extends BaseAcyivity {
    private ShadowLayout cardLogin;
    private LinearLayout linearLayout;
    private  final int Success=1;
    private final  int Failed =2;
//对下方传递的信息判断
    @SuppressLint("HandlerLeak")
    private  Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case Failed:
                    Toast.makeText(LoginActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                    break;
                case Success:
//                    成功则使用行页面跳转 ，进入选择词书界面
                    ActivityCollector.startOtherActivity(LoginActivity.this,ChooseWordBookActivity.class);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        初始化
        cardLogin = findViewById(R.id.card_login);
        linearLayout = findViewById(R.id.linear_login);
        // 渐变动画  ，从透明到不透明持续2秒
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(2000);

//        点击事件
        cardLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                当奠基石初始化，用户信息
                int id = 00001;
                String name = "user";
//                查询User表找出id为“00001”的用户信息,并存到users中
                List<User> users = LitePal.where("userId=?", id + "").find(User.class);
//                如果用户列表为空
                if (users.isEmpty()) {
//                    创建新用户
                    User user = new User();
                    user.setUserName(name);
                    user.setUserId(id);
//                    设置user的金币等信息
                    user.setUserMoney(0);
                    user.setUserWordNumber(0);
//                    将信息保存到表中
                    user.save();
                }
                // 查询用户配置表中，是否存在该用户，若没有，则新建数据
                List<UserConfig> userConfigs = LitePal.where("userId=?", id + "").find(UserConfig.class);
                if (userConfigs.isEmpty()) {
//                    创建userconfig的新对象
                    UserConfig userConfig = new UserConfig();
//                    设置id
                    userConfig.setUserId(id);
//                    设置当前新用户使用的书为-1 ，当前用户并未学习词本
                    userConfig.setCurrentBookId(-1);
//                    将信息保存
                    userConfig.save();

                }
//                设置用户已登陆
                ConfigData.setIsLogged(true);
//                传入用户的id到parentshare中键值对保存
                ConfigData.setLoggedNum(id);
//                向handle传递信息
                Message message = new Message();
                message.what = Success;
                handler.sendMessage(message);
            }

        });

    }
//    点击返回键   弹出提示框并退出
    @Override
    public void onBackPressed() {
//        通mainactivity
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("提示")
                .setMessage("确定要退出吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCollector.finishALL();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

}
