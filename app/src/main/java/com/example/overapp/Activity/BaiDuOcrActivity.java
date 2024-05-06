package com.example.overapp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.overapp.R;
import com.example.overapp.Utils.TimeController;
import com.example.overapp.Utils.WordsControllor;
import com.example.overapp.database.FolderLinkWord;
import com.example.overapp.database.Word;
import com.example.overapp.database.WordFolder;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;
//百度ocr文字识别
public class BaiDuOcrActivity extends BaseAcyivity  {
    private EditText wordResult;

    private RelativeLayout cardWordStart;

    private ImageView imgWordJoinFold;

    private final String[] choice = {"加入已有单词本", "自动新建单词本并存入"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_du_ocr);
//初始化
        initBaiDuOCR();

        StringBuilder stringBuilder = new StringBuilder();
        if (!WordsControllor.needLearnWords.isEmpty()) {
            for (Integer integer : WordsControllor.needLearnWords) {
                List<Word> words = LitePal.where("wordId = ?", integer + "").select("word").find(Word.class);
                stringBuilder.append(words.get(0).getWord() + "\n");
            }
            wordResult.setText(stringBuilder.toString());
        }
//        当用户点击背单词时，用户跳转
        cardWordStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] result = wordResult.getText().toString().toLowerCase().split("\n");
                if (result.length >= 1) {
                    WordsControllor.needLearnWords.clear();
                    HashMap<Integer, Integer> map = new HashMap<>();
                    for (int i = 0; i < result.length; ++i) {
                        Log.d("BaiDuOcrActivity", i + result[i]);
                        List<Word> words = LitePal.where("word = ?", result[i]).select("wordId", "word").find(Word.class);
                        if (!words.isEmpty()) {
                            Log.d("BaiDuOcrActivity", i + "我找到了" + words.get(0).getWord());
                            if (!map.containsValue(words.get(0).getWordId())) {
                                map.put(i, words.get(0).getWordId());
                                Log.d("BaiDuOcrActivity", "我已添加" + words.get(0).getWord());
                            }
                        }
                    }
                    for (int ii : map.keySet()) {
                        WordsControllor.needLearnWords.add(map.get(ii));
                    }
                    Log.d("BaiDuOcrActivity", "长度：" + WordsControllor.needLearnWords.size());
                    WordsControllor.justLearnedWords.clear();
                    WordsControllor.needReviewWords.clear();
                    LearnActivity.lastWordMean = "";
                    LearnActivity.lastWord = "";
                    if (WordsControllor.needLearnWords.size() != 0) {
                        Intent intent = new Intent(BaiDuOcrActivity.this, LearnActivity.class);
                        intent.putExtra(LearnActivity.MODE_NAME, LearnActivity.MODE_ONCE);
                        startActivity(intent);
                        Toast.makeText(BaiDuOcrActivity.this, "开始背单词", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BaiDuOcrActivity.this, "当前单词库并没有图片中显示的单词偶", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        imgWordJoinFold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                当点击文件图片是，弹出对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(BaiDuOcrActivity.this);
                builder.setSingleChoiceItems(choice, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        // 延迟500毫秒取消对话框
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                if (which == 0) {
                                    final List<WordFolder> wordFolders = LitePal.findAll(WordFolder.class);
                                    if (!wordFolders.isEmpty()){
                                        String[] folderNames = new String[wordFolders.size()];
                                        for (int i = 0; i < wordFolders.size(); ++i) {
                                            folderNames[i] = wordFolders.get(i).getName();
                                        }
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(BaiDuOcrActivity.this);
                                        builder2.setSingleChoiceItems(folderNames, -1, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, final int which) {
                                                // 延迟500毫秒取消对话框
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialog.dismiss();
//                                                        EditText控件中获取文本，转换为小写，并使用换行符\n分割成一个字符串数组
                                                        String[] result = wordResult.getText().toString().toLowerCase().split("\n");
//                                                        检查长度书否大于一
                                                        if (result.length >= 1) {
//                                                            创建一个HashMap对象map，存储单词索引和与之对应的单词ID。
                                                            HashMap<Integer, Integer> map = new HashMap<>();
//                                                            对单词进行遍历
                                                            for (int i = 0; i < result.length; ++i) {
                                                                Log.d("BaiDuOcrActivity", i + result[i]);
                                                                List<Word> words = LitePal.where("word = ?", result[i]).select("wordId", "word").find(Word.class);
                                                                if (!words.isEmpty()) {
                                                                    Log.d("BaiDuOcrActivity", i + "我找到了" + words.get(0).getWord());
//                                                                    先判断当前map中是否存在键值对
//                                                                    containsValue() 方法检查 hashMap 中是否存在指定的 value 对应的映射关系。
                                                                    if (!map.containsValue(words.get(0).getWordId())) {
//                                                                        不存在直接将当前单词存入map
                                                                        map.put(i, words.get(0).getWordId());
                                                                        Log.d("BaiDuOcrActivity", "我已添加" + words.get(0).getWord());
                                                                    }
                                                                }
                                                            }
                                                            for (int ii : map.keySet()) {
                                                                List<FolderLinkWord> folderLinkWords = LitePal.where("wordId = ? and folderId = ?", map.get(ii) + "", wordFolders.get(which).getId() + "").find(FolderLinkWord.class);
//                                                               如果当前单词为空，创建新的保存数据
                                                                if (folderLinkWords.isEmpty()) {
                                                                    FolderLinkWord folderLinkWord = new FolderLinkWord();
                                                                    folderLinkWord.setFolderId(wordFolders.get(which).getId());
                                                                    folderLinkWord.setWordId(map.get(ii));
                                                                    folderLinkWord.save();
                                                                }
                                                            }
                                                            Toast.makeText(BaiDuOcrActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }, 200);
                                            }
                                        }).show();
                                    } else {
                                        Toast.makeText(BaiDuOcrActivity.this, "当前暂无单词本", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    String[] result =wordResult.getText().toString().toLowerCase().split("\n");
                                    if (result.length >= 1) {
//                                        同上
                                        HashMap<Integer, Integer> map = new HashMap<>();
                                        for (int i = 0; i < result.length; ++i) {
                                            Log.d("BaiDuOcrActivity", i + result[i]);
                                            List<Word> words = LitePal.where("word = ?", result[i]).select("wordId", "word").find(Word.class);
                                            if (!words.isEmpty()) {
                                                if (!map.containsValue(words.get(0).getWordId())) {
                                                    map.put(i, words.get(0).getWordId());
                                                }
                                            }
                                        }
//                                        获得当前时间
                                        long currentTime = TimeController.getNowTimeStamp();
                                        WordFolder wordFolder = new WordFolder();
                                        wordFolder.setName("拍照取词");
                                        wordFolder.setRemark("创建于：" + TimeController.getStringDateDetail(currentTime));
                                        if (wordFolder.save()) {
                                            List<WordFolder> wordFolders = LitePal.where("createTime = ? and name = ?", currentTime + "", "拍照").find(WordFolder.class);
                                            if (!wordFolders.isEmpty()) {
                                                for (int ii : map.keySet()) {
                                                    List<FolderLinkWord> folderLinkWords = LitePal.where("wordId = ? and folderId = ?", map.get(ii) + "", wordFolders.get(0).getId() + "").find(FolderLinkWord.class);
                                                    if (folderLinkWords.isEmpty()) {
                                                        FolderLinkWord folderLinkWord = new FolderLinkWord();
                                                        folderLinkWord.setFolderId(wordFolders.get(0).getId());
                                                        folderLinkWord.setWordId(map.get(ii));
                                                        folderLinkWord.save();
                                                    }
                                                }
                                            }
                                            Toast.makeText(BaiDuOcrActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }, 200);
                    }
                }).show();
            }
        });

    }
    private void initBaiDuOCR() {
        wordResult = findViewById(R.id.edit_ocr_result);
        cardWordStart = findViewById(R.id.layout_ocr_bottom);
        imgWordJoinFold = findViewById(R.id.img_ocr_joinfold);
    }
//    返回结束当前活动
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
//    Activity即将被销毁时，清楚列表中的元素
    @Override
    protected void onDestroy() {
        super.onDestroy();
        WordsControllor.needLearnWords.clear();
    }
}