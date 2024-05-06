package com.example.overapp.Adapter;

import android.content.Intent;
import android.util.Log;
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
import com.example.overapp.ItemData.ItemWordMeanChoice;
import com.example.overapp.R;
import com.example.overapp.Utils.MediaPlayHelper;
import com.example.overapp.Utils.MyApplication;

import com.example.overapp.config.ConfigData;
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
public class WordListContentAdapter extends RecyclerView.Adapter<WordListContentAdapter.ViewHolder> {
    private List<ItemWordListContent> mItemWordListContents;

    public WordListContentAdapter(List<ItemWordListContent> mItemWordListContents) {
        this.mItemWordListContents = mItemWordListContents;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wordlist_content, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
//        查看点击事件,同其他adapte,获取位置,进行操作
viewHolder.Search.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        int position=viewHolder.getAdapterPosition();
        ItemWordListContent itemWordListContent = mItemWordListContents.get(position);
//        可查看
        if (itemWordListContent.isSearch()) {
            Log.d("WordListContentAdapter","可查看");
//            如果出于可查看状态   ,获取列表中的单词id,跳转带展示当前单词
            WordDetailActivity.wordId = itemWordListContent.getWordId();
            Intent intent = new Intent();
            intent.setClass(MyApplication.getContext(), WordDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(WordDetailActivity.TYPE_NAME, WordDetailActivity.TYPE_GENERAL);
            MyApplication.getContext().startActivity(intent);
        }
//        不可查看,直接移除当前项
        else {
            Log.d("WordListContentAdapter","移除单词");
//            否则将单词移除
            mItemWordListContents.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(0, mItemWordListContents.size());
            LitePal.deleteAll(FolderLinkWord.class, "folderId = ? and wordId = ?", WordFoldDetailActivity.currentFolderId + "", itemWordListContent.getWordId() + "");
        } } });
//点击遮挡的释义,更换背景显示单词,防其他
        viewHolder.WordsMean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                ItemWordListContent itemWordListContent = mItemWordListContents.get(position);
//                当点击单词意思是刷新ui将单词意思显示
                if (itemWordListContent.isClick()) {
                    itemWordListContent.setClick(false);


                } else
                    itemWordListContent.setClick(true);
                notifyDataSetChanged();
            }
});
//        点击英语单词时调用音频发生
        viewHolder.WordsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                ItemWordListContent itemWordListContent =mItemWordListContents.get(position);
//                play方法进行发音
                MediaPlayHelper.play(itemWordListContent.getWordName());
            }
        });
        return viewHolder;
    }
//绑定数据
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
ItemWordListContent itemWordListContent=mItemWordListContents.get(position);
//设置单词名单词释义等
holder.WordsName.setText(itemWordListContent.getWordName());
holder.WordsMean.setText(itemWordListContent.getWordMean());
//先判断item是都被点击过,在判断当前是否是夜间更新背景颜色
        if (itemWordListContent.isClick())
            if (ConfigData.getIsNight())
                holder.WordsMean.setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.colorBgWhiteNight));
            else
                holder.WordsMean.setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.colorBgWhite));
        else
            holder.WordsMean.setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.colorGrey));
        if (itemWordListContent.isSearch()) {
//            根据是否可查看,加载不同图片
            Glide.with(MyApplication.getContext()).load(R.drawable.icon_search).into(holder.Search);
        } else {
            Glide.with(MyApplication.getContext()).load(R.drawable.icon_reduce).into(holder.Search);
        }
    }

    @Override
    public int getItemCount() {
        return mItemWordListContents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView Search;
        TextView WordsName, WordsMean;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            Search=itemView.findViewById(R.id.img_itls_search);
            WordsName=itemView.findViewById(R.id.text_itls_word);
            WordsMean=itemView.findViewById(R.id.text_itls_mean);
        }
    }
}
