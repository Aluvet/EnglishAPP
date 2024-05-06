package com.example.overapp.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.Adapter.SearchAdapter;
import com.example.overapp.ItemData.ItemSearch;
import com.example.overapp.R;
import com.example.overapp.database.Interpretation;
import com.example.overapp.database.Word;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
//搜索界面  需配置adapter 以及 item 和 itemData
public class SearchActivity extends BaseAcyivity{
    private EditText editSearch;
    private TextView textCancel;
    private RecyclerView recyclerView;
    private RelativeLayout recyclerNothing;
private List<ItemSearch> itemSearches =new ArrayList<>();
    private final int FINISH = 1;
private SearchAdapter searchAdapter;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FINISH:

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        初始化基本控件
        initSearch();
//        实现与Listview相同的效果  ，创建LinearLayoutManager决定RecyclerView中项目的布局方式
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
//        将LinearLayoutManager设置给recyclerView
        recyclerView.setLayoutManager(linearLayoutManager);
//  创建WordBookAdapter对象，并传入itemBookList的列表作为数据源
//        将适配器设置到RecyclerView，令RecyclerView知道如何显示itemBookList中的数据
        searchAdapter =new SearchAdapter(itemSearches);
        recyclerView.setAdapter(searchAdapter);
//        添加事件，监听文本变化
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
//            这个方法就是在EditText内容已经改变之后调用
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
//在文本变化完成后
            @Override
            public void afterTextChanged(Editable editable) {
//                判断如果输入框为空
                if (TextUtils.isEmpty(editable.toString().trim())){
//                    则将nothing显示,另一个查询单词布局不可见
                    recyclerNothing.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
//                不为空遇上相反
                else {
//                    调用方法设置文本框的数据
                    setData(editable.toString().trim());
                    Log.d("SearchActivity", editable.toString().trim());
                }

            }
        });
//        取消直接返回
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });    }

    public void initSearch(){
        recyclerView=findViewById(R.id.recycler_search);
        recyclerNothing=findViewById(R.id.layout_search_nothing);
        editSearch =findViewById(R.id.edit_search);
        textCancel =findViewById(R.id.text_search_cancel);
    }
//    设置数据
    public void setData(String s){
//        必须先确保新搜索结果与旧的结果不会重复，将列表清空
        itemSearches.clear();
//        模糊查找，查询与输入字母有关的单词
        List<Word> words = LitePal.where("word like ?", s +"%").select("wordId","word","usPhone").limit(10).find(Word.class);
//        非空对列表循环
        if (!words.isEmpty()){
        for (Word word:words){
//            查询循环得到的数据id相仿的释义及类型
            List<Interpretation> interpretations =LitePal.where("wordId = ?",word.getWordId()+"").select("wordType","CHSMeaning").find(Interpretation.class);
//           利用stringbuilder进行拼接
            StringBuilder stringBuilder =new StringBuilder();
//            同上循环，进行拼接追加
            for (Interpretation interpretation: interpretations){
//                将类型与中文拼接
                stringBuilder.append(interpretation.getWordType() +"."+interpretation.getCHSMeaning()+"");

            }
//           为每个单词创建一个新的ItemSearch对象，并将单词的ID、单词本身、美式发音和解释添加到itemSearches列表中
            itemSearches.add(new ItemSearch(word.getWordId(),word.getWord(),word.getUsPhone(),stringBuilder.toString()));

        }
//        单词显示
            recyclerView.setVisibility(View.VISIBLE);
        recyclerNothing.setVisibility(View.GONE);
        }else {
//            不显示
            recyclerNothing.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
//        通用使用notify通知适配器进行更新
searchAdapter.notifyDataSetChanged();
    }
//返回处理
    @Override
    public void onBackPressed() {
//        用于执行过渡动画
        supportFinishAfterTransition();
    }
}
