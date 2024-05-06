package com.example.overapp.Fragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import com.example.overapp.Activity.BaseAcyivity;
import com.example.overapp.Activity.LoadActivity;
import com.example.overapp.Activity.MainActivity;
import com.example.overapp.Activity.SearchActivity;
import com.example.overapp.Activity.WordDetailActivity;
import com.example.overapp.Activity.WordFoldActivity;
import com.example.overapp.R;

import com.example.overapp.Utils.NumberControl;

import com.example.overapp.config.ConfigData;
import com.example.overapp.config.ConstantData;
import com.example.overapp.database.Interpretation;
import com.example.overapp.database.UserConfig;
import com.example.overapp.database.UserData;
import com.example.overapp.database.Word;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;

public class WordFragment extends Fragment implements View.OnClickListener {
    private CardView card_start,card_search;
    private ImageView img_refresh;
    private TextView start , textWord,textMean,textWordNum,text_book;
    private RelativeLayout relativeLayout;
    private int currentBookId;
    private int currentRandomId;
public static int prepareData =0;
    private boolean isOnClick = true;

//    fragment必须创建和返回Fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        创建视图对象
        View view = inflater.inflate(R.layout.fragment_word_fragment, container, false);
        return view;
    }
//Fragment的onActivityCreated方法 ，初始化fragment需要的资源
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//初始化布局控件
        initFgWord();

        Log.d("WordFragment", "onActivityCreated: ");
//判断是否需要刷新数据
        if (MainActivity.FragmentRefresh) {
            prepareData = 0;
//            开启新线程准备数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BaseAcyivity.prepareDailyData();
                }
            }).start();
        }

    }

   private  void initFgWord(){
//        Fragment中有一个getActivity()的方法。返回与Fragment关联的Activity对象（通过该对象可以查找activity中的控件们（findViewById()））。
//        当fragment生命周期结束并销毁时，getActivity()返回的会是null。onAttach和onDetach之间的其他生命周期方法都可以调用getActivity方法

       card_start=getActivity().findViewById(R.id.card_start);
       card_search=getActivity().findViewById(R.id.card_search);
       img_refresh=getActivity().findViewById(R.id.img_refresh);

       start =getActivity().findViewById(R.id.text_main_start);
       textWord=getActivity().findViewById(R.id.refresh_main_show_word);
       textMean=getActivity().findViewById(R.id.refresh_show_word_mean);
       textWordNum=getActivity().findViewById(R.id.show_word_num);
       text_book=getActivity().findViewById(R.id.show_book_name);
       relativeLayout=getActivity().findViewById(R.id.relativtion_main_wordFold);
//       匿名接口点击事件
       card_start.setOnClickListener(this);
       card_search.setOnClickListener(this);
       img_refresh.setOnClickListener(this);
       start.setOnClickListener(this);
       textMean.setOnClickListener(this);
       relativeLayout.setOnClickListener(this);

   }
//点击事件
    @Override
    public void onClick(View view) {
//     根据id设置点击事件
        switch (view.getId()){
//            更新单词的图片
        case R.id.img_refresh:
//       点击图片产生动画效果 360旋转
        RotateAnimation animation =new RotateAnimation(0.0f,360.0f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(700);
        animation.setAnimationListener(new  Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation) {

            }
//动画结束设置单词
            @Override
            public void onAnimationEnd(Animation animation) {
                setRandomWord();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        启动动画
        img_refresh.startAnimation(animation);
        break;

    case R.id.text_main_start:
//        点击跳转到加载界面
        if (isOnClick){
//            当点击时跳转
            Intent intent =new Intent(getActivity(), LoadActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            创建场景转换动画
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//            将点击设置为false
            isOnClick = false;
        }
break;
//        点击刷新单词的界面跳转到单词列表展示界面
    case R.id.refresh_show_word_mean:
//       将全局的wordId 设置为WordDetailActivity.wordId
        WordDetailActivity.wordId = currentRandomId;
        Intent intent1 = new Intent(getActivity(), WordDetailActivity.class);
//        传递信息
        intent1.putExtra(WordDetailActivity.TYPE_NAME, WordDetailActivity.TYPE_GENERAL);
        startActivity(intent1);
        break;
//        单词本跳转
    case R.id.relativtion_main_wordFold:
        Intent intent2= new Intent(getActivity(), WordFoldActivity.class);
        startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        break;
//        搜索界面跳转
    case R.id.card_search:
        Intent intent3=new Intent(getActivity(), SearchActivity.class);
//        使用Intent.FLAG_ACTIVITY_NEW_TASK标志来启动新任务
        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent3);
}

    }
//    生成随机的单词
private void setRandomWord() {
//        prepareData变量的值增加1，为了跟踪数据准备的次数
    ++prepareData;
   Log.d("WordFragment", "setRandomWord: " + ConstantData.BookWordNum(currentBookId));
//   调用NumberControl.getRandomNumber方法生成一个介于1和当前书籍单词总数之间的随机整数，作为要显示的单词的ID
    int randomId = NumberControl.getRandomNumber(1, ConstantData.BookWordNum(currentBookId));
    Log.d("WordFragment", "当前ID" + randomId);
//    随机生成的ID赋值给currentRandomId变量
    currentRandomId = randomId;
    Log.d("WordFragment", "要传入的ID" + currentRandomId);
    Log.d("WordFragment", randomId + "");
//    查询与随机id相符的单词
    Word word = LitePal.where("wordId = ?", randomId + "").select("wordId", "word").find(Word.class).get(0);
    Log.d("WordFragment", word.getWord());
//    释义与所查单词相符
    List<Interpretation> interpretations = LitePal.where("wordId = ?", word.getWordId() + "").find(Interpretation.class);
//    设置查询得到的单词
    textWord.setText(word.getWord());
//创建stringbuilder 构建可变字符串
    StringBuilder stringBuilder = new StringBuilder();
//    对释义的列表进行循环
    for (int i = 0; i < interpretations.size(); ++i) {
//将释义的单词类型和中文释义拼接追加到stringbuilder中
        stringBuilder.append(interpretations.get(i).getWordType() + ". " + interpretations.get(i).getCHSMeaning());
//        若当前释义不是最后一个直接换行
        if (i != interpretations.size() - 1)
            stringBuilder.append("\n");
    }
//    设置释义
    textMean.setText(stringBuilder.toString());
}
//fragment必用   将Fragment 与 Activity 之间的正确关联
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

//设置onstart周期，根据目标用户的学习进度以及计划更新UI
    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();
        Log.d("WordFragment", "onStart: ");
//        在使用Calendar获取当前时间
        Calendar calendar = Calendar.getInstance();
//        查询单词数据库以及用户数据库
//        先查询深度学习不为三的单词
//        在查询与当前实现相符的用户
        List<Word> words = LitePal.where("deepMasterTimes <> ?", 3 + "").select("wordId").find(Word.class);
        List<UserData> myDates = LitePal.where("year = ? and month = ? and date = ? and userId = ?",
                calendar.get(Calendar.YEAR) + "",
                (calendar.get(Calendar.MONTH) + 1) + "",
                calendar.get(Calendar.DATE) + "",
                ConfigData.getLoggedNum() + "").find(UserData.class);
//       查询单词不为空，还有需要学习的单词
        if (!words.isEmpty()) {
//            个人用户没有数据即还没点过按钮，还未开始学习，
//            通过数据判断进行控件设置
            if (myDates.isEmpty()) {
                // 未完成计划，将按钮设置为可点击
                card_start.setCardBackgroundColor(getActivity().getColor(R.color.btn));
                start.setTextColor(getActivity().getColor(R.color.colorFontInBlue));
                start.setText("开始背单词");
                isOnClick = true;
            } else {
                // 完成计划，将按钮改变，设置为不可点击
                if ((myDates.get(0).getWordLearnNumber() + myDates.get(0).getWordReviewNumber()) > 0) {
                    card_start.setCardBackgroundColor(getActivity().getColor(R.color.colorBgWhite));
                    start.setTextColor(getActivity().getColor(R.color.colorFontInWhite));
                    start.setText("已完成今日任务");
                    card_start.setClickable(false);
                    isOnClick = false;
                } else {
//                    用户没有学习任何单词，则执行与myDates为空时相同的操作
                    // 未完成计划
                    card_start.setCardBackgroundColor(getActivity().getColor(R.color.btn));
                    start.setTextColor(getActivity().getColor(R.color.colorFontInBlue));
                    start.setText("开始背单词");
                    isOnClick = true;
                }
            }
//            单词列表为空，则显示次数已背玩，且不可点击
        } else {
            card_start.setCardBackgroundColor(getActivity().getColor(R.color.colorBgWhite));
            start.setTextColor(getActivity().getColor(R.color.colorFontInWhite));
            start.setText("恭喜！已背完此书");
            card_start.setClickable(false);
            isOnClick = false;
        }
        // 设置界面数据
        /*1.先查询相关数据
        * 2.根据用户当前选则书籍设置书籍的名称以及设置用户每日学习的数据*/
        List<UserConfig> userConfigs = LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserConfig.class);
        currentBookId = userConfigs.get(0).getCurrentBookId();
        textWordNum.setText("每日须学" + userConfigs.get(0).getWordNeedReciteNum() + "个单词");
        text_book.setText(ConstantData.WordBookName(currentBookId));
//        检查prepareData变量的值是否为0。如果是0，表示还没有准备随机单词数据
        if (prepareData == 0)
            setRandomWord();
    }
}