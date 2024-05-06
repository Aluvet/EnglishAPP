package com.example.overapp.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.overapp.R;
import com.example.overapp.database.WordFolder;
//创建单词本
public class CreateFoldActivity extends BaseAcyivity {
private RelativeLayout wordFold_Add;
private EditText creatFoldName , creatFoldRemark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_fold);
//        方法初始化
initCreatFold();
//添加按钮创建成功
 wordFold_Add.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
//         判断框中是否存在
         if (!TextUtils.isEmpty(creatFoldName.getText().toString().trim())) {
//             不为空则创建单词本实例，并对其设置信息
             WordFolder wordFolder = new WordFolder();
//             得到编辑框中的文字
             wordFolder.setName(creatFoldName.getText().toString().trim());
//下面设计备注
             if (!TextUtils.isEmpty(creatFoldRemark.getText().toString().trim()))
                 wordFolder.setRemark(creatFoldRemark.getText().toString().trim());
//                保存
             wordFolder.save();
//                 可返回
             onBackPressed();
             Toast.makeText(CreateFoldActivity.this, "单词本创建成功", Toast.LENGTH_SHORT).show();

         }else{
                 Toast.makeText(CreateFoldActivity.this,"单词本名称不能为空",Toast.LENGTH_SHORT).show();
         }
     }
 });
//点击事件添加逻辑
    }
    public void initCreatFold(){
        creatFoldName=findViewById(R.id.edit_creatfold_name);
        creatFoldRemark=findViewById(R.id.edit_creatfold_remark);
        wordFold_Add =findViewById(R.id.layout_createFold_add);
    }
}