package com.example.overapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.overapp.Fragment.WordFragment;
import com.example.overapp.R;
import com.example.overapp.Utils.ActivityCollector;
import com.example.overapp.Utils.TimeController;
import com.example.overapp.config.ConfigData;
import com.example.overapp.config.ConstantData;
import com.example.overapp.database.UserConfig;
import com.example.overapp.database.Word;

import org.litepal.LitePal;

import java.util.List;
//更新数据
public class PlanActivity extends BaseAcyivity {

//  两个布局卡片1.书本。2.数据
    private RelativeLayout cardBookChange,cardDataChange;
    private TextView planBookName,planBookNum,planBookDailyLearn,planLearnOver;
    private ImageView imgPlanBook;
    private EditText speedChange,matchChange;
    private  TextView  planDataIndex;
    private  String[] planChangeWay={"词本切换","相关数据修改","单词书重置"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
//        初始化修改计划
        initChangePlan();

//查询当前用户的配置信息,配置当前用户使用的书信息,学习信息
        List<UserConfig> userConfigs = LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserConfig.class);
        int bookId = userConfigs.get(0).getCurrentBookId();
        final int wordTotalNum = ConstantData.BookWordNum(bookId);
        int dailyWordNum = userConfigs.get(0).getWordNeedReciteNum();

//        设置词书计划的信息
        Glide.with(this).load(ConstantData.BookPic(bookId)).into(imgPlanBook);
        planBookName.setText(ConstantData.WordBookName(bookId));
        planBookNum.setText("词汇单词总数：" + wordTotalNum);
        planBookDailyLearn.setText("每日须学单词："+dailyWordNum);

//        设置天数,总单词/每日学习+1
        int learnOverDay = (wordTotalNum/dailyWordNum)+1;
//        今天+天数得到在什么时候学完
        planLearnOver.setText("本书将在"+ TimeController.getDayAgoOrAfterString(learnOverDay)+"学完单词");

//        设置数据修改模块
         planDataIndex.setText("单词数量需要在5到"+wordTotalNum+"区间内");
        matchChange.setText(ConfigData.getMatchPlanNum() + "");
        speedChange.setText(ConfigData.getSpeedPlanNum() + "");


//        设置完成后，对修改部分设置相关点击事件
        cardBookChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                对话框
                final AlertDialog.Builder builder =new AlertDialog.Builder(PlanActivity.this);
                builder.setTitle("请选择方式").setSingleChoiceItems(planChangeWay, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int which) {
//                        延迟取消对话框
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialogInterface.dismiss();
                                WordFragment.prepareData=0;
                                switch (which){
//                                    此本切换时，进入此本交换界面
                                    case 0:
                                        ActivityCollector.startOtherActivity(PlanActivity.this, ChooseWordBookActivity.class);
                                        ConfigData.ReChooseBook = true;
                                        break;
//                                        相关此本数据修改
                                    case 1:
                                        Intent intent = new Intent(PlanActivity.this, ChangeLearnActivity.class);
                                        intent.putExtra(ConfigData.UPDATE_NAME, ConfigData.isUpdate);
                                        startActivity(intent);
                                        break;
//                                        充值用学习数据
                                    case 2:
                                        dialogInterface.dismiss();
//                                        弹出提示框
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(PlanActivity.this);
                                        builder1.setTitle("提示")
                                                .setMessage("此操作会重置此书的所有学习配置信息（当你学完这本书后，可以选择）")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Word word = new Word();
//                                                        充值用户信息
                                                        word.setToDefault("isNeedLearned");
                                                        word.setToDefault("needLearnDate");
                                                        word.setToDefault("needReviewDate");
                                                        word.setToDefault("isLearned");
                                                        word.setToDefault("examNum");
                                                        word.setToDefault("examRightNum");
                                                        word.setToDefault("lastMasterTime");
                                                        word.setToDefault("lastReviewTime");
                                                        word.setToDefault("masterDegree");
                                                        word.setToDefault("deepMasterTimes");
                                                        UserConfig userConfig = new UserConfig();
                                                        userConfig.setToDefault("lastStartTime");
//                                                        充值完后全部更新
                                                        userConfig.updateAll();
                                                        word.updateAll();
                                                        Toast.makeText(PlanActivity.this, "词本重置成功", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setNegativeButton("取消", null)
                                                .show();
                                        break;

                                }
                            }
                        },200);
                    }
                }).show();
            }
        });
//        学习数据更新
        cardDataChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               将编辑框中获得相关数据转化为数字
                int speedNum = Integer.parseInt(speedChange.getText().toString());
                int matchNum = Integer.parseInt(matchChange.getText().toString());
//                得到数字后进行判断
                if ((speedNum>=5 && speedNum<=wordTotalNum)&&(matchNum>=2 && matchNum<=wordTotalNum) ){
//                    将设置的信息存入shareperent中
                    ConfigData.setSpeedPlanNum(speedNum);
                    ConfigData.setMatchPlanNum(matchNum);
                    Toast.makeText(PlanActivity.this, "数据修改成功", Toast.LENGTH_SHORT).show();
                    matchChange.setText(ConfigData.getMatchPlanNum() + "");
                    speedChange.setText(ConfigData.getSpeedPlanNum() + "");
                }
//                否则提示用户输入有效信息
                else{
                    Toast.makeText(PlanActivity.this,"请输入有效信息",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public  void initChangePlan(){
        cardBookChange=findViewById(R.id.layout_planbook_change);
        imgPlanBook=findViewById(R.id.img_planbook_img);
        planBookName=findViewById(R.id.text_planbook_name);
        planBookNum=findViewById(R.id.text_planbook_num);
        planBookDailyLearn=findViewById(R.id.text_planbook_daily);
        planLearnOver=findViewById(R.id.text_planbook_learnOver);
        cardDataChange=findViewById(R.id.layout_plandata_change);
        speedChange=findViewById(R.id.edit_plandata_speed);
        matchChange=findViewById(R.id.edit_plandata_match);
        planDataIndex=findViewById(R.id.text_plandata_idex);

    }
}