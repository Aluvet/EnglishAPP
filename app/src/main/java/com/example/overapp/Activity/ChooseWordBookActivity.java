package com.example.overapp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.Adapter.WordBookAdapter;
import com.example.overapp.ItemData.ItemBook;
import com.example.overapp.R;
import com.example.overapp.Utils.ActivityCollector;
import com.example.overapp.config.ConfigData;
import com.example.overapp.config.ConstantData;
import com.example.overapp.database.UserConfig;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
//选择词书，recycleview对应wordbookadapter
//第一行代码学习
public class ChooseWordBookActivity extends BaseAcyivity {
//在活动中定义内部类，以及RecyclerView的逻辑 ，需要用到item以及adapter
   private RecyclerView recyclerView;

//   创建书单数据
    private List<ItemBook> itemBookList =new ArrayList<>();

    @Override
//    活动调用必写
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word_book);
//        初始化
        recyclerView = findViewById(R.id.word_book_list);
//        设置recyclerView套路
//        实现与Listview相同的效果，配置recycleview，
//        创建LinearLayoutManager对象，RecyclerView的布局管理器，用于决定如何排列RecyclerView中的项
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
//        设置RecyclerView的布局管理器为之前创建的LinearLayoutManager
        recyclerView.setLayoutManager(linearLayoutManager);

        //        进行书单数据初始化
        initBooks();
//适配器设置
//        创建WordBookAdapter对象，并传入itemBookList的列表作为数据源
        WordBookAdapter wordBookAdapter =new WordBookAdapter(itemBookList);
//      setAdapter  将适配器设置到RecyclerView，令RecyclerView知道如何显示itemBookList中的数据
        recyclerView.setAdapter(wordBookAdapter);

    }
    //    初始化单词书的数据，来源语有道
    private void initBooks(){
//            创建item     添加数据依据对应的数据添加数据，单词书名称，单词数，以及单词书的照片，设置单词书的来源
itemBookList.add(new ItemBook(ConstantData.KaoYan_MUST,ConstantData.WordBookName(ConstantData.KaoYan_MUST),ConstantData.BookWordNum(ConstantData.KaoYan_MUST),ConstantData.BookPic(ConstantData.KaoYan_MUST),"来源：有道词典"));
itemBookList.add(new ItemBook(ConstantData.KaoYan_ALL,ConstantData.WordBookName(ConstantData.KaoYan_ALL),ConstantData.BookWordNum(ConstantData.KaoYan_ALL),ConstantData.BookPic(ConstantData.KaoYan_ALL),"来源：有道词典"));
    }

//返回事件
    @Override
    public void onBackPressed() {
//        查询数据库中与当前登录用户ID相关联的UserConfig记录，如果id不为-1即已经选择书目则执行返回操作
        if (LitePal.where("userId=?",ConfigData.getLoggedNum()+"").find(UserConfig.class).get(0).getCurrentBookId()!=-1) {
            super.onBackPressed();
        }
        else {
//        构建框
        AlertDialog.Builder builder =new AlertDialog.Builder(ChooseWordBookActivity.this);
        builder.setTitle("提示")
                .setMessage("确定退出吗")
//                创建按钮并注册点击事件
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        调用activity中的activtyFINISH全部销毁
                        ActivityCollector.finishALL();
                    }
                })
                .setNegativeButton("取消",null)
        .show();
    }}
}