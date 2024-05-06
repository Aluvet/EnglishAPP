package com.example.overapp.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.overapp.R;
import com.example.overapp.Utils.MyApplication;
import com.example.overapp.config.ConstantData;
import com.example.overapp.database.Interpretation;
import com.example.overapp.database.Word;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;
//单词速记，使用音频
public class SpeedActivity extends BaseAcyivity implements View.OnClickListener {
    // 单词列表
    public static ArrayList<Word> wordList = new ArrayList<>();
    private TextView playProgress;
//展示部分
    private LinearLayout WordPlayALL;
    private AutofitTextView WordPlay;
    private TextView wordPhonePlay, WordMeanPlay;


    private RelativeLayout cardPause, cardHome;
    private TextView textPause;

    private MediaPlayer mediaPlayer;

    private int nowWordPlay = 0;

    private boolean isPause = false;
    private ShowWordActivity showWordActivity=new ShowWordActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);
        windowExplode();
        //        初始化相关控件
        initSpeed();

//展示单词数据
        showWordData();
        playWordDate();
    }
    private  void initSpeed(){

        playProgress=findViewById(R.id.text_speed_top);
        //        绑定展示数据
        WordPlayALL=findViewById(R.id.layout_speed_play);
        WordPlay=findViewById(R.id.text_speedplay_word);
        wordPhonePlay=findViewById(R.id.text_speedplay_phone);
        WordMeanPlay=findViewById(R.id.text_speedplay_mean);
//        绑定下部工能数据
        cardHome=findViewById(R.id.layout_speed_home);
        cardPause=findViewById(R.id.layout_speed_pause);
        textPause=findViewById(R.id.text_speed_pause);
//        对空间设置点击事件
        cardHome.setOnClickListener(this);
        cardPause.setOnClickListener(this);


    }
    private void showWordData(){
//        展示相关数据
        playProgress.setText((nowWordPlay+1)+"/"+wordList.size());
//        根据当前所展示的单词数据在列表中获取单词展示
        WordPlay.setText(wordList.get(nowWordPlay).getWord());
//        再根据得到的单词的id查找翻译
        List<Interpretation> interpretations = LitePal.where("wordId=?",wordList.get(nowWordPlay).getWordId()+"").find(Interpretation.class);
//       stringbuilder拼接单词
        StringBuilder stringBuilder=new StringBuilder();
        for (int i=0; i<interpretations.size();i++){
//            如果遍历单词最后一个则直接拼接
            if (i==(interpretations.size()-1)){
                stringBuilder.append(interpretations.get(i).getWordType() + ". " + interpretations.get(i).getCHSMeaning());
//                不是最后一个则拼接后直接换换行
            }else stringBuilder.append(interpretations.get(i).getWordType()+"."+interpretations.get(i).getCHSMeaning()+"\n");
        }
//        将意思转化为字符串展示
        WordMeanPlay.setText(stringBuilder.toString());
//        设置完单词释义等数据，展示英式发音
        wordPhonePlay.setText(wordList.get(nowWordPlay).getUkPhone());

    }
//      点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            home返回上衣活动
            case R.id.layout_speed_home:
                 onBackPressed();
                 break;
//判断是否是暂停状态，
            case R.id.layout_speed_pause:
//若点击时不是暂停状态，则设置为暂停
                if (!isPause){
                      isPause=true;
                    textPause.setText("继续");
                }else{
//                    同上
                    isPause=false;
//                    在播放时进行相关数据的更新并播放音频
                    textPause.setText("暂停");
                    playWordDate();
                }
                break;
        }

    }
//    展示音频
    private void playWordDate(){
//此时在speed中需要播放单词音频
//        判断是否存在yinpin
        if (mediaPlayer==null){
            mediaPlayer =new MediaPlayer();

        }
//        如果存在，则清空后在重新创建
        else{
            mediaPlayer.release();
        mediaPlayer=null;
        mediaPlayer =new MediaPlayer();
        }
//        抛出trycatch
        try {
//            设置音频源，准备播放
            mediaPlayer.setDataSource(ConstantData.YOU_DAO_VOICE_UK+wordList.get(nowWordPlay).getWord());
//            异步播放
            mediaPlayer.prepareAsync();
//            监听器，音频文件准备好后，调用
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
//                    不是暂停，播放音频
                    if (!isPause){
                        mediaPlayer.start();
                        showWordData();
                    }
                }
            });
//            监听器，播放完毕后调用，当播放单词不是最后一个
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
//                    不是最后一个，一定时间后播放下一个
                    if (nowWordPlay!=(wordList.size()-1)){
                        if (!isPause){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ++nowWordPlay;
                                   playWordDate();
                                }
                            }, 1000);
                        }
                    }
//                    如果单词已经结束，弹出信息
                    else {
                        Toast.makeText(SpeedActivity.this,"单词已播放完毕",Toast.LENGTH_SHORT).show();
//                        跳转界面将播放的单词全部进行展示
                        Intent intent=new Intent();
                        intent.setClass(SpeedActivity.this, ShowWordActivity.class);
//                        在活动跳转时加入Intent.FLAG_ACTIVITY_NEW_TASK
//                        实现单例活动，确保不会回到音频播放界面
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(showWordActivity.SHOW_Word_TYPE, showWordActivity.SPEEDMode);
                        startActivity(intent);
                    }
                }
            });
//            播放出错
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(MyApplication.getContext(), "出现错误，请检查联网设置", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    return true;
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
//返回
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//         返回时将音频停止
        isPause=true;
//        并将现存的miadia清楚
        if (mediaPlayer!=null){
//            调用release，清空
            mediaPlayer.release();
//            并meidia置为空
            mediaPlayer=null;
        }
    }
}