package com.example.overapp.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.Adapter.WordMeanChoiceAdapter;
import com.example.overapp.Interface.OnItemClickListener;
import com.example.overapp.ItemData.ItemWordMeanChoice;
import com.example.overapp.R;
import com.example.overapp.Utils.ActivityCollector;
import com.example.overapp.Utils.MediaPlayHelper;
import com.example.overapp.Utils.TimeController;
import com.example.overapp.Utils.WordsControllor;
import com.example.overapp.database.Interpretation;
import com.example.overapp.database.LearnTime;
import com.example.overapp.database.Sentence;
import com.example.overapp.database.Word;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//学习背单词界面 ,需要item，切换
public class LearnActivity extends BaseAcyivity implements View.OnClickListener {
    private WordMeanChoiceAdapter wordMeanChoiceAdapter;
    private List<ItemWordMeanChoice> wordMeanChoices=new ArrayList<>();
private RelativeLayout cardDele,cardHelp,cardVoice;
    private RelativeLayout cardKnow, cardNotKnow, cardFuzz;
private TextView wordPlay, wordPhonplay,wordSentenceplay,topLastWord, topLastWordMean,top_NewLearnword_Num,top_ReviewWord_Num;
  private CardView cardSentenceTip;
    // 记录上一个单词
    public static String lastWord;
    public static String lastWordMean;

    private RecyclerView recyclerView;
    private LinearLayout layoutBottomReview;

    private LinearLayout layoutBottomLearn;
//    更新学习界面
    public static boolean needUpdate = true;
    private int nowlearnMode;

    private String tipSentence;
    // 学习时间记录
    private long startTime = -1;

    public static final String MODE_NAME = "learnwordmode";

    public static final int MODE_GENERAL = 1;
    public static final int MODE_ONCE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
//        初始化
        init();
//        得到其他传来的学习数据，普通
        nowlearnMode = getIntent().getIntExtra(MODE_NAME, MODE_GENERAL);
//获得现在的日期,有时间及日期
        startTime = TimeController.getNowTimeStamp();
//同其他adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        wordMeanChoiceAdapter = new WordMeanChoiceAdapter(wordMeanChoices);
//        点击事件
        wordMeanChoiceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, ItemWordMeanChoice itemWordMeanChoice) {
//               判断是否是第一次点击
                if (WordMeanChoiceAdapter.isFirstClick) {
                    Log.d("LearnActivity", "用户选择了：" + itemWordMeanChoice.getId());
                    Log.d("LearnActivity", "对应的目标是： " + WordsControllor.nowWordId);
                    // 答错了，用户选择与对应目标不一样
                    if (itemWordMeanChoice.getId() != WordsControllor.nowWordId) {
                    //直接处理及时复习和一般复习，分别调用对应方法
                        switch (WordsControllor.nowLearnMode) {
                            case WordsControllor.REVIEW_AT_TIME:
                                WordsControllor.reviewNewWordDone(WordsControllor.nowWordId, false);
                                break;
                            case WordsControllor.REVIEW_GENERAL:
                                WordsControllor.reviewOneWordDone(WordsControllor.nowWordId, false);
                                break;
                        }
//                        更新对应的UI状态
                        itemWordMeanChoice.setRight(ItemWordMeanChoice.WRONG);
                        wordMeanChoiceAdapter.notifyDataSetChanged();
                        WordMeanChoiceAdapter.isFirstClick = false;
//                        打错的情况延迟启动单词列表界面
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                WordDetailActivity.wordId = WordsControllor.nowWordId;
                                Intent intent = new Intent();
                                intent.setClass(LearnActivity.this, WordDetailActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(WordDetailActivity.TYPE_NAME, WordDetailActivity.TYPE_LEARN);
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LearnActivity.this).toBundle());
                                WordMeanChoiceAdapter.isFirstClick = true;
                            }
                        }, 250);
                    }
//                    相同增加测试或掌握程度
                    else {
                        switch (WordsControllor.nowLearnMode) {
                            case WordsControllor.REVIEW_AT_TIME:
                                WordsControllor.reviewNewWordDone(WordsControllor.nowWordId, true);
                                break;
                            case WordsControllor.REVIEW_GENERAL:
                                WordsControllor.reviewOneWordDone(WordsControllor.nowWordId, true);
                                break;
                        }
//                        更新数据
                        itemWordMeanChoice.setRight(ItemWordMeanChoice.RIGHT);
                        wordMeanChoiceAdapter.notifyDataSetChanged();
                        WordMeanChoiceAdapter.isFirstClick = false;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateStatus();
                                WordMeanChoiceAdapter.isFirstClick = true;
                            }
                        }, 250);
                    }
                    Log.d("", "id+" + itemWordMeanChoice.getId());
                }
            }
        });

        recyclerView.setAdapter(wordMeanChoiceAdapter);
    }
    public void init(){
        recyclerView = findViewById(R.id.recyclerview_word_mean_list);
        layoutBottomReview = findViewById(R.id.layout_word_bottom);
        layoutBottomLearn = findViewById(R.id.linear_learn_control);
//        功能性卡片设置嗲点击时间
        cardDele=findViewById(R.id.layout_word_delete);
        cardDele.setOnClickListener(this);
        cardFuzz=findViewById(R.id.card_fuzzy);
        cardFuzz.setOnClickListener(this);
        cardHelp=findViewById(R.id.layout_word_help);
        cardHelp.setOnClickListener(this);
        cardKnow =findViewById(R.id.card_know);
        cardKnow.setOnClickListener(this);
        cardNotKnow =findViewById(R.id.card_no_know);
        cardNotKnow.setOnClickListener(this);
        cardVoice=findViewById(R.id.layout_word_voice);
        cardVoice.setOnClickListener(this);
//        展示页
        cardSentenceTip=findViewById(R.id.card_learn_Sentencetip);
        wordPlay=findViewById(R.id.text_learn_word);
        wordPhonplay =findViewById(R.id.text_learn_word_phone);
        wordSentenceplay =findViewById(R.id.text_lw_Sentencetip);
//        顶部展示
        topLastWord=findViewById(R.id.text_wordLearn_top_word);
        topLastWordMean=findViewById(R.id.text_wordLearn_top_mean);
        top_NewLearnword_Num =findViewById(R.id.text_new_num_top);
        top_ReviewWord_Num=findViewById(R.id.text_review_num_top);
    }
//点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            明白，将单词从新学移除
            case R.id.card_know:
                WordsControllor.learnNewWordDone(WordsControllor.nowWordId);
                updateStatus();
                break;
            case R.id.card_no_know:
//                不懂，将单词放入复习列表
//                跳转，展示对应单词
                WordDetailActivity.wordId = WordsControllor.nowWordId;
                Intent intent = new Intent();
                intent.setClass(LearnActivity.this, WordDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(WordDetailActivity.TYPE_NAME, WordDetailActivity.TYPE_LEARN);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LearnActivity.this).toBundle());
                WordsControllor.learnNewWordDone(WordsControllor.nowWordId);
                break;
            case R.id.card_fuzzy:
//                句子部分展示，将句子展示卡片显示，并播放音频
                if (!TextUtils.isEmpty(tipSentence.trim())) {
                    cardSentenceTip.setVisibility(View.VISIBLE);
                    wordSentenceplay.setText(tipSentence);
                    MediaPlayHelper.play(tipSentence);
                } else {
                    Toast.makeText(this, "暂无提示", Toast.LENGTH_SHORT).show();
                }
                break;
//                复习部分
            case R.id.layout_word_delete:
//                删除，移除单词
                WordsControllor.removeOneWord(WordsControllor.nowWordId);
                updateStatus();
                break;
//                播放对应单词
            case R.id.layout_word_voice:
                MediaPlayHelper.play( wordPlay.getText().toString());
                break;
//                帮助，开启单词展示活动
            case R.id.layout_word_help:
                ActivityCollector.startOtherActivity(LearnActivity.this, WordDetailActivity.class);
                WordDetailActivity.wordId = WordsControllor.nowWordId;
                break;

        }
    }
//返回主界面fragment
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(LearnActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
//        为ture调用更新
        if (needUpdate) {
            updateStatus();
            needUpdate = false;
        }
    }

//更新学习模式以及界面
//        top_NewLearnword_Num =findViewById(R.id.text_new_num_top);
//        top_ReviewWord_Num=findViewById(R.id.text_review_num_top);
    public void updateStatus() {
//        提示句子为空，且句子不可见，当用户点击提示显示，设置对应控件
        tipSentence = "";
        cardSentenceTip.setVisibility(View.GONE);
        top_NewLearnword_Num.setText("新学" + WordsControllor.needLearnWords.size());
        top_ReviewWord_Num.setText("复习" + (WordsControllor.needReviewWords.size() +WordsControllor.justLearnedWords.size()));
//       设置学习的顺序
        WordsControllor.nowLearnMode = WordsControllor.whatToDo();
        switch (WordsControllor.nowLearnMode) {
//          根据不同模式展示对应界面
            case WordsControllor.REVIEW_AT_TIME:
                WordsControllor.nowWordId = WordsControllor.reviewNewWord();
                showReview();
                break;
            case WordsControllor.REVIEW_GENERAL:
                WordsControllor.nowWordId = WordsControllor.reviewOneWord();
                showReview();
                break;
            case WordsControllor.NEW_LEARN:
                WordsControllor.nowWordId = WordsControllor.learnNewWord();
                showLearn();
                break;
            case WordsControllor.TODAY_MASK_DONE:
                switch (nowlearnMode) {
                    case MODE_GENERAL:
                        Toast.makeText(this, "已完成今日任务", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, FinishActivity.class);
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LearnActivity.this).toBundle());
                        finish();
                        break;
                    case MODE_ONCE:
                        Toast.makeText(this, "复习完毕", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        break;
                }
                break;
        }

        Log.d("LearnActivity", "currentId" + WordsControllor.nowWordId);

        // 找到该单词的数据
        List<Word> words = LitePal.where("wordId = ?", WordsControllor.nowWordId+ "").select("wordId", "word", "ukPhone", "usPhone").find(Word.class);
//        单词非空，分别获取对应数据，绑定到对应控件上
        if (!words.isEmpty()) {
            Word word = words.get(0);
            wordPlay.setText(word.getWord());
            if (word.getUsPhone() != null)
                wordPhonplay.setText(word.getUsPhone());
            else
                wordPhonplay.setText(word.getUkPhone());
//没完成，延迟播放，当前单词的发音
            if (WordsControllor.nowLearnMode != WordsControllor.TODAY_MASK_DONE)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaPlayHelper.play(wordPlay.getText().toString());
                    }
                }).start();

            // 得到该单词的释义
            List<Interpretation> interpretations = LitePal.where("wordId = ?", WordsControllor.nowWordId + "").find(Interpretation.class);
//            stringbuilder追加拼接
            StringBuilder stringBuilder = new StringBuilder();
            if (!interpretations.isEmpty()) {
                stringBuilder.append(interpretations.get(0).getWordType() + ". " + interpretations.get(0).getCHSMeaning());
            }

            // 得到该单词的例句
            List<Sentence> sentences = LitePal.where("wordId = ?", WordsControllor.nowWordId + "").find(Sentence.class);
            if (!sentences.isEmpty())
                tipSentence = sentences.get(0).getEnSentence();
//         当前学习模式是及时复习以及普通复习模式时，先清空
            if (WordsControllor.nowLearnMode == WordsControllor.REVIEW_AT_TIME ||
                    WordsControllor.nowLearnMode == WordsControllor.REVIEW_GENERAL) {
                wordMeanChoices.clear();
                // litepal查询不是当前单词的释义，将顺序打乱
                List<Interpretation> interpretationWrongs = LitePal.where("wordId != ?", WordsControllor.nowWordId + "").find(Interpretation.class);
                Collections.shuffle(interpretationWrongs);
//             单词意思选择可见，添加当前单词的正确释义
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    wordMeanChoices.add(new ItemWordMeanChoice(WordsControllor.nowWordId, stringBuilder.toString(), ItemWordMeanChoice.NOTSTART));
                    // 在上述打乱单词的意思列表中，获取三个释义
                    for (int i = 0; i < 3; ++i) {
                        wordMeanChoices.add(new ItemWordMeanChoice(-1, interpretationWrongs.get(i).getWordType() + ". " + interpretationWrongs.get(i).getCHSMeaning(), ItemWordMeanChoice.NOTSTART));
                    }
                    // 打乱顺序，通知更新
                    Collections.shuffle(wordMeanChoices);
                    wordMeanChoiceAdapter.notifyDataSetChanged();
                }

            }
            //    topLastWord=findViewById(R.id.text_wordLearn_top_word);
//        topLastWordMean=findViewById(R.id.text_wordLearn_top_mean);
//获取控件绑定数据
            topLastWord.setText(lastWord);
            topLastWordMean.setText(lastWordMean);

            lastWord = words.get(0).getWord();
            lastWordMean = stringBuilder.toString();
        } else {
            Toast.makeText(this, "发生错误，请重试", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

    }
//展示学习界面
    private void showLearn() {
        recyclerView.setVisibility(View.GONE);
        layoutBottomReview.setVisibility(View.GONE);
        layoutBottomLearn.setVisibility(View.VISIBLE);
    }
//复习界面
    private void showReview() {
        recyclerView.setVisibility(View.VISIBLE);
        layoutBottomReview.setVisibility(View.VISIBLE);
        layoutBottomLearn.setVisibility(View.GONE);
    }
//销毁后将界面重置为学习界面
    @Override
    protected void onDestroy() {
        super.onDestroy();
        needUpdate = true;
        long endTime = TimeController.getNowTimeStamp();
        long duration = endTime - startTime;
        startTime = -1;
        LearnTime learnTime = new LearnTime();
        List<LearnTime> learnTimeList = LitePal.where("date = ?", TimeController.getPastDateWithYear(0)).find(LearnTime.class);
        if (learnTimeList.isEmpty()) {
            learnTime.setTime(duration + "");
            learnTime.setDate(TimeController.getPastDateWithYear(0));
            learnTime.save();
        } else {
            int lastTime = Integer.valueOf(learnTimeList.get(0).getTime());
            learnTime.setTime((lastTime + duration) + "");
            learnTime.updateAll("date = ?", TimeController.getPastDateWithYear(0));
        }
    }

}