package com.example.overapp.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.Activity.ShowWordActivity;
import com.example.overapp.ItemData.IdAndePositionAnalyse;
import com.example.overapp.ItemData.ItemMatchWord;
import com.example.overapp.R;

import com.example.overapp.Utils.MyApplication;
import com.example.overapp.config.ConfigData;

import java.util.ArrayList;
import java.util.List;
/*
adapter大体实现
1.将数据源传递进来
2.onCreateViewHolder会被调用，进而创建子视图view，将其实例化，
3.然后将其进行ViewHolder，将子视图里的控件进行实例化
4.onBindViewHolder进行数据绑定，最其控件进行赋值
给recycle设置adapter 并继承自recycleView*/
public class MatchWordAdapter extends RecyclerView.Adapter<MatchWordAdapter.ViewHolder>{
    private List<ItemMatchWord> itemMatchWordList =new ArrayList<>();
    private  List<IdAndePositionAnalyse> idAndePositionAnalyseList =new ArrayList<>();
    private ShowWordActivity showWordActivity =new ShowWordActivity();
    private static final String TAG = "MatchWordAdapter";
    public MatchWordAdapter(List<ItemMatchWord> itemMatchWordList) {
        this.itemMatchWordList = itemMatchWordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_word, parent, false);
        final ViewHolder holder = new ViewHolder(view);
//        对实例设置点击事件
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                同其他，先获取位置，在处理点击逻辑
                int position = holder.getAdapterPosition();
               ItemMatchWord itemMatcword = itemMatchWordList.get(position);
                // 开始为空
                if (idAndePositionAnalyseList.isEmpty()) {
//                    将单词和位置加入，设置为可选择，并通知更新
                    idAndePositionAnalyseList.add(new IdAndePositionAnalyse(itemMatcword.getId(), position));
                    itemMatcword.setChosen(true);
//                    通知recycleView的adapter数据更改，出发ui
                    notifyDataSetChanged();
                }
//               当 idAndePositionAnalyseList 有一个元素时，检查这个是否与当前点击的单词相同但位置不同，当前点击的单词和位置添加到列表中
                else if (idAndePositionAnalyseList.size() == 1) {
                    if (idAndePositionAnalyseList.get(0).getWordId() == itemMatcword.getId() && idAndePositionAnalyseList.get(0).getPosition() != position) {
                        idAndePositionAnalyseList.add(new IdAndePositionAnalyse(itemMatcword.getId(), position));
                        List<ItemMatchWord> itemMatches = new ArrayList<>();
                        itemMatches.add(itemMatchWordList.get(idAndePositionAnalyseList.get(0).getPosition()));
                        itemMatches.add(itemMatchWordList.get(position));
//                         itemMatchWordList 中移除两个相同的单词
                        itemMatchWordList.removeAll(itemMatches);
//                        通知adapter ，当前想已移除，大小已更改
                        notifyItemRemoved(position);
                        notifyItemChanged(position,itemMatchWordList.size());
//                        如果当前元素为第一个位置，更新，其他的上去
                        if (idAndePositionAnalyseList.get(0).getPosition() == 0) {
                            notifyItemRemoved(0);
                            notifyItemChanged(0,itemMatchWordList.size());
                        } else {
//                            否则移除并更新钱一个位置
                            notifyItemRemoved(idAndePositionAnalyseList.get(0).getPosition() - 1);
                            notifyItemChanged(idAndePositionAnalyseList.get(0).getPosition() - 1, itemMatchWordList.size());
                        }
//                        通知第一个位置已更新
                        notifyItemChanged(0, itemMatchWordList.size());
                        idAndePositionAnalyseList.clear();
//                        列表全部清空，则该功能单词全部学完，跳转到展示列表
                        if (itemMatchWordList.isEmpty()) {
                            Toast.makeText(MyApplication.getContext(), "当前单词匹配完成", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(MyApplication.getContext(), ShowWordActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            传递一个额外的数据，指定单词显示类型模式匹配
                            intent.putExtra(showWordActivity.SHOW_Word_TYPE, showWordActivity.MATCHMode);
                            MyApplication.getContext().startActivity(intent);
                        }
                    } else {
//                        没弄完，不为空，直接重置让用户重新选择
//                        获取idAndePositionAnalyseList中第一个元素的位置所对应的ItemMatchWord对象，并将其设置为未选择状态（setChosen(false)）
                        ItemMatchWord itemMatchword1 = itemMatchWordList.get(idAndePositionAnalyseList.get(0).getPosition());
                        itemMatchword1.setChosen(false);
                        ItemMatchWord itemMatch2 = itemMatchWordList.get(position);
                        itemMatch2.setChosen(false);
                        notifyDataSetChanged();
                        idAndePositionAnalyseList.clear();
                        Toast.makeText(MyApplication.getContext(), "点错了哦", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        return holder;
    }
//数据绑定
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
ItemMatchWord itemMatchWord =itemMatchWordList.get(position);
//给实力设置i相关信息
holder.text.setText(itemMatchWord.getWordString());
//处于夜间模式，设置一个颜色，不是一个颜色，下同
if (itemMatchWord.isChosen()){
    if (ConfigData.getIsNight()) {
        holder.card.setCardBackgroundColor(MyApplication.getContext().getColor(R.color.colorLightBlueN));
        holder.text.setTextColor(MyApplication.getContext().getColor(R.color.colorFontWhite));
        Log.d(TAG, "sss");
    } else {
        holder.card.setCardBackgroundColor(MyApplication.getContext().getColor(R.color.colorLightBlue));
        holder.text.setTextColor(MyApplication.getContext().getColor(R.color.colorBgWhite));
    }
} else {
    if (ConfigData.getIsNight()) {
        holder.card.setCardBackgroundColor(MyApplication.getContext().getColor(R.color.colorLightWhiteN));
        holder.text.setTextColor(MyApplication.getContext().getColor(R.color.colorLightBlackN));
    } else {
        holder.card.setCardBackgroundColor(MyApplication.getContext().getColor(R.color.colorLightWhite));
        holder.text.setTextColor(MyApplication.getContext().getColor(R.color.colorLightBlack));
    }
}
    }
//看需要多少
    @Override
    public int getItemCount() {
        return itemMatchWordList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        CardView card;
        TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          view=itemView;
          card=itemView.findViewById(R.id.match_over);
          text=itemView.findViewById(R.id.text_match_word);        }
    }
}
