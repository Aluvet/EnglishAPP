package com.example.overapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.overapp.R;
import com.example.overapp.Utils.CircleBack;
import com.example.overapp.config.ConfigData;
import com.example.overapp.database.UserData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;
//日历,显示英语学习打卡
public class CalendarActivity extends BaseAcyivity {
    private LinearLayout Data,Word;
    private ImageView imgTip;
private TextView textDATA,textWord,TextTip;
private MaterialCalendarView materialCalendarView;
private CardView cardTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
//        通初始化
        initCalendar();
//为引入的日历获取对应信息,查询当前id的用户信息
        final List<UserData> userDateList = LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserData.class);
//得到日历属性,年月日
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDate = calendar.get(Calendar.DAY_OF_MONTH);
//当前选中日期
        materialCalendarView.setDateSelected(CalendarDay.from(currentYear, currentMonth, currentDate), true);
//        日历的星期
        materialCalendarView.setWeekDayLabels(new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"});
//        设置选择模式为单选模式（用户一次只能选择一个日期）。
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
//        调用 updateDailyData() 方法来更新当前选中日期的数据
        updateDailyData(CalendarDay.from(currentYear, currentMonth, currentDate));
//调用Add a day decorator
//Params:
//decorator – decorator to add，给日历添加装饰
        materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
//            当前遍历的日期是否与 userDateList 中的任何日期匹配。如果匹配，则返回 true，表示需要对该日期进行装饰
            public boolean shouldDecorate(CalendarDay day) {
                for (UserData userDate : userDateList) {
                    if (day.getDay() == userDate.getDate() && day.getMonth() == (userDate.getMonth() - 1) && day.getYear() == userDate.getYear())
                        return true;
                }
                return false;
            }
//给日历中的数字添加到央视
            @Override
            public void decorate(DayViewFacade view) {
//                调用了view对象的addSpan方法
//                调用方法添加圆环

                view.addSpan(new CircleBack());
            }
        });
//日历选择
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

//                更换数据
                updateDailyData(date);
            }
        });
    }

    public void initCalendar(){
        textDATA=findViewById(R.id.text_cld_date);
        textWord=findViewById(R.id.text_cld_word);
        imgTip=findViewById(R.id.img_calendar_tip);
        TextTip=findViewById(R.id.text_cld_tip);
        materialCalendarView =findViewById(R.id.calendar_view);
        cardTask=findViewById(R.id.card_calendar_task);
        Word=findViewById(R.id.layout_cld_word);
        Data=findViewById(R.id.layout_calendar_date);

    }
//更新日历中显示的每日数据
    private void updateDailyData(CalendarDay date) {
//        查找UserData表中符合特定条件的记录。日期、月份、年份和用户ID
        List<UserData> UserDates = LitePal.where("date = ? and month = ? and year = ? and userId = ?", date.getDay() + "", (date.getMonth() + 1) + "", date.getYear() + "", ConfigData.getLoggedNum() + "").find(UserData.class);
//        条件判断，如果为空则将展示的今日学习记录不显示，反之显示
        if (UserDates.isEmpty()) {
            Data.setVisibility(View.GONE);
            Word.setVisibility(View.GONE);
            Glide.with(CalendarActivity.this).load(R.drawable.icon_no_done).into(imgTip);
            TextTip.setText("该日学习计划未完成");
            TextTip.setTextColor(getColor(R.color.colorLightBlack));
            cardTask.setCardBackgroundColor(getColor(R.color.colorBgWhite));
        } else {
//            可见
//            布局
            Data.setVisibility(View.VISIBLE);
            Word.setVisibility(View.VISIBLE);
            Glide.with(CalendarActivity.this).load(R.drawable.icon_done).into(imgTip);
            TextTip.setText("该日学习计划已完成");
            TextTip.setTextColor(getColor(R.color.colorMainBlue));
//            夜间更换背景
            if (ConfigData.getIsNight())
                cardTask.setCardBackgroundColor(getColor(R.color.colorBgWhite));
            else
                cardTask.setCardBackgroundColor(getColor(R.color.colorLittleBlue));
            textDATA.setText(UserDates.get(0).getYear() + "年" + UserDates.get(0).getMonth() + "月" + UserDates.get(0).getDate() + "日" + "");
            textWord.setText((UserDates.get(0).getWordLearnNumber() + UserDates.get(0).getWordReviewNumber()) + "");


        }
    }
}