package com.example.overapp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.overapp.Adapter.WordDetailPhraseAdapter;
import com.example.overapp.Adapter.WordDetailSentenceAdapter;
import com.example.overapp.ItemData.ItemPhrase;
import com.example.overapp.ItemData.ItemSentence;
import com.example.overapp.R;
import com.example.overapp.Utils.MediaPlayHelper;
import com.example.overapp.database.FolderLinkWord;
import com.example.overapp.database.Interpretation;
import com.example.overapp.database.Phrase;
import com.example.overapp.database.Sentence;
import com.example.overapp.database.Word;
import com.example.overapp.database.WordFolder;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;

public class WordDetailActivity extends BaseAcyivity implements View.OnClickListener {
    // 功能区
    private RelativeLayout Star,  Folder;
    private RelativeLayout Continue, Voice;
    private ImageView imgStar;
    private TextView textContinue;

    // 单词
    List<Word> words;

    // 传入的单词ID
    public static int wordId;
    private Word nowWord;
//    展示类
// 单词
private AutofitTextView WordName;

    // 单词发音
    private LinearLayout PhoneUk, PhoneUs;
    private TextView textphoneUk, textphoneUs;

    // 单词释义
    private TextView interpretation;

    // 巧记
    private CardView RemMind;
    private TextView remMind;

    // 例句
    private CardView Sentence;
    private RecyclerView recyclerSentence;
    private WordDetailSentenceAdapter wordDetailSentenceAdapter;
    private List<ItemSentence> itemSentenceList = new ArrayList<>();


    // 词组
    private CardView Phrase;
    private RecyclerView recyclerPhrase;
    private WordDetailPhraseAdapter wordDetailPhraseAdapter;
    private List<ItemPhrase> itemPhraseList = new ArrayList<>();

    public static final String TYPE_NAME = "typeName";
    public static final int TYPE_LEARN = 1;
    public static final int TYPE_GENERAL = 2;

    private int nowType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        initWordDetaile();

        nowType = getIntent().getIntExtra(TYPE_NAME, 0);

        if (nowType == TYPE_GENERAL) {
            textContinue.setText("返回");
        } else {
            textContinue.setText("继续");
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        setData();
    }
    private void initWordDetaile(){
//        功能栏
        Star=findViewById(R.id.layout_worddetail_star);
        imgStar = findViewById(R.id.img_worddetail_star);
        Folder=findViewById(R.id.layout_worddetail_folder);
        Continue=findViewById(R.id.layout_detail_continue);
        Voice=findViewById(R.id.layout_detail_voice);
        textContinue=findViewById(R.id.text_detail_continue);
//        功能按钮点击事件
        Star.setOnClickListener(this);
        Folder.setOnClickListener(this);
        Continue.setOnClickListener(this);
        Voice.setOnClickListener(this);
//        展示区
        WordName=findViewById(R.id.text_worddetaile_wordname);
        PhoneUk=findViewById(R.id.layout_worddetail_phone_uk);
        textphoneUk=findViewById(R.id.text_worddetail_phone_uk);
        PhoneUs=findViewById(R.id.layout_worddetail_phone_us);
        textphoneUs=findViewById(R.id.text_worddetail_phone_us);
        interpretation=findViewById(R.id.text_worddetail_interpretation);
       RemMind=findViewById(R.id.card_worddetail_remMethod);
       remMind=findViewById(R.id.text_worddetail_remMethod);
//       但此部分对英美发音
        PhoneUk.setOnClickListener(this);
        PhoneUs.setOnClickListener(this);
//       例句以及词组部分
        Sentence=findViewById(R.id.card_worddetail_sentence);
        recyclerSentence=findViewById(R.id.recycler_worddetail_sentence);
        Phrase=findViewById(R.id.card_worddetail_phrase);
        recyclerPhrase=findViewById(R.id.recycler_worddetail_phrase);
//设置关于的Adapter
//        sen
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setSmoothScrollbarEnabled(true);
        linearLayoutManager1.setAutoMeasureEnabled(true);
        recyclerSentence.setLayoutManager(linearLayoutManager1);
        recyclerSentence.setHasFixedSize(false);
        recyclerSentence.setNestedScrollingEnabled(false);
        recyclerSentence.setFocusable(false);
        wordDetailSentenceAdapter = new WordDetailSentenceAdapter(itemSentenceList);
        recyclerSentence.setAdapter(wordDetailSentenceAdapter);
//        phrase
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setSmoothScrollbarEnabled(true);
        linearLayoutManager2.setAutoMeasureEnabled(true);
        recyclerPhrase.setLayoutManager(linearLayoutManager2);
        recyclerPhrase.setHasFixedSize(false);
        recyclerPhrase.setNestedScrollingEnabled(false);
        recyclerPhrase.setFocusable(false);
        wordDetailPhraseAdapter = new WordDetailPhraseAdapter(itemPhraseList);
        recyclerPhrase.setAdapter(wordDetailPhraseAdapter);

    }

    @Override
    public void onClick(View view) {
switch (view.getId()){
    case R.id.layout_worddetail_star:
        if (nowWord.getIsCollected() == 1) {
            Glide.with(this).load(R.drawable.icon_star).into(imgStar);
            Word word = new Word();
            word.setToDefault("isCollected");
            word.updateAll("wordId = ?", wordId + "");
            nowWord = LitePal.where("wordId = ?", wordId + "").find(Word.class).get(0);
        } else {
            Glide.with(this).load(R.drawable.icon_star_fill).into(imgStar);
            Word word = new Word();
            word.setIsCollected(1);
            word.updateAll("wordId = ?", wordId + "");
            nowWord = LitePal.where("wordId = ?", wordId + "").find(Word.class).get(0);
        }
        break;
    case R.id.layout_worddetail_folder:
        final List<WordFolder> wordFolders = LitePal.findAll(WordFolder.class);
//        判断是否为空
        if (wordFolders.isEmpty())
            Toast.makeText(this, "暂无单词本", Toast.LENGTH_SHORT).show();
        else {
            String[] folderNames = new String[wordFolders.size()];
            for (int i = 0; i < wordFolders.size(); ++i) {
                folderNames[i] = wordFolders.get(i).getName();
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(WordDetailActivity.this);
            builder.setTitle("存入单词本")
                    .setSingleChoiceItems(folderNames, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            // 延迟500毫秒取消对话框
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    List<FolderLinkWord> folderLinkWords = LitePal.where("wordId = ? and folderId = ?", nowWord.getWordId() + "", wordFolders.get(which).getId() + "").find(FolderLinkWord.class);
                                    if (folderLinkWords.isEmpty()) {
                                        FolderLinkWord folderLinkWord = new FolderLinkWord();
                                        folderLinkWord.setFolderId(wordFolders.get(which).getId());
                                        folderLinkWord.setWordId(nowWord.getWordId());
                                        folderLinkWord.save();
                                        Toast.makeText(WordDetailActivity.this, "单词添加成功！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(WordDetailActivity.this, "单词本中已存在", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, 200);
                        }
                    }).show();
        }
        break;
//        当点击音标时调用工具类发音
    case  R.id.layout_worddetail_phone_uk:
        MediaPlayHelper.play(MediaPlayHelper.VOICE_UK,words.get(0).getWord());
        break;
    case R.id.layout_worddetail_phone_us:
        MediaPlayHelper.play(MediaPlayHelper.VOICE_US, words.get(0).getWord());
        break;
//        工具
    case R.id.layout_detail_voice:
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayHelper.play(words.get(0).getWord());
            }
        }).start();
        break;
    case R.id.layout_detail_continue:
        if (nowType == TYPE_GENERAL) {
            onBackPressed();
        } else {
            LearnActivity.needUpdate = true;
            onBackPressed();
        }
        break;
}
    }
    private void setData() {
        words = LitePal.where("wordId = ?", wordId + "").find(Word.class);
        nowWord = words.get(0);
        // 设置收藏
        if (nowWord.getIsCollected() == 1)
            Glide.with(this).load(R.drawable.icon_star_fill).into(imgStar);
        else
            Glide.with(this).load(R.drawable.icon_star).into(imgStar);
        // 设置名称
       WordName.setText(nowWord.getWord());
        // 设置英音
        if (nowWord.getUkPhone() != null) {
            PhoneUk.setVisibility(View.VISIBLE);
           textphoneUk.setText(nowWord.getUkPhone());
        } else {
            PhoneUk.setVisibility(View.GONE);
        }
        // 设置美音
        if (nowWord.getUsPhone() != null) {
            PhoneUs.setVisibility(View.VISIBLE);
            textphoneUs.setText(nowWord.getUsPhone());
        } else {
            PhoneUs.setVisibility(View.GONE);
        }
        // 设置中文
        List<Interpretation> interpretationList = LitePal.where("wordId = ?", wordId + "").find(Interpretation.class);
        StringBuilder chinese = new StringBuilder();
        StringBuilder english = new StringBuilder();
        ArrayList<String> chsMeans = new ArrayList<>();
        ArrayList<String> enMeans = new ArrayList<>();
        for (int i = 0; i < interpretationList.size(); ++i) {
            chsMeans.add(interpretationList.get(i).getWordType() + ". " + interpretationList.get(i).getCHSMeaning());
            if (interpretationList.get(i).getENMeaning() != null) {
                enMeans.add(interpretationList.get(i).getWordType() + ". " + interpretationList.get(i).getENMeaning());
            }
        }
        for (int i = 0; i < chsMeans.size(); ++i) {
            if (i != chsMeans.size() - 1)
                chinese.append(chsMeans.get(i) + "\n");
            else
                chinese.append(chsMeans.get(i));
        }
        interpretation.setText(chinese.toString());
        // 设置巧记
        if (nowWord.getRemMethod() != null) {
            RemMind.setVisibility(View.VISIBLE);
            remMind.setText(nowWord.getRemMethod());
        } else {
            RemMind.setVisibility(View.GONE);
        }
        // 设置例句
        List<Sentence> sentenceList = LitePal.where("wordId = ?", wordId + "").find(Sentence.class);
        if (!sentenceList.isEmpty()) {
            Sentence.setVisibility(View.VISIBLE);
            setSentenceData(sentenceList);
        } else {
            Sentence.setVisibility(View.GONE);
        }
        // 设置词组
        List<Phrase> phraseList = LitePal.where("wordId = ?", wordId + "").find(Phrase.class);
        if (!phraseList.isEmpty()) {
            Phrase.setVisibility(View.VISIBLE);
            setPhraseData(phraseList);
        } else {
            Phrase.setVisibility(View.GONE);
        }



    }


    private void setSentenceData(List<Sentence> sentenceList) {
        itemSentenceList.clear();
        for (Sentence sentence : sentenceList) {
            itemSentenceList.add(new ItemSentence(sentence.getChsSentence(), sentence.getEnSentence()));
        }
        wordDetailSentenceAdapter.notifyDataSetChanged();
    }
    private void setPhraseData(List<Phrase> phraseList) {
        itemPhraseList.clear();
        for (Phrase phrase : phraseList) {
            itemPhraseList.add(new ItemPhrase(phrase.getChsPhrase(), phrase.getEnPhrase()));
        }
        wordDetailPhraseAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        WordListActivity.needUpdate = true;
        LearnActivity.needUpdate = true;
        MediaPlayHelper.releasePlayer();
    }

}