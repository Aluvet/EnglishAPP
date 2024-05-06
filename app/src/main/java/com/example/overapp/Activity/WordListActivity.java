package com.example.overapp.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.Adapter.WordListContentAdapter;
import com.example.overapp.ItemData.ItemWordListContent;
import com.example.overapp.R;
import com.example.overapp.config.ConfigData;
import com.example.overapp.config.ConstantData;
import com.example.overapp.database.Interpretation;
import com.example.overapp.database.UserConfig;
import com.example.overapp.database.Word;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
//单词列表,展示大全
public class WordListActivity extends BaseAcyivity {
private List<ItemWordListContent> wordListContents =new ArrayList<>();
private  List<Word> words =new ArrayList<>();
private WordListContentAdapter wordListContentAdapter;
private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private int nowMode;
//    在单词列表中全部设置单词列表选项供用户选择
    private final int AllWordsMode=0;
    private  final int CollectWordsMode=1;
    private  final String[] top_content={"全部单词","收藏单词"};
    private TextView textTopInsideWord, text_topcontent;
    private int nowItemNum = 0;
    private int addItemNum = 100;
    private int nowAllNumber;
    private LinearLayout TopContent, Tip;
    private ProgressDialog progressDialog;
    private final int Finish = 1;
    public  static   boolean needUpdate=false;


//接受下面的信息
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message message) {
            switch (message.what){
                case Finish:
//                    更新适配器并隐藏进度条
                    wordListContentAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
//                    判断如果当前单词和数据为空，将相关布局设为不可建
                    if (words.isEmpty()){
                        recyclerView.setVisibility(View.GONE);
                        Tip.setVisibility(View.VISIBLE);
                    }else {
                        recyclerView.setVisibility(View.VISIBLE);
                        Tip.setVisibility(View.GONE);
                    }
//                    在对当前的模块
                  switch (nowMode){
//                        展示全部单词
                        case AllWordsMode:
                            text_topcontent.setText(top_content[AllWordsMode]);
                            textTopInsideWord.setText("当前单词数"+nowAllNumber);
                            break;
                        case CollectWordsMode:
//                            展示收藏单词
                            text_topcontent.setText(top_content[CollectWordsMode]);
                            textTopInsideWord.setText("当前单词数"+words.size());
break;
                    }
                    progressDialog.dismiss();
                    break;
                    }
            }
        };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        initWordList();
//当前词书的全部单词数
        nowAllNumber = ConstantData.BookWordNum(LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserConfig.class).get(0).getCurrentBookId());
//设置 RecyclerView 的布局管理器 ,线
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
//设置 RecyclerView 的适配器
        wordListContentAdapter = new WordListContentAdapter(wordListContents);
        recyclerView.setAdapter(wordListContentAdapter);

        showWordPogress();
//默认为当前模式为所有单词
        nowMode = AllWordsMode;

//开心线程等,完成耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateNowModelDate(nowMode);
            }
        }).start();
//对顶部的content设值switch选择，加载数据
        TopContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                弹出对话框
                final AlertDialog.Builder builder = new AlertDialog.Builder(WordListActivity.this);
//                默认全部
                builder.setSingleChoiceItems(top_content, nowMode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        // 延迟500毫秒取消对话框
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                // 更换模式,将
                                if (nowMode != which) {
                                    switch (which) {
                                        case 0:
//                                            在新的线程中清空 wordListContents 列表，
//                                            重置 nowItemNum 和 addItemNum 变量，然后调用 updateNowModelDate(AllWordsMode) 方法更新,下同
                                            showWordPogress();
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
//                                                    将当前列表中的东子全清空
//                                                    并初始化
                                                    wordListContents.clear();
                                                    nowItemNum = 0;
                                                    addItemNum = 100;
                                                   updateNowModelDate(AllWordsMode);
                                                    nowMode = AllWordsMode;
                                                }
                                            }).start();
                                            break;
                                        case 1:
//                                            设置为收藏
                                            showWordPogress();
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    updateNowModelDate(CollectWordsMode);
                                                    nowMode = CollectWordsMode;
                                                }
                                            }).start();
                                            break;

                                    }
                                }
                            }
                        }, 200);
                    }
                }).show();
            }
        });
//滚动事件见同期,recycle滚动监听器
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                当前模块为全部单词是
                if (nowMode == AllWordsMode) {
//                    获取布局管理器
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                    第一个位置
                    int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (firstCompletelyVisibleItemPosition == 0)
//                        顶部
                        Log.d("WordListActivity", "top");
//                    最后一个位置
                    int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
//                        当前可见最后一个展示进度条,加载剩余单词
                        progressBar.setVisibility(View.VISIBLE);
//                        加在单词要开新线程
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                updateNowModelDate(nowMode);
                            }
                        }).start();
                    }
                }
            }
        });
    }

    public void initWordList(){
        progressBar =findViewById(R.id.progress_wordlist_load);
        TopContent =findViewById(R.id.layout_wordlist_topcontent);
        Tip=findViewById(R.id.layout_wordlist_tip);
        text_topcontent=findViewById(R.id.text_wordlist_topcontent);
        textTopInsideWord=findViewById(R.id.text_wordlist_insideWord);
        recyclerView=findViewById(R.id.recycler_wordlist_content);
    }
//    活动即将可见时,调用
    @Override
    protected void onStart() {
        super.onStart();
//        更换,即需要更新数据,且当前不是全部单词
        if (needUpdate && nowMode != AllWordsMode) {
//            显示进度条
            showWordPogress();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    updateNowModelDate(nowMode);
                }
            }).start();
//            更新能完成后,置为否
            needUpdate = false;
        }
    }
    public void showWordPogress(){
        progressDialog=new ProgressDialog(WordListActivity.this);
        progressDialog.setTitle("请等待");
        progressDialog.setMessage("正在加载当前模块的相关数据");
//        不能用返回键关闭dialog
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
//创建更新数据的相关方法，根据当前的模块更新相应的数据
    public void  updateNowModelDate(int Mode){
//        清空当前数据
        words.clear();
//        传入响应数据，查找对应单词，并更新数据
        switch (Mode){
            case AllWordsMode:
//litepal 查询全部单词,且使用limit ,和 offset进行分页查询
                words = LitePal.select("wordId", "word").limit(addItemNum).offset(nowItemNum + addItemNum).find(Word.class);
                nowItemNum=addItemNum+nowItemNum;
                break;
            case CollectWordsMode:
                wordListContents.clear();
//                查询所有标记为已收藏的单词（通过 isCollected = 1 条件
                words=LitePal.where("isCollected = ?", 1 + "").select("wordId", "word").find(Word.class);
                break;
        }
//        循环添加对应数据
        for (Word word:words){
//            设置可查看
            wordListContents.add(new ItemWordListContent(word.getWordId(),word.getWord(),getWordsMean(word.getWordId()),false,true));

        }
//        向handle发送信息,更新ui
        Message message =new Message();
        message.what=Finish;
        handler.sendMessage(message);

    }
//    获得上方添加的单词释义
    public String getWordsMean(int needWordsId){
//        litepal查释义,查找当前单词需要的释义
        List<Interpretation> interpretationList =LitePal.where("wordId = ?", needWordsId + "").find(Interpretation.class);
//        如果未找到,直接空,找到,返回构建的释义
        if (interpretationList.isEmpty()) {
            // 如果没有找到对应的释义，返回一个空字符串或者错误信息
            return "";
        }
        return new StringBuilder()
                .append(interpretationList.get(0).getWordType())
                .append(". ")
                .append(interpretationList.get(0).getCHSMeaning())
                .toString();
    }
//    返回关闭当前模块,重置数据
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        nowItemNum=0;
        addItemNum=100;
        finish();
    }
}