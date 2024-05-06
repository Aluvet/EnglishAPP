package com.example.overapp.Interface;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.ItemData.ItemWordMeanChoice;

public interface OnItemClickListener {
    void onItemClick(RecyclerView parent, View view, int position, ItemWordMeanChoice itemWordMeanChoice);
}
