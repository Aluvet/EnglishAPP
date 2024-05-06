package com.example.overapp.Activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.overapp.Interface.PermissionListener;
import com.example.overapp.JSON.JsonBing;
import com.example.overapp.JSON.JsonDailySentence;
import com.example.overapp.Utils.ActivityCollector;
import com.example.overapp.Utils.HttpHelper;
import com.example.overapp.Utils.TimeController;
import com.example.overapp.config.ConfigData;
import com.example.overapp.config.ConstantData;
import com.example.overapp.database.DailyData;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;



public class BaseAcyivity extends AppCompatActivity {
//    接口
    private PermissionListener mListener;
//    申请权限需要请求码
    private  static  final  int PERMISSION_CODE=100;
    private static final String TAG = "BaseActivity";
    @Override
//    初始化
//    活动第一次创建时，调用oncreate方法， Bundle 类型的参数，该参数用于保存活动的状态
    protected  void  onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
//        使用log将目前活动打印，帮助了解那个活动正在运行
        Log.d(TAG,getClass().getSimpleName());

        if (ConfigData.getIsNight()) {
            // 沉浸式状态栏，设置深色
            ImmersionBar.with(this)
                    .statusBarDarkFont(false)
                    .init();
        } else {
            ImmersionBar.with(this)
                    .statusBarDarkFont(true)
                    .init();
        }

//调用add将活动添加到活动列表中，管理活动的生命周期，确保活动可以被正确销毁
        ActivityCollector.addActivity(this);
        // 软键盘弹出，应用内容上推，防止软键盘遮蔽内容
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
//    重写销毁，调用活动管理器的 removeActivity
@Override
//销毁，若活动不用是销毁
//确保当活动被销毁时，能够从活动收集器中移除，从而释放相关资源
protected  void onDestroy(){
        super.onDestroy();
//        将活动移除表中，释放资源，管理活动生命周期
        ActivityCollector.removeActivity(this);


}
    // 权限封装，在welativity中调用
//    检查传入的权限是否都已经被授予
//    公共方法接受pemissions字符串数组（需要请求的权限）和PermissionListener 类型的监听器 listener（用于在权限请求结果返回时接收通知
    public void requestRunPermission(String[] permissions, PermissionListener listener) {
        mListener = listener;
//        存入授权权限 ，创建列表存放
        List<String> permissionLists = new ArrayList<>();
//        遍历遍历权限数组
        for (String permission : permissions) {
//            使用 ContextCompat.checkSelfPermission 方法检查权限是否授权
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
//                若未授权加入带授权的permissionLists 列表中
                permissionLists.add(permission);
            }
        }
//        如果授权列表不为空则 去授权
        if (!permissionLists.isEmpty()) {
//            向用户请求权限，用 ActivityCompat.requestPermissions 方法向用户请求这些权限
            ActivityCompat.requestPermissions(this,
//                    permissionLists 列表转换为一个数组，并传入 PERMISSION_CODE 作为请求代码
                    permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_CODE);
        } else {
            //表示全都授权了，调用监听器的 onGranted 方法，通知所有权限都已经被授予即可。
            mListener.onGranted();
        }
    }
//请求权v限回调
//onRequestPermissionsResult 方法，用户对运行时权限请求做出响应后被调用。这个方法用于处理用户对于权限请求的决策结果（即用户是否授予了权限）
    @Override
    /*三个参数分别
    * requestCode: 请求权限时传递的请求代码。
permissions: 请求的权限列表。
grantResults: 用户对每个权限的响应结果列表。*/
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super调用父类
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        判断是否符合权限代码
        switch (requestCode){
//            100，满足上面定义的code，处理特定权限请求结果
            case PERMISSION_CODE:
                // 存放没授权的权限
                if (grantResults.length > 0) {
//                    创建新的列表 存放被拒绝的权限
                List<String> deniedPermissions = new ArrayList<>();
//                 grantResults 和 permissions 数组，检查每个权限的授权状态
               for (int i=0 ;i<grantResults.length;i++){
//                   获取当前权限的授权结果
                   int grantResult =grantResults[i];
//获取当前权限的名称
                   String permission =permissions[i];
//                   权限为被授予
                   if (grantResult != PackageManager.PERMISSION_GRANTED){
//                       保存被用户拒绝的权限，添加 到deniedPermissions列表中
                       deniedPermissions.add(permission);
                   }
               }
//               如果为空即权限全部接受
               if (deniedPermissions.isEmpty())
               {
//                   被授权，调用listen的ongranted方法
                   mListener.onGranted();
        }
               else{
//                   被拒绝，回调onDenied方法
                   mListener.onDenied(deniedPermissions);
               }}
               break;
            default:
                break;
    }}
//准备每日数据
    public static void prepareDailyData() {
//        使用调用工具类的方法，获取当前时间戳
        long currentDate = TimeController.getCurrentDateStamp();
//        根据当天时间查询对应的图片，存储到，列表中
        List<DailyData> dailyDataList = LitePal.where("dayTime = ?", currentDate + "").find(DailyData.class);
//判断，列表为空
        if (dailyDataList.isEmpty()) {

//            调用下面方法json解析和保存
            analyseJsonAndSave();

        }
//        不为空
         else {
//             判断列表中第一个是否存在图片以及每日一句是否为空
            if (dailyDataList.get(0).getPicVertical() == null ||
                    dailyDataList.get(0).getPicHorizontal() == null ||
                    dailyDataList.get(0).getDailyEn() == null ) {
                //               空，也调用
                analyseJsonAndSave();
            }
        }
    }
/*
 Json 解析与处理服务器返回的必应每日一图的数据
第一行代码
public static String handleBingPicResponse(String response) {
    try {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("images");
        JSONObject bingPic = jsonArray.getJSONObject(0);
        String url = bingPic.getString("url");
        return "http://cn.bing.com" + url;
    } catch (JSONException e) {
        throw new RuntimeException(e);
    }
}
*/
    //欢迎界面的数据，使用GSON进行数据分析和保存，定义公共类
    public static void analyseJsonAndSave() {
//        字节数组，垂直，水平图片数组
        byte[] imgVertical;
        byte[] imgHorizontal;
//        每日一句英文
        String dailyEn;
        String result = "", json, tem;
//        进行每日数据获取解析前，先清空相关数据，防止数据过多
        LitePal.deleteAll(DailyData.class);
//        创建实例进行相关操作
        DailyData dailyData = new DailyData();
        try {
//            try---catch 中 try写入主要操作，
            json = HttpHelper.requestResult(ConstantData.IMG_API);
            Log.d("BaseAcyivity", "数据" + json);
//            创建一个Gson对象，并进行JSON数据的反序列化。
            Gson gson = new Gson();
            JsonBing jsonBing = gson.fromJson(json, JsonBing.class);
            Log.d("BaseAcyivity", "prepareDailyData: " + jsonBing.toString());
//         拼接   构造图片的URL，并将其存储在tem
            tem = ConstantData.IMG_API_BEFORE + jsonBing.getImages().get(0).getUrl();
            Log.d("BaseAcyivity", "URL1:" + tem);
//            构造的URL获取图片数据，并存储
            imgHorizontal = HttpHelper.requestBytes(tem);
//            水平进行替换
//            使用indexof检查tem的url中是否存在"1920x1080"字符串，若存在，直接.replace替换，不存在不替换
            if (tem.indexOf("1920x1080") != -1) {
                result = tem.replace("1920x1080", "1080x1920");
            } else {
                result = tem;
            }
//            在通过http的方法获得竖直图片的数据并存储
            Log.d("BaseAcyivity", "URL2:" + result);
            imgVertical = HttpHelper.requestBytes(result);
//            每日一句
//            1.从ConstantData.DAILY_SENTENCE_API获取每日一句的JSON数据，
//            2.使用Gson反序列化为JsonDailySentence对象。
            json = HttpHelper.requestResult(ConstantData.DAILY_SENTENCE_API);
//            GSON进行数据反序列化，转化为Java数据
            Gson gson1 = new Gson();
//            使用Json解析每日一句的json数据 ，及反序列化 展示英语所以只获得content就行
            JsonDailySentence dailySentence = gson1.fromJson(json, JsonDailySentence.class);
            Log.d("BaseActivity", "数据" + json);

//          获取每日一句的英文
            /*
            * 设置dailyData对象的属性，包括水平图片、垂直图片、每日一句的英文内容和当前日期时间。调用save方法将dailyData对象保存到数据库中*/
            dailyEn = dailySentence.getContent();
            Log.d("BaseActivity", "每日一句：" + dailyEn);
            dailyData.setPicHorizontal(imgHorizontal);
            dailyData.setPicVertical(imgVertical);
            dailyData.setDailyEn(dailyEn);
            dailyData.setDayTime(TimeController.getCurrentDateStamp() + "");
            dailyData.save();
        } catch (Exception e) {
//            catch作用捕获异常
            Log.d("BaseActivity", "prepareDailyData: " + e.toString());
        }
    }
    /*Window.setEnterTransition(Transition transition) 设置进场动画 主界面-(跳转)->A，A 进入过渡
      Window.setExitTransition(Transition transition) 设置出场动画 A-(跳转)->B，A 出场过渡
      Window.setReturnTransition(Transition transition) 设置返回动画 A-(返回)->主界面，A 出场过渡
      Window.setReenterTransition(Transition transition) 设置重新进入动画 B-(返回)->A，A 进入过渡
*/
//    设置相关方法实现窗口弹出
//    爆炸
//    分解效果Explode、滑动进入效果Slide、淡入淡出Fade
//1、Explode：从屏幕的中间进入或退出。
//2、Slide：从屏幕的一边向另一边进入或退出。
//3、Fade：通过改变透明度来出现或消失
//    下面相关方法的调用通常会在Activity的onCreate()、onDestroy()、onResume()等生命周期方法
    public void windowExplode() {
//        持续时间300下同
        getWindow().setEnterTransition(new Explode().setDuration(300));
        getWindow().setExitTransition(new Explode().setDuration(300));
        getWindow().setReenterTransition(new Explode().setDuration(300));
        getWindow().setReturnTransition(new Explode().setDuration(300));
    }
//滑动方向由传入的position参数决定，该参数应该是Slide类中的一个静态常量，如Slide.IN_LEFT、Slide.IN_RIGHT、Slide.IN_UP或Slide.IN_DOWN，分别表示从左、右、上、下四个方向滑动
    public void windowSlide(int position) {
        getWindow().setEnterTransition(new Slide(position).setDuration(300));
        getWindow().setExitTransition(new Slide(position).setDuration(300));
        getWindow().setReenterTransition(new Slide(position).setDuration(300));
        getWindow().setReturnTransition(new Slide(position).setDuration(300));
    }
    public void windowFade() {
        getWindow().setEnterTransition(new Fade().setDuration(500));
        getWindow().setExitTransition(new Fade().setDuration(500));
        getWindow().setReenterTransition(new Fade().setDuration(500));
        getWindow().setReturnTransition(new Fade().setDuration(500));
    }


}
