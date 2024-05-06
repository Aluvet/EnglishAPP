package com.example.overapp.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.overapp.Activity.WordFoldDetailActivity;
import com.example.overapp.ItemData.ItemWordFold;
import com.example.overapp.R;
import com.example.overapp.Utils.MyApplication;

import com.example.overapp.database.WordFolder;

import org.litepal.LitePal;

import java.util.List;
//单词本
/*
adapter大体实现
1.将数据源传递进来
2.onCreateViewHolder会被调用，进而创建子视图view，将其实例化，
3.然后将其进行ViewHolder，将子视图里的控件进行实例化
4.onBindViewHolder进行数据绑定，最其控件进行赋值
给recycle设置adapter 并继承自recycleView*/
public class WordFoldAdapter extends RecyclerView.Adapter<WordFoldAdapter.ViewHolder> {
    private List<ItemWordFold>  mitemWordFoldLIst;
    public WordFoldAdapter (List<ItemWordFold> mitemWordFoldLIst){
        this.mitemWordFoldLIst=mitemWordFoldLIst;    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_fold, parent, false);
//        创建一个新的ViewHolder实例，并将之前创建的视图传递给它
        final  ViewHolder holder =new ViewHolder(view);
//        点击事件
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                点击位置
                int position = holder.getAdapterPosition();
//获得对应列表想的数据保存
                ItemWordFold wordFold =  mitemWordFoldLIst.get(position);
//                如果存在单词跳转的单词展示列表
                if (wordFold.getWordNum() > 0) {
                    Intent intent = new Intent(MyApplication.getContext(), WordFoldDetailActivity.class);
                    WordFoldDetailActivity.currentFolderId = wordFold.getFoldId();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getContext().startActivity(intent);
                } else {
                    Toast.makeText(MyApplication.getContext(), "该单词本下没有内容哦", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        给图片设置点击，删除
        holder.FoldDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                同上，想获得位置，将当前位置的列表获得赋值 ，移除item及数据
                final int position = holder.getAdapterPosition();
                final ItemWordFold wordFolder = mitemWordFoldLIst.get(position);
//                弹出权限狂
                AlertDialog.Builder builder =new AlertDialog.Builder(parent.getContext());
                builder .setTitle("警告").setMessage("确定要删除该单词本吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
/* 移除   hisList.remove(position);
   hisAdapter.notifyItemRemoved(position);
   hisAdapter.notifyItemChanged(0,hisList.size());*/
                                mitemWordFoldLIst.remove(position);
//                                通知已移除更新
                                notifyItemRemoved(position);
//                                通知已经更新，在进行item重新拍列
                                notifyItemChanged(0,mitemWordFoldLIst.size());
//                                在数据库中删除等于item实体类中的fold的id
                                LitePal.deleteAll(WordFolder.class,"id = ?", wordFolder.getFoldId() + "");

                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
        return holder;
    }
//数据绑定复制
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemWordFold itemWordFold = mitemWordFoldLIst.get(position);
        holder.FoldName.setText(itemWordFold.getFoldName());
        holder.FoldRemark.setText(itemWordFold.getFoldRemark());
        holder.FoldWordNum.setText(itemWordFold.getWordNum() + "");
    }
//空间多少
    @Override
    public int getItemCount() {
        return mitemWordFoldLIst.size();
    }
//    静态内部类 对应每个item的数据
    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView FoldDelete;
        TextView FoldName, FoldRemark, FoldWordNum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view =itemView;
            FoldDelete =itemView.findViewById(R.id.img_ash_delete);
            FoldName=itemView.findViewById(R.id.text_fold_name);
            FoldWordNum =itemView.findViewById(R.id.text_fold_Num);
            FoldRemark=itemView.findViewById(R.id.text_fold_remark);
        }
    }


}
