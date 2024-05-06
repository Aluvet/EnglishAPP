package com.example.overapp.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import  com.example.overapp.Activity.BaseAcyivity;
import com.example.overapp.Adapter.MatchWordAdapter;
import com.example.overapp.ItemData.ItemMatchWord;
import com.example.overapp.R;
import com.example.overapp.database.Word;

import java.util.ArrayList;
import java.util.List;
//单词匹配，需要item 以及adapter
public class MatchActivity extends BaseAcyivity {
    public static List<Word> wordList =new ArrayList<>();
    public static  ArrayList<ItemMatchWord> itemMatchWordArrayList =new ArrayList<>();
    public static ArrayList<ItemMatchWord>   itemMatchWordArrayListAll =new ArrayList<>();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

//        初始化
        initMatch();

//        上瀑布流，两列
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//传入数据，并将adapter 设给recycleview
        MatchWordAdapter matchAdapter = new MatchWordAdapter(itemMatchWordArrayList);
        recyclerView.setAdapter(matchAdapter);
    }



    private void initMatch() {
        recyclerView = findViewById(R.id.recycler_match);
    }
}