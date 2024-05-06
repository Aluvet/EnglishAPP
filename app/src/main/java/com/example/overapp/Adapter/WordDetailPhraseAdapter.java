package com.example.overapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.ItemData.ItemPhrase;
import com.example.overapp.R;

import java.util.List;

public class WordDetailPhraseAdapter extends RecyclerView.Adapter<WordDetailPhraseAdapter.ViewHolder> {

private  List<ItemPhrase> mItemPhraseList;
    public WordDetailPhraseAdapter(List<ItemPhrase> mItemPhraseList) {
        this.mItemPhraseList = mItemPhraseList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_worddetail_phrase, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
ItemPhrase itemPhrase =mItemPhraseList.get(position);
        holder.Cn.setText(itemPhrase.getCn());
        holder.En.setText(itemPhrase.getEn());
    }

    @Override
    public int getItemCount() {
        return mItemPhraseList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView En, Cn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            En=itemView.findViewById(R.id.text_item_worddetail_phrase_en);
            Cn=itemView.findViewById(R.id.text_item_worddetail_phrase_cn);
        }
    }
}
