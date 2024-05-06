package com.example.overapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.overapp.Activity.WordDetailActivity;
import com.example.overapp.Activity.WordFoldDetailActivity;
import com.example.overapp.ItemData.ItemWordListContent;
import com.example.overapp.R;
import com.example.overapp.Utils.MediaPlayHelper;
import com.example.overapp.Utils.MyApplication;

import com.example.overapp.database.FolderLinkWord;

import org.litepal.LitePal;

import java.util.List;
/*
adapter大体实现
1.将数据源传递进来
2.onCreateViewHolder会被调用，进而创建子视图view，将其实例化，
3.然后将其进行ViewHolder，将子视图里的控件进行实例化
4.onBindViewHolder进行数据绑定，最其控件进行赋值
给recycle设置adapter 并继承自recycleView*/
//创建单词本列表的适配器,ok后设置activity
public class WordFolderListAdapter extends RecyclerView.Adapter<WordFolderListAdapter.ViewHolder> {
    private List<ItemWordListContent>  mItemWordListcontents;
    public WordFolderListAdapter(List<ItemWordListContent> mItemWordListcontents) {
        this.mItemWordListcontents = mItemWordListcontents;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wordfold_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);
//        删除设置点击
        holder.imgReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                同其他adaorter获取位置
                int position = holder.getAdapterPosition();
//                将当前位置的item
                ItemWordListContent itemWordContentList = mItemWordListcontents.get(position);
//                不能被查看移除，通知更新，且删除该数据
                if (!itemWordContentList.isSearch()) {
                    mItemWordListcontents.remove(position);
                    notifyItemRemoved(position);
                    notifyItemChanged(0, mItemWordListcontents.size());
//                    删除与当前文件夹ID和单词ID相关联的所有记录
                    LitePal.deleteAll(FolderLinkWord.class, "folderId = ? and wordId = ?", WordFoldDetailActivity.currentFolderId + "",  itemWordContentList.getWordId() + "");
                }
            }
        });
//        设置点击事件播放相关信息，跳转
        holder.textWord.setOnClickListener(new View.OnClickListener() {
            @Override
//            点击单词进行音频播放
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ItemWordListContent itemWordList = mItemWordListcontents.get(position);
                MediaPlayHelper.play(itemWordList.getWordName());
            }
        });
        holder.textMean.setOnClickListener(new View.OnClickListener() {
//            意思当前位置的单词意思进行界面跳转，到单词展实
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ItemWordListContent itemWordList = mItemWordListcontents.get(position);
                WordDetailActivity.wordId = itemWordList.getWordId();
                Intent intent = new Intent();
                intent.setClass(MyApplication.getContext(), WordDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(WordDetailActivity.TYPE_NAME, WordDetailActivity.TYPE_GENERAL);
                MyApplication.getContext().startActivity(intent);
            }
        });
        return holder;
    }
//绑定
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemWordListContent itemWordList = mItemWordListcontents.get(position);
        holder.textMean.setText(itemWordList.getWordMean());
        holder.textWord.setText(itemWordList.getWordName());
        Glide.with(MyApplication.getContext()).load(R.drawable.icon_reduce).into(holder.imgReduce);
    }

    @Override
    public int getItemCount() {
        return  mItemWordListcontents.size();
    }
//静态
    static class ViewHolder extends RecyclerView.ViewHolder {
    View view;
    ImageView imgReduce;
    TextView textWord, textMean;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imgReduce = itemView.findViewById(R.id.img_itemwordfold_list_reduce);
            textWord = itemView.findViewById(R.id.text_itemwordfold_list_word);
            textMean = itemView.findViewById(R.id.text_itemwordfold_list_mean);
        }
    }
}
