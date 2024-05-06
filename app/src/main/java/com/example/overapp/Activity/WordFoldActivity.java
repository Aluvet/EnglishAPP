package com.example.overapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.overapp.Adapter.WordFoldAdapter;
import com.example.overapp.ItemData.ItemWordFold;
import com.example.overapp.R;
import com.example.overapp.database.FolderLinkWord;
import com.example.overapp.database.WordFolder;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
//单词本新建添加收藏单词
public class WordFoldActivity extends BaseAcyivity {
private RecyclerView recyclerView;
private List<ItemWordFold> wordFoldList =new ArrayList<>();
private ImageView imgAdd;
private WordFoldAdapter wordFoldAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_fold);
//        动画效果
        windowExplode();
//        初始化
        initWordFold();
//        设置布局瀑布流列2
//        RecyclerView+StaggeredGridLayoutManage，在单词本界面设置两列竖直展示item
        StaggeredGridLayoutManager layoutManager =new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//        不用动画效果
        recyclerView.setItemAnimator(null);
//        创建新的实例，传入数据，并设置，将adapter传给recycleview
        wordFoldAdapter =new WordFoldAdapter(wordFoldList);
        recyclerView.setAdapter(wordFoldAdapter);
//添加按钮，要创建新的单词本，进行界面跳转进行单词本的创建
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(WordFoldActivity.this,CreateFoldActivity.class);
                        startActivity(intent);
            }
        });
    }
    public void  initWordFold(){
        imgAdd=findViewById(R.id.img_wordfold_add);
        recyclerView =findViewById(R.id.recycler_wordFold);
    }
//从一数据库中获取数据，并更新一个列表的显示
    @Override
    protected void onStart() {
        super.onStart();
//        litepal查询所有数据并进行保存
        List<WordFolder> wordFolders = LitePal.findAll(WordFolder.class);
//        判断是否存在
        if (!wordFolders.isEmpty()){
//            先清空列表，防止存在旧数据
            wordFoldList.clear();
//            遍历列表
            for (WordFolder wordFolder:wordFolders){
//                根据folderId字段与当前wordFolder的id匹配，查找所有FolderLinkWord记录
                List<FolderLinkWord> folderLinkWords =LitePal.where("folderId=?",wordFolder.getId()+"").find(FolderLinkWord.class);
//像列表添加单词本id，名字，备注，保存单词数多少
                wordFoldList.add(new ItemWordFold(wordFolder.getId(),wordFolder.getName(),wordFolder.getRemark(),folderLinkWords.size()));}
//            通知更新
            wordFoldAdapter.notifyDataSetChanged();

        }
    }
}