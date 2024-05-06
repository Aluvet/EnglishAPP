package com.example.overapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.overapp.Activity.ChangeLearnActivity;
import com.example.overapp.ItemData.ItemBook;
import com.example.overapp.R;
import com.example.overapp.Utils.MyApplication;
import com.example.overapp.config.ConfigData;
import com.example.overapp.database.UserConfig;

import org.litepal.LitePal;

import java.util.List;

/*
adapter大体实现
1.将数据源传递进来
2.onCreateViewHolder会被调用，进而创建子视图view，将其实例化，
3.然后将其进行ViewHolder，将子视图里的控件进行实例化
4.onBindViewHolder进行数据绑定，最其控件进行赋值
给recycle设置adapter 并继承自recycleView*/
public class WordBookAdapter extends RecyclerView.Adapter<WordBookAdapter.ViewHolder>{
//数据源
    private List<ItemBook> itemBookList;
//    itemadapter构造方法
//构造时传值给list数据
    public WordBookAdapter(List<ItemBook> itemBookList){
        this.itemBookList =itemBookList;
    }

//    静态内部类 对应每个item的数据
    static  class ViewHolder extends RecyclerView.ViewHolder{
View itemBookview;
TextView BookName, WordsNum ,BookFrom;
ImageView BookImg;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        itemBookview=itemView;
        BookName=itemView.findViewById(R.id.text_book_name);
        WordsNum =itemView.findViewById(R.id.text_book_word_num);
        BookFrom =itemView.findViewById(R.id.text_bookfrom);
        BookImg = itemView.findViewById(R.id.img_book);

    }
}

    //用于创建ViewHolder实例,并把加载的布局传入到ViewHolder的构造函数去
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        从parent上返回上下文，实例化
        View view = LayoutInflater.from(parent.getContext())
//                传参及item
                .inflate(R.layout.item_book_list,parent,false);
//        创建一个新的ViewHolder实例，并将之前创建的视图传递给它
       final ViewHolder holder =new ViewHolder(view);
//       对试图此设置点击事件
        holder.itemBookview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                获取点击视图的位置
                int position =holder.getAdapterPosition();
//                通过得到点击使徒的位置获取对应的item对象
                final ItemBook itemBook = itemBookList.get(position);
//                通过litepal 查询当前登录用户的id与用户相关数据的数据库
                List<UserConfig> userConfigs = LitePal.where("userId= ?", ConfigData.getLoggedNum()+"").find(UserConfig.class);
//                判断登录用户配置的书籍id是否与选中的一样，且，用户已经设置了学习相关数据
                if (userConfigs.get(0).getCurrentBookId()==itemBook.getBookId() && userConfigs.get(0).getWordNeedReciteNum()!=0){
//                   弹出对话框
                    Toast.makeText(MyApplication.getContext(),"当前选的是这本书",Toast.LENGTH_SHORT).show();
                }
//                反之
                else
                {
//                   创建新的config对象 更新对应数据
                    UserConfig userConfig =new UserConfig();
                    userConfig.setCurrentBookId(itemBook.getBookId());
                    userConfig.updateAll("userId=?",ConfigData.getLoggedNum()+"");
//                    传值，并启动新的Activity（ChangeLearnActivity），同时传递标志指示数据没有更新
                    Intent intent = new Intent(MyApplication.getContext(), ChangeLearnActivity.class);
                    intent.putExtra(ConfigData.UPDATE_NAME, ConfigData.notUpdate);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getContext().startActivity(intent);

                }
            }
        });

return holder;
    }
//    绑定ViewHolder时的回调函数
//传入 自定义内部类的 holder 和 int position
//    position
//可以理解为list中item的下标，就像数组一样，每个item都有自己的标识
//那数据怎么来，Adapter初始
//新增构造方法，记得要传context，顺便加上成员变量

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        传入对应实体类,get当前实例
        ItemBook itemBook = itemBookList.get(position);
        holder.BookName.setText(itemBook.getBookName());
        holder.BookFrom.setText(itemBook.getBookFrom());
//        将数字转化为字符串
        holder.WordsNum.setText(itemBook.getWordNum()+"");
//        利用glide加载对应的图片,获取context
        Glide.with(MyApplication.getContext()).load(itemBook.getImg()).into(holder.BookImg);

    }
//没有传参，需要返回一个int
//这个方法是控制创建item的条数，返回的就是条数
//我这里直接给其中一个ArrayList的大小即可
    @Override
    public int getItemCount() {
        return itemBookList.size();
    }



}
