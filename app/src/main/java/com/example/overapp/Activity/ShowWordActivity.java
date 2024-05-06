package com.example.overapp.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.Adapter.ShowWordAdapter;
import com.example.overapp.ItemData.ItemMatchWord;
import com.example.overapp.ItemData.ItemShowWord;
import com.example.overapp.R;
import com.example.overapp.Utils.ActivityCollector;
import com.example.overapp.database.Interpretation;
import com.example.overapp.database.Word;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ShowWordActivity extends BaseAcyivity {
    private ShowWordAdapter showWordAdapter;
private List<ItemShowWord> showWordList =new ArrayList<>();
private List<Word> wordList =new ArrayList<>();
    private RecyclerView recyclerView;
    public final String SHOW_Word_TYPE = "showWordType";
    public final int MATCHMode = 1;
    public final int SPEEDMode = 2;
    private final  int Finish=0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case Finish:
                    showWordAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word);
//        初始化
        initShowWord();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        showWordAdapter =new ShowWordAdapter(showWordList);
        recyclerView.setAdapter(showWordAdapter);
//        开心线程进行单词搜索

        new Thread(new Runnable() {
            @Override
            public void run() {
                searchWord();
                bindData();
                Message message = new Message();
                message.what = Finish;
                handler.sendMessage(message);
            }
        }).start();

    }
    private void initShowWord(){
     recyclerView =findViewById(R.id.recycler_show_word);
    }

//    创建方法
    public void searchWord() {
        wordList.clear();
        int nowType = getIntent().getIntExtra(SHOW_Word_TYPE, 0);
        Log.d("ShowWordActivity", "nowType: " + nowType);
        switch (nowType) {
            case MATCHMode:
                for (ItemMatchWord itemMatchWord : MatchActivity.itemMatchWordArrayListAll) {
                    List<Word> words = LitePal.where("wordId = ?", itemMatchWord.getId() + "").select("wordId", "word").find(Word.class);
                    wordList.add(words.get(0));
                }
                break;
            case SPEEDMode:
                wordList = (ArrayList<Word>) SpeedActivity.wordList.clone();
                break;

        }
    }

    private void bindData() {
        showWordList.clear();
//        搜索单词列表
        for (Word word : wordList) {
//            找单词意思
            List<Interpretation> interpretations = LitePal.where("wordId = ?", word.getWordId() + "").find(Interpretation.class);
            StringBuilder stringBuilder = new StringBuilder();
            for (Interpretation interpretation : interpretations) {
                stringBuilder.append(interpretation.getWordType() + ". " + interpretation.getCHSMeaning() + " ");
            }
            if (word.getIsCollected() == 1)
                showWordList.add(new ItemShowWord(word.getWordId(), word.getWord(), stringBuilder.toString(), true));
            else
                showWordList.add(new ItemShowWord(word.getWordId(), word.getWord(), stringBuilder.toString(), false));
        }

        }
//在展示界面返回进行跳转
    @Override
    public void onBackPressed() {
        ActivityCollector.startOtherActivity(ShowWordActivity.this, MainActivity.class);
        finish();}
}