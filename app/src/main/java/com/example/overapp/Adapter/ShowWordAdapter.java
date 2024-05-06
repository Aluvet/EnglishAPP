package com.example.overapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.overapp.Activity.WordDetailActivity;
import com.example.overapp.ItemData.ItemShowWord;
import com.example.overapp.R;
import com.example.overapp.Utils.MyApplication;

import com.example.overapp.config.ConfigData;
import com.example.overapp.database.Word;

import java.util.List;

public class ShowWordAdapter extends RecyclerView.Adapter<ShowWordAdapter.ViewHolder> {
    private List<ItemShowWord> mitemShowWordList;
    public ShowWordAdapter(List<ItemShowWord> mItemShowWordList) {
        this.mitemShowWordList = mItemShowWordList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_word, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.imgStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                ItemShowWord itemShowWord = mitemShowWordList.get(position);
                if (itemShowWord.isStar()){
                    itemShowWord.setStar(false);
                    Word word = new Word();
                    word.setToDefault("isCollected");
                    word.updateAll("wordId = ?", itemShowWord.getWordId() + "");
                } else {
                    itemShowWord.setStar(true);
                    Word word = new Word();
                    word.setIsCollected(1);
                    word.updateAll("wordId = ?", itemShowWord.getWordId() + "");
                }
                notifyDataSetChanged();
            }

        });
        viewHolder. layoutWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                ItemShowWord itemShowWord = mitemShowWordList.get(position);
                        WordDetailActivity.wordId = itemShowWord.getWordId();
                        Intent intent = new Intent();
                        intent.setClass(MyApplication.getContext(), WordDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(WordDetailActivity.TYPE_NAME, WordDetailActivity.TYPE_GENERAL);
                        MyApplication.getContext().startActivity(intent);
                    }
                });
                return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
ItemShowWord itemShowWord =mitemShowWordList.get(position);
holder.WordName.setText(itemShowWord.getWord());
holder.WordMean.setText(itemShowWord.getWordMean());
//对收藏设置
        if (itemShowWord.isStar()){
            Glide.with(MyApplication.getContext()).load(R.drawable.icon_star_fill).into(holder.imgStar);
        }else
            Glide.with(MyApplication.getContext()).load(R.drawable.icon_star).into(holder.imgStar);
        if (ConfigData.getIsNight()) {
            holder.itemShow.setBackgroundColor(MyApplication.getContext().getColor(R.color.colorLittleWhiteN));
        }

    }

    @Override
    public int getItemCount() {
        return mitemShowWordList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemShow;
        LinearLayout layoutWords;
        ImageView imgStar;
        TextView WordName, WordMean;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemShow=itemView.findViewById(R.id.layout_show);
            layoutWords=itemShow.findViewById(R.id.layout_wordsrelact);
            imgStar = itemView.findViewById(R.id.img_showword_startocollect);
            WordName=itemView.findViewById(R.id.text_showword_name);
            WordMean=itemView.findViewById(R.id.text_showword_mean);

        }
    }
}
