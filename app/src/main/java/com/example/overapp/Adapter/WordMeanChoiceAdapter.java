package com.example.overapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.overapp.Interface.OnItemClickListener;
import com.example.overapp.ItemData.ItemWordMeanChoice;
import com.example.overapp.R;
import com.example.overapp.Utils.MyApplication;
import com.example.overapp.config.ConfigData;

import java.util.List;

public class WordMeanChoiceAdapter extends RecyclerView.Adapter<WordMeanChoiceAdapter.ViewHolder> implements View.OnClickListener{
    private List<ItemWordMeanChoice> itemWordMeanChoiceList;
//    在进行选择是，判断是否为第一次点击
    public static boolean isFirstClick=true;
    private RecyclerView recyclerView;
    // 声明单击接口
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
//    review附加到adapter上

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    public WordMeanChoiceAdapter(List<ItemWordMeanChoice> itemWordMeanChoiceList)
    {
        this.itemWordMeanChoiceList=itemWordMeanChoiceList;
    }
    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(recyclerView, view, position, itemWordMeanChoiceList.get(position));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wordmean_choice, parent, false);
        view.setOnClickListener(this);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
ItemWordMeanChoice itemWordMeanChoice=itemWordMeanChoiceList.get(position);
        holder.WordMean.setText(itemWordMeanChoice.getWordMean());
        holder.imgChoice.setVisibility(View.GONE);
        if (ConfigData.getIsNight())
            holder.WordMean.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorLightBlack));
        else
            holder.WordMean.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorLightBlack));
        if (itemWordMeanChoice.isRight() == ItemWordMeanChoice.WRONG) {
            // 答错了
            if (ConfigData.getIsNight())
                holder.cardViewWordMean.setCardBackgroundColor(MyApplication.getContext().getColor(R.color.colorLittleRedN));
            else
                holder.cardViewWordMean.setCardBackgroundColor(MyApplication.getContext().getColor(R.color.colorLittleRed));
            holder.imgChoice.setVisibility(View.VISIBLE);
            Glide.with(MyApplication.getContext()).load(R.drawable.icon_wrong).into(holder.imgChoice);
            if (ConfigData.getIsNight())
                holder.WordMean.setTextColor(MyApplication.getContext().getColor(R.color.colorLightRedN));
            else
                holder.WordMean.setTextColor(MyApplication.getContext().getColor(R.color.colorLightRed));
        } else if (itemWordMeanChoice.isRight() == ItemWordMeanChoice.RIGHT) {
            // 说明答对了
            if (ConfigData.getIsNight())
                holder.cardViewWordMean.setCardBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.colorLittleBlueN));
            else
                holder.cardViewWordMean.setCardBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.colorLittleBlue));
            holder.imgChoice.setVisibility(View.VISIBLE);
            Glide.with(MyApplication.getContext()).load(R.drawable.icon_select_blue).into(holder.imgChoice);
            if (ConfigData.getIsNight())
                holder.WordMean.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorLightBlueN));
            else
                holder.WordMean.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorLightBlue));
        } else if (itemWordMeanChoice.isRight() == ItemWordMeanChoice.NOTSTART) {
            if (ConfigData.getIsNight())
                holder.cardViewWordMean.setCardBackgroundColor(MyApplication.getContext().getColor(R.color.colorBgWhiteNight));
            else
                holder.cardViewWordMean.setCardBackgroundColor(MyApplication.getContext().getColor(R.color.colorBgWhite));
            holder.imgChoice.setVisibility(View.GONE);
            if (ConfigData.getIsNight())
                holder.WordMean.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorLightBlackN));
            else
                holder.WordMean.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorLightBlack));
        }
    }

    @Override
    public int getItemCount() {
        return itemWordMeanChoiceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View wordMeanChoiceView;
        CardView cardViewWordMean;
        ImageView imgChoice;
        TextView WordMean;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wordMeanChoiceView = itemView;
            cardViewWordMean = itemView.findViewById(R.id.item_card_word_choice);
            imgChoice = itemView.findViewById(R.id.item_img_word_choice);
            WordMean = itemView.findViewById(R.id.item_text_word_means);
        }
    }
}
