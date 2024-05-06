package com.example.overapp.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//时间控制器
public class TimeController {

    public static long todayDate;
//静态常量字符串FORMAT_NOTIME，它表示日期的格式，年-月-日。
    public static final String FORMAT_NOTIME = "yyyy-MM-dd";
//格式化,创建一个SimpleDateFormat对象simpleDateFormat，将日期字符串转换为Date对象
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_NOTIME);
    /*----日期类----*/
//    SimpleDateFormat对象
//  没用localdata   等  ,而是用calendar
    // 得到当前日期戳(不带时间，只有日期)
    public static long getCurrentDateStamp() {
//        获得时间初始化
//       getInstance() 获取默认时区和区域设置
        Calendar cal = Calendar.getInstance();
//        创建完Calendar后可以获取对应的值获
        int currentYear = cal.get(Calendar.YEAR);
     int currentMonth = cal.get(Calendar.MONTH) + 1;  //月份从零开始要加1Month必须加1
        int currentDate = cal.get(Calendar.DATE);
        long time = 0;
//        捕获异常
        try {
//            核心代码  使用simpleDateFormat将年、月、日拼接成的字符串转换为Date对象，并调用getTime()方法获取其时间戳
            time = simpleDateFormat.parse(currentYear + "-" + currentMonth + "-" + currentDate).getTime();
        } catch (ParseException e) {
//            捕获异常 ，输出异常信息
            e.printStackTrace();
        }
        Log.d("TimeController", "getCurrentDateStamp: " + time);
        return time;
    }
    // 根据指定日期戳解析成日期形式（yyyy-MM-dd）
    public static String getStringDate(long timeStamp) {
        return simpleDateFormat.format(new Date(Long.parseLong(String.valueOf(timeStamp))));
    }

//    设置n天后的日期，即具体到某天
    public static String getDayAgoOrAfterString(int num) {
//        调用calendar.getinstance()返回的时间是以当前系统所在区域为基准的标准时区时间。
        Calendar calendar =Calendar.getInstance();
//        格式化
        SimpleDateFormat simpleDateFormat1 =new SimpleDateFormat("yyyy年MM月dd日");

//        将接收到的num加到当前日期算出num后的日期
//       第一个参数固定使用 Calendar.DATE 是一个静态常量，用于指示要添加或减去的时间单位是日期（天）
        calendar.add(Calendar.DATE,num);
        // 使用SimpleDateFormat将Calendar对象转换为字符串，并存储在局部变量中
        String formattedDate = simpleDateFormat1.format(calendar.getTime());

        return  formattedDate;

    }
    // 得到当前时间戳（有日期与时间）
    public static long getNowTimeStamp() {
        return System.currentTimeMillis();
    }
    // 根据指定日期戳解析成日期形式（yyyy-MM-dd）
    public static String getStringDateDetail(long timeStamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(Long.parseLong(String.valueOf(timeStamp)));
        String formattedDate = simpleDateFormat.format(date);
        return formattedDate;
    }
    // 返回过去第几天的日期（有年份）
    public static String getPastDateWithYear(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }
    // 判断两个时间戳是否为同一天
    public static boolean isTheSameDay(long time1, long time2) {
//        判断time1和time2是否相同，分别调用 getStringDate 方法将 time1 和 time2 转换为字符串，equel判断字符串
        return getStringDate(time1).equals(getStringDate(time2));
    }
    // 返回两个日期之间相隔多少天，接受两个时间戳
    public static int daysInternal(long time1, long time2) throws ParseException {
//        将time转换成Date对象并赋值给变量
        Date date1 = simpleDateFormat.parse(getStringDate(time1));
        Date date2 = simpleDateFormat.parse(getStringDate(time2));
//        获得两个时间戳，相减的时间差，将时间差*(1000 * 3600 * 24)转化为天
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }
}
