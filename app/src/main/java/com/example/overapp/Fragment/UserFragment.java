package com.example.overapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;


import com.example.overapp.Activity.AppAboutActivity;
import com.example.overapp.Activity.CalendarActivity;
import com.example.overapp.Activity.LearnAlarmActivity;
import com.example.overapp.Activity.MainActivity;
import com.example.overapp.Activity.PlanActivity;
import com.example.overapp.Activity.WordListActivity;
import com.example.overapp.R;
import com.example.overapp.Utils.MyApplication;
import com.example.overapp.Utils.TimeController;

import com.example.overapp.config.ConfigData;
import com.example.overapp.database.User;
import com.example.overapp.database.UserData;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;
//用户界面，fragment 跳转
public class UserFragment extends Fragment implements View.OnClickListener {
    private LinearLayout calendar,wordList,Plan,money;
    private RelativeLayout alarm, about;
    private TextView UserName,textDays,textWordNum,textMoney;
    private Switch toNightSwitch;
////    fragment必须创建和返回Fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }
//    //视图创建后调用
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        初始化
          init();

        Log.d("UserFragment", "onActivityCreated: ");
//根据判断更新组件状态
//夜间

        boolean isNight = ConfigData.getIsNight();
        if (isNight)
            toNightSwitch.setChecked(true);
        else
            toNightSwitch.setChecked(false);
//        给用户设置信息并更
        List<User> userList = LitePal.where("userId=?",ConfigData.getLoggedNum()+"").find(User.class);
        UserName.setText(userList.get(0).getUserName());
//        给开关设置点击事件
        toNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    ConfigData.setIsNight(true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    ConfigData.setIsNight(false);
                }
                MainActivity.FragmentRefresh = false;
                MainActivity.lastFragment = 2;
                WordFragment.prepareData = 0;
                getActivity().recreate();
            }
        });


//money 点击
        money.setOnClickListener(view -> {
//            日期数组,存储日期  得到当前日期戳分割(不带时间，只有日期)
            final String[] date = TimeController.getStringDate(TimeController.getCurrentDateStamp()).split("-");
//            从textmoney文本框中获取金额并转化为整数
            final int currentMoney = Integer.parseInt(textMoney.getText().toString().trim());
//            当前钱大于100,弹出对话框
            if (currentMoney >= 100) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示")
                        .setMessage("确定要花费100铜板进行日历补卡吗？")
//                        积极时间
                        .setPositiveButton("确定", (dialog, which) -> {
//创建日期对话框,供用户选择日历中的日期
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                    (view1, year, month, dayOfMonth) -> {
//                                      先查询数据库中是否存在这个日期的用户数据,若没有则新建,年月日
                                        List<UserData> myDateList = LitePal.where("year = ? and month = ? and date = ?", year + "", (month + 1) + "", dayOfMonth + "").find(UserData.class);
                                        if (myDateList.isEmpty()) {
//                                            为空,用户选择当天,弹出不让用户打卡
                                            if (year == Integer.parseInt(date[0]) && month == Integer.parseInt(date[1]) - 1 && dayOfMonth == Integer.parseInt(date[2])) {
                                                Toast.makeText(MyApplication.getContext(), "不可对今日进行补打卡", Toast.LENGTH_SHORT).show();
                                            } else {
//                                                不是今天,创建新的对象,设置相关信息
//                                                设置数据并保存
                                                UserData myDate = new UserData();
                                                myDate.setDate(dayOfMonth);
                                                myDate.setUserId(ConfigData.getLoggedNum());
                                                myDate.setYear(year);
                                                myDate.setMonth(month + 1);
                                                myDate.save();
//                                                新建用户对象
                                                User user = new User();
//                                                判断当前的金钱是否满足
                                                if (currentMoney - 100 > 0)
                                                    user.setUserMoney(currentMoney - 100);
                                                else
//                                                    刚好100,回复0,litepal
                                                    user.setToDefault("userMoney");
//                                                完成上述操作更新数据库
                                                user.updateAll("userId = ?", ConfigData.getLoggedNum() + "");
                                                updateUserData();
                                                Toast.makeText(MyApplication.getContext(), "补卡成功！", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
//                                            已有,弹出不能打卡
                                            Toast.makeText(MyApplication.getContext(), "已在该日进行打卡，不可重复", Toast.LENGTH_SHORT).show();
                                        }
                                    },
//                                   年月日,将字符串转化为整数
                                    Integer.parseInt(date[0]),
                                    Integer.parseInt(date[1]) - 1,
                                    Integer.parseInt(date[2]));
//                            获取DatePickerDialog中的DatePicker对象
                            DatePicker datePicker = datePickerDialog.getDatePicker();
//                            设置最大日期,用户不能选取未来的日期
                            datePicker.setMaxDate(new Date().getTime());
                            datePickerDialog.show();
                        })
                        .setNegativeButton("取消", null)
                        .show();
            } else {
//                金币小于100,直接弹出不够
                Toast.makeText(MyApplication.getContext(), "抱歉，你的铜板个数还不足要求。必须满足100个铜板才可以补打卡哦", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private  void  init(){
        calendar=getActivity().findViewById(R.id.User_calendar);
        wordList=getActivity().findViewById(R.id.User_word_list);
        Plan=getActivity().findViewById(R.id.User_plan);
        money=getActivity().findViewById(R.id.layout_User_money);
        alarm=getActivity().findViewById(R.id.Learn_alarm);
        about =getActivity().findViewById(R.id.APP_ABOUT);
        toNightSwitch =getActivity().findViewById(R.id.switch_night);

        textDays =getActivity().findViewById(R.id.text_User_days);
        textMoney=getActivity().findViewById(R.id.text_User_money);
        textWordNum=getActivity().findViewById(R.id.text_User_words);
        UserName=getActivity().findViewById(R.id.User_Name);

//     点击事件
        calendar.setOnClickListener(this);
        wordList.setOnClickListener(this);
        Plan.setOnClickListener(this);
        alarm.setOnClickListener(this);
        about.setOnClickListener(this);
    }

//点击事件开启新活动
    @Override
    public void onClick(View view) {
//        意图根据用户点击进行活动跳转
        Intent intent =new Intent();
        switch (view.getId()){
            case R.id.User_calendar:
                intent.setClass(getActivity(), CalendarActivity.class);
                break;
            case R.id.User_plan:
                intent.setClass(getActivity(), PlanActivity.class);
                break;
            case R.id.User_word_list:
                intent.setClass(getActivity(), WordListActivity.class);
                break;
            case R.id.Learn_alarm:
                intent.setClass(getActivity(), LearnAlarmActivity.class);
                break;
            case R.id.APP_ABOUT:
                intent.setClass(getActivity(), AppAboutActivity.class);
                break;


        }
        startActivity(intent);
    }
    @Override
    public void onStart() {
        super.onStart();
        updateUserData();
    }

    private void updateUserData() {
        // 设置天数
        List<UserData> myDateList = LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserData.class);
        textDays.setText(myDateList.size() + "");
        // 设置单词数与金币数
        List<User> userList = LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(User.class);
        textWordNum.setText(userList.get(0).getUserWordNumber() + "");
        textMoney.setText(userList.get(0).getUserMoney() + "");
    }
}
