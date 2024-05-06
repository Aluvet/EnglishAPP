package com.example.overapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.Adapter.WordFolderListAdapter;
import com.example.overapp.ItemData.ItemWordListContent;
import com.example.overapp.R;
import com.example.overapp.Utils.WordsControllor;
import com.example.overapp.database.FolderLinkWord;
import com.example.overapp.database.Interpretation;
import com.example.overapp.database.Word;
import com.example.overapp.database.WordFolder;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
//展示存入的单词
public class WordFoldDetailActivity extends BaseAcyivity {
    public static int currentFolderId;

    private CardView cardRemark;

    private TextView textName, textRemark;

    private ImageView imgStart;
//    展示
    private RecyclerView recyclerView;

    private List<ItemWordListContent> wordListContents = new ArrayList<>();

    private WordFolderListAdapter wordListAdapter;

    private String[] editType = {"更改名称", "更改备注"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_fold_detail);
//      初始化
        initWordFoldDetail();
//        上方
        windowSlide(Gravity.TOP);
//        recycle固定格式，同其他设置
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
//传入数据并设置sdapter
        wordListAdapter = new WordFolderListAdapter(wordListContents);
        recyclerView.setAdapter(wordListAdapter);
//查询当前单词的id
        List<WordFolder> wordFolders = LitePal.where("id = ?", currentFolderId + "").find(WordFolder.class);
//        设置查询到的单词的名字
        textName.setText(wordFolders.get(0).getName());
//        非空备注
        if (wordFolders.get(0).getRemark() != null) {
//            备注空啥都不展示,非空展示到固定空间中
            if (TextUtils.isEmpty(wordFolders.get(0).getRemark().trim())) {
                textRemark.setVisibility(View.GONE);
                cardRemark.setVisibility(View.GONE);
            } else {
                cardRemark.setVisibility(View.VISIBLE);
                textRemark.setVisibility(View.VISIBLE);
                textRemark.setText(wordFolders.get(0).getRemark());
            }
        } else {
            textRemark.setVisibility(View.GONE);
            cardRemark.setVisibility(View.GONE);
        }
//查询folderlinkword
        List<FolderLinkWord> folderLinkWords = LitePal.where("folderId = ?", currentFolderId + "").find(FolderLinkWord.class);
//        列表清空,并进行数据填充
        wordListContents.clear();
//        列表非空,循环列表
        if (!folderLinkWords.isEmpty()) {

            for (FolderLinkWord folderLinkWord : folderLinkWords) {
//                在word中查询folderlinkword中对应的id的单词,取第一个添加到实体对象item中
                List<Word> words = LitePal.where("wordId = ?", folderLinkWord.getWordId() + "").select("wordId", "word").find(Word.class);
                Word word = words.get(0);
                wordListContents.add(new ItemWordListContent(word.getWordId(), word.getWord(), getMeans(word.getWordId()), false, false));
            }
//            通知变化
            wordListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "暂无单词", Toast.LENGTH_SHORT).show();
        }
//        name点击事件
        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                弹出对话框，让用户选择是否修改
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(WordFoldDetailActivity.this);
                builder2.setSingleChoiceItems(editType, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        final int type = which;
                        // 延迟500毫秒取消对话框
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                final View dialogView = LayoutInflater.from(WordFoldDetailActivity.this)
                                        .inflate(R.layout.item_edit, null);
                                final EditText editText = dialogView.findViewById(R.id.edit_text);
//                                更改备注,则在设置获取的数据
                                if (type == 0)
                                    editText.setText(textName.getText().toString());
                                else {
//                                    更改备注
                                    List<WordFolder> wordFolders = LitePal.where("id = ?", currentFolderId + "").find(WordFolder.class);
                                    textName.setText(wordFolders.get(0).getName());
                                    if (wordFolders.get(0).getRemark() != null) {
                                        if (TextUtils.isEmpty(wordFolders.get(0).getRemark().trim())) {
                                            editText.setText("");
                                        } else {
                                            editText.setText(wordFolders.get(0).getRemark());
                                        }
                                    } else {
                                        editText.setText("");
                                    }
                                }
                                AlertDialog.Builder inputDialog = new AlertDialog.Builder(WordFoldDetailActivity.this);
                                if (type == 0)
                                    inputDialog.setTitle("编辑单词本名称");
                                else
                                    inputDialog.setTitle("编辑备注");
                                inputDialog.setView(dialogView)
                                        .setPositiveButton("确定",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        非空
                                                        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
//                                                            编辑名字
                                                            if (type == 0) {
//                                                                创建对象,分别赋值,更新
                                                                WordFolder wordFolder = new WordFolder();
//
                                                                wordFolder.setName(editText.getText().toString().trim());
                                                                wordFolder.updateAll("id = ?", currentFolderId + "");
                                                                dialog.dismiss();
                                                                textName.setText(editText.getText().toString().trim());
                                                                Toast.makeText(WordFoldDetailActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                                                            } else {
//                                                                更新备注,更新赋值,修改数据
                                                                WordFolder wordFolder = new WordFolder();
                                                                wordFolder.setRemark(editText.getText().toString().trim());
                                                                wordFolder.updateAll("id = ?", currentFolderId + "");
                                                                cardRemark.setVisibility(View.VISIBLE);
                                                                textRemark.setVisibility(View.VISIBLE);
                                                                textRemark.setText(editText.getText().toString().trim());
                                                            }
                                                        } else {
//                                                            空
                                                            if (type == 0)
//                                                                标题不能为空
                                                                Toast.makeText(WordFoldDetailActivity.this, "不得为空", Toast.LENGTH_SHORT).show();
                                                            else {
//                                                                备注无所谓
                                                                WordFolder wordFolder = new WordFolder();
                                                                wordFolder.setToDefault("remark");
                                                                wordFolder.updateAll("id = ?", currentFolderId + "");
//                                                                设置为小时
                                                                textRemark.setVisibility(View.GONE);
                                                                cardRemark.setVisibility(View.GONE);
                                                                Toast.makeText(WordFoldDetailActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                })
                                        .setNegativeButton("取消", null).show();
                            }
                        }, 200);
                    }
                }).show();
            }
        });
//点击事件，进行奠基石进行跳转学习单词
        imgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                查询单词与foldword相联系的
                List<FolderLinkWord> folderLinkWords = LitePal.where("folderId = ?", currentFolderId + "").find(FolderLinkWord.class);
//                列表非空,循环将每个单词的ID添加到WordsControllor.needLearnWords列表中
                if (!folderLinkWords.isEmpty()) {
                    WordsControllor.needLearnWords.clear();
                    for (ItemWordListContent itemWordListContent : wordListContents) {
                        WordsControllor.needLearnWords.add(itemWordListContent.getWordId());
                    }
                    WordsControllor.justLearnedWords.clear();
                    WordsControllor.needReviewWords.clear();
                    LearnActivity.lastWordMean = "";
                    LearnActivity.lastWord = "";
//                    不为空,启动一个新的LearnActivity,参数（MODE_ONCE）单次学习模式
                    if (WordsControllor.needLearnWords.size() != 0) {
                        Intent intent = new Intent(WordFoldDetailActivity.this, LearnActivity.class);
                        intent.putExtra(LearnActivity.MODE_NAME, LearnActivity.MODE_ONCE);
                        startActivity(intent);
                        Toast.makeText(WordFoldDetailActivity.this, "开始背单词", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(WordFoldDetailActivity.this, "请输入合法单词", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void initWordFoldDetail(){

        textName = findViewById(R.id.text_wordfolddetile_name);
        textRemark = findViewById(R.id.text_wordfolddetile_remark);
        imgStart = findViewById(R.id.img_wordfolddetile_start);
        cardRemark = findViewById(R.id.card_wordfolddetile_remark);
//        展示
        recyclerView = findViewById(R.id.recycler_wordfolddetile);
    }
//    定义方法，得到意思
    private String getMeans(int id) {
//        据id的释义
        List<Interpretation> interpretationList = LitePal.where("wordId = ?", id + "").find(Interpretation.class);
//        拼接
        StringBuilder stringBuilder = new StringBuilder();
//        循环拼接
        for (int i = 0; i < interpretationList.size(); ++i) {
            stringBuilder.append(interpretationList.get(i).getWordType() + ". " + interpretationList.get(i).getCHSMeaning());
            if (i != interpretationList.size() - 1)
                stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

}