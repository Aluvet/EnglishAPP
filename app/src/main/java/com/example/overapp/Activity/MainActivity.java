package com.example.overapp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.overapp.Fragment.ReviewFragment;
import com.example.overapp.Fragment.UserFragment;
import com.example.overapp.Fragment.WordFragment;
import com.example.overapp.R;
import com.example.overapp.Utils.ActivityCollector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseAcyivity {
    private static final String TAG = "MainActivity";
//    三个fg碎片
private Fragment  FgWords,FgReview,FgUser;
private  LinearLayout linearLayout;
private Fragment [] fragments;
    //用于记录上个选择的Fragment
    public static int lastFragment;
    public static boolean FragmentRefresh =true;
//底部视窗
    private BottomNavigationView bottomNavigation;
    @Override
//    活动创建必须
    protected void onCreate(Bundle savedInstanceState) {
//        调用父类，保存实例
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
//定义调用
        initMain();
//判断是否需要刷新
        if (FragmentRefresh) {
//创建平移动画
            TranslateAnimation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f
            );
            animation.setDuration(2000);
        }
//        对底部导航栏的控件初始化获得对应fragment
//        先创建单词界面

         initFragment();
    }
    private void initMain(){
        bottomNavigation =findViewById(R.id.bottom_nav);
        linearLayout =findViewById(R.id.linear_frag_container);
    }
    private  void  initFragment(){
//        新建Frag的三个对象，表示单词界面 ，复习界面 ，以及用户界面
     FgWords =new WordFragment();
     FgReview =new ReviewFragment();
     FgUser = new UserFragment();
//     fragment列表表示多少选项使用Switch进行选择
//        创建包含单词界面 ，复习界面 ，以及用户界面的三个Fragment对象的数组，用于后续的Fragment管理和切换
        fragments =new Fragment[]{FgWords,FgReview,FgUser};
//        switch对对应的fragment进行选择
        switch (lastFragment){
//            fragment提交事务
//            words，为零，将FgWords添加到linear_frag_container底部导航栏中，并展示
//            fragment在动态时添加
//            通过getSupportFragmentManager()方法获取一个fragmentManager，开始事务
//            替换fragment，需要用到replace方法
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.linear_frag_container,FgWords).show(FgWords).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.linear_frag_container,FgReview).show(FgReview).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.linear_frag_container,FgUser).show(FgUser).commit();
                break;}
//                对底部导航栏添加点击事件
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_word:
//                        定义切换方法实现不同导航栏界面的切换
//                        判断是否是当前页面，不是更新界面以及当前的值保存fragment的数据
                        if (lastFragment != 0) {
//                            界面切换
                            switchFragment(lastFragment, 0);
                            lastFragment = 0;
                        }
                       return true;
                    case R.id.menu_review:
                        if (lastFragment != 1) {
                            switchFragment(lastFragment, 1);
                            lastFragment = 1;
                        }
                      return true;
                    case R.id.menu_me:
                        if (lastFragment != 2) {
                            switchFragment(lastFragment, 2);
                            lastFragment = 2;
                        }
                       return true;
                }
return true;
            }
        });

}
//定义用于界面切换，传入当前的fragment值以及第几个索引进行更换
    private  void switchFragment(int lastFragment,int index){
//        获取fragmentManager以及开启新的事物，进行下属操作 ，FragmentTransaction是一个用于执行Fragment事务（
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
//        hide 和 show 是配对的，当要显示隐藏的 Fragment A 时，就 show(A)；而对于其他 Fragment，则先 hide 起来，等之后要显示时再 show
//        将界面隐藏  hide ，调用hide将目前的fragment隐藏
        transaction.hide(fragments[lastFragment]);
//        判断切换的fragment是否已被添加到容器中，若没添加，添加到容器中
        if (fragments[index].isAdded()==false){
            transaction.add(R.id.linear_frag_container,fragments[index]);
        }
//        将切换的fragment调用show显示出来，并采用commitAllowingStateLoss()提交
//        此时使用commitAllowingStateLoss()允许在Activity的保存状态之后提交事务
//       将隐藏的 Fragment 显示出来  show
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

//    用户点击设备返回键时使用
    @Override
    public void onBackPressed() {
//        弹出提示框
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        构建标题
        builder.setTitle("提示")
//                消息文本
                .setMessage("今天不再背单词了吗？")
//                积极按钮，做出相应点击事件
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        点击退出重置fragment
                        FragmentRefresh = true;
//                        调用activityCollect的finishall将活动全部去掉
                        ActivityCollector.finishALL();
                    }
                })
//                消极不做任何活动
                .setNegativeButton("不退出", null)
                .show();
    }

//    onDestroy() 是 Activity 生命周期中的一个方法，当 Activity 被销毁时，该方法会被调用
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }}