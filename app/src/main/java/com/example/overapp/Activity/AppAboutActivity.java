package com.example.overapp.Activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.overapp.R;
import com.example.overapp.Utils.MyApplication;
import com.example.overapp.Utils.NumberControl;
import com.example.overapp.config.ConstantData;
//关于APP
public class AppAboutActivity extends BaseAcyivity{
private TextView AppVersion, AppName,AppContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_about);
        initAppAbout();

//        显示
        AppVersion.setText("目前App的版本为:"+getAppVersionName(AppAboutActivity.this)+"（" + getAppVersionCode(AppAboutActivity.this) + "）");
        AppName.setText(getAppName(MyApplication.getContext()));
//        app内容随机出现短语并展示
        AppContent.setText(ConstantData.phrases[NumberControl.getRandomNumber(0,ConstantData.phrases.length-1)]);
    }
    public  void  initAppAbout(){
        AppVersion =findViewById(R.id.text_Apabout_version);
        AppName=findViewById(R.id.text_Apabout_name);
        AppContent=findViewById(R.id.text_Apabout_content);
    }
//    获取应用版本码
    public static String getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
//            通过传入的 context 获取 PackageManager 实例
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
//            提取versioncode
            versioncode = pi.versionCode;
        } catch (Exception e) {
            //Log.e("VersionInfo", "Exception", e);
        }
//        字符串
        return versioncode + "";
    }
//版本名称
    public static String getAppVersionName(Context context) {
//        存储应用版本名称
        String versionName = null;
        try {
//            尝试获取版本信息,
            PackageManager pm = context.getPackageManager();
//            调用 `PackageManager` 的 `getPackageInfo` 方法来获取指定应用包的信息,
//            context.getPackageName()包名,设为0,不需任何权限
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
//            提取version,应用版本名
            versionName = pi.versionName;
        } catch (Exception e) {
            //Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
//    APP名称,通过 context 获取 PackageManager 实例
//    调用 context.getApplicationInfo() 来获取当前应用的应用信息，
//    并传递给 packageManager.getApplicationLabel() 来获取应用的标签
//    获取到的标签转换为 String 类型
    public static String getAppName(Context context) {
//        全局为空,直接返回防止空指针
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(context.getApplicationInfo()));
        } catch (Throwable e) {
        }
        return null;
    }


}