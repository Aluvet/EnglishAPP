package com.example.overapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.Activity.WordDetailActivity;
import com.example.overapp.ItemData.ItemSearch;
import com.example.overapp.R;
import com.example.overapp.Utils.MyApplication;


import java.util.List;

/*
adapter大体实现
1.将数据源传递进来
2.onCreateViewHolder会被调用，进而创建子视图view，将其实例化，
3.然后将其进行ViewHolder，将子视图里的控件进行实例化
4.onBindViewHolder进行数据绑定，最其控件进行赋值
给recycle设置adapter 并继承自recycleView*/
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
//构造数据源
    private List<ItemSearch>  mItemSearchList;
//构造是需要传值给list
    public SearchAdapter(List<ItemSearch> mItemSearchLists) {
        this.mItemSearchList = mItemSearchLists;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_words, parent, false);
//       创建一个新的ViewHolder实例，并将之前创建的视图传递给它
        final ViewHolder viewHolder =new ViewHolder(view);
//        空间点击事件，跳转
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                获取当前使徒的位置
                int position = viewHolder.getAdapterPosition();
//                处于当前位置的item对象
                ItemSearch itemSearch = mItemSearchList.get(position);
//                当前位置的单词的id赋值给单词相关展示的id进行跳转展示
                WordDetailActivity.wordId = itemSearch.getWordId();
                Intent intent = new Intent(MyApplication.getContext(), WordDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(WordDetailActivity.TYPE_NAME, WordDetailActivity.TYPE_GENERAL);
                MyApplication.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }
//数据绑定赋值
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemSearch itemSearch = mItemSearchList.get(position);
        holder.textWord.setText(itemSearch.getWordName());
        holder.textSound.setText(itemSearch.getWordSound());
        holder.textMean.setText(itemSearch.getWordMean());

    }
//返回列表数据，看一共有几个item
    @Override
    public int getItemCount() {
        return mItemSearchList.size() ;
    }
    //    静态内部类 对应每个item的数据
    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView textWord, textMean, textSound;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view =itemView;
            textWord = itemView.findViewById(R.id.text_search_name);
            textMean = itemView.findViewById(R.id.text_search_means);
            textSound = itemView.findViewById(R.id.text_search_sound);
        }
    }
}
