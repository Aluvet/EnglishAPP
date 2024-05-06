package com.example.overapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.ItemData.ItemSentence;
import com.example.overapp.R;
import com.example.overapp.Utils.MediaPlayHelper;

import java.util.List;

public class WordDetailSentenceAdapter extends RecyclerView.Adapter<WordDetailSentenceAdapter.ViewHolder> {
    private List<ItemSentence> mItemSentenceList;
    public WordDetailSentenceAdapter(List<ItemSentence> mItemSentenceList) {
        this.mItemSentenceList = mItemSentenceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_worddetail_sentence, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.Voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final ItemSentence itemSentence = mItemSentenceList.get(position);
                // 开新线程播放播放
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaPlayHelper.play(itemSentence.getEn());
                    }
                }).start();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemSentence itemSentence = mItemSentenceList.get(position);
        holder.Cn.setText(itemSentence.getCn());
        holder.En.setText(itemSentence.getEn());
    }

    @Override
    public int getItemCount() {
        return mItemSentenceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Voice;
        TextView En, Cn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Voice = itemView.findViewById(R.id.img_worddetail_voicesn);
            En = itemView.findViewById(R.id.text_worddetail_englishsn);
            Cn = itemView.findViewById(R.id.text_worddetail_chinesesn);
        }
    }
}
