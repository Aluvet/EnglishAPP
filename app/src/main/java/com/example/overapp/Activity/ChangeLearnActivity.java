package com.example.overapp.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.overapp.R;
import com.example.overapp.Utils.ActivityCollector;
import com.example.overapp.Utils.FileUtils;
import com.example.overapp.Utils.GSON;
import com.example.overapp.config.ConfigData;
import com.example.overapp.config.ConstantData;
import com.example.overapp.database.UserConfig;
import com.example.overapp.database.UserData;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChangeLearnActivity extends BaseAcyivity  {


    private EditText editText;
    private TextView text_start,text_Book,text_WordsMaxNum;
    private  int WordsMax;
    private  int currentBookId;
    private List<UserConfig> userConfigs ;
    private ProgressDialog progressDialog;
    private final int FINISH = 1;
    private final int DOWN_DONE = 2;


//开启新线程下载东西

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FINISH:
                    // 等待框消失
                    progressDialog.dismiss();
                    // 重置上次学习时间，以及单词书
                    UserConfig userConfig1 = new UserConfig();
                    userConfig1.setLastStartTime(0);
                    userConfig1.setCurrentBookId(currentBookId);
                    userConfig1.updateAll("userId = ?", ConfigData.getLoggedNum() + "");
                    // 删除当天打卡记录
                    Calendar calendar = Calendar.getInstance();
                    LitePal.deleteAll(UserData.class, "year = ? and month = ? and date = ? and userId = ?"
                            , calendar.get(Calendar.YEAR) + ""
//                            月份初始要+1
                            , (calendar.get(Calendar.MONTH) + 1) + ""
                            , calendar.get(Calendar.DAY_OF_MONTH) + ""
                            , ConfigData.getLoggedNum() + "");
                    ActivityCollector.startOtherActivity(ChangeLearnActivity.this, MainActivity.class);
                    break;
                case DOWN_DONE:
                    progressDialog.setMessage("已下载完成，请等待数据加载");
                    break;
            }
        }
    };
//活动创建必须
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_learn);
//        初始化

        editText =findViewById(R.id.edit_num);
        text_start=findViewById(R.id.text_start);
        text_Book=findViewById(R.id.text_type);
        text_WordsMaxNum=findViewById(R.id.Max_wordsmax);
        Log.d("ChangeLearnActivity", "onCreate: ");
//        获取当前的intent对象，对应choosewordbook传入数据的intent
        final Intent intent = getIntent();
//查询数据库
        userConfigs = LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserConfig.class);
//        判断当前用户的需要复习的单词数量不为 0，则将该数量设置为 EditText 组件的文本
        if (userConfigs.get(0).getWordNeedReciteNum() != 0)
            editText.setText(userConfigs.get(0).getWordNeedReciteNum() + "");
//        开始点击事件
        text_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                判断在text中获取的文字是否为空
                if (!editText.getText().toString().trim().equals("")) {
//                    不为空   判断框中的数字先转化为string类型再转为int类型使用interge.parseInt与最低数和最高数比较
                   if (Integer.parseInt(editText.getText().toString().trim()) >= 5
                            && Integer.parseInt(editText.getText().toString().trim()) < WordsMax) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        隐藏键盘
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        // 设置数据并更新数据
//                        创建新对象，设置相关信息，更新       满足条件设置学习词汇量
                        final UserConfig userConfig = new UserConfig();
                        userConfig.setWordNeedReciteNum(Integer.parseInt(editText.getText().toString().trim()));
                        userConfig.updateAll("userId = ?", ConfigData.getLoggedNum() + "");

                        // 第一次设置数据，通过okhttp获取服务器文件后解析成json
                        if (ConfigData.notUpdate == intent.getIntExtra(ConfigData.UPDATE_NAME, 0)) {
//                            构建对话框
                            progressDialog = new ProgressDialog(ChangeLearnActivity.this);
                            progressDialog.setTitle("请稍等");
                            progressDialog.setMessage("数据正在获取中");
//                        按取消键不能取消
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            // 延迟两秒再运行，防止等待框不显示
//                        在谈话狂出现后延迟开始新线程下载
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                抛出新县城分析数据
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
//                                            使用步骤：
//创建OkHttpClient对象
//构造Request对象
//通过OkHttpClient和Request对象来构建Call对象
//通过Call对象的enqueue(Callback)方法来执行异步请求
// 下载
                                                OkHttpClient client = new OkHttpClient();
                                                Request request = new Request.Builder().
//                                                        "http://ydschool-online.nos.netease.com/1521164661106_KaoYanluan_1.zip"
//        根据用户选择的书籍下载对应的文件
                                                        url(ConstantData.WordBook(currentBookId)).build();
                                                Response response = client.newCall(request).execute();
//                                                通过message传递信息进行下一步操作下同
                                                Message message = new Message();
                                                message.what = DOWN_DONE;
                                                handler.sendMessage(message);
//                                            调用fileUtils中的将okhttp接受的字符数组转化为文件，并将地址职位englishlean，文件名设为zip
                                                FileUtils.getFileByBytes(response.body().bytes(), getFilesDir() + "/" + ConstantData.DIR_TOTAL, ConstantData.BookFileName(currentBookId));
                                                //设为zip后创建zip进行解压
                                                FileUtils.unZipFile(getFilesDir() + "/" + ConstantData.DIR_TOTAL + "/" + ConstantData.BookFileName(currentBookId)
                                                        , getFilesDir() + "/" + ConstantData.DIR_TOTAL + "/" + ConstantData.DIR_AFTER_FINISH, false);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
//使用json读取解压到本地的文件并进行解析生成数据
                                            GSON.analysJSONandSAVE(FileUtils.readLocalData(ConstantData.DIR_TOTAL + "/" + ConstantData.DIR_AFTER_FINISH + "/" + ConstantData.BookFileName(currentBookId).replace(".zip", ".json")));
                                            Message message = new Message();
                                            message.what = FINISH;
                                            handler.sendMessage(message);
                                        }
                                    });
                                    thread.start();
                                }
                            }, 500);
                        }
                        else {
                          //判断如果不相等
                            if (userConfigs.get(0).getWordNeedReciteNum() != Integer.parseInt(editText.getText().toString().trim())) {
                                // 重置上次学习时间
                                UserConfig userConfig1 = new UserConfig();
                                userConfig1.setLastStartTime(-1);
                                userConfig1.updateAll("userId = ?", ConfigData.getLoggedNum() + "");
                                Toast.makeText(ChangeLearnActivity.this, "" + LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserConfig.class).get(0).getLastStartTime(), Toast.LENGTH_SHORT).show();
                                Log.d("ChangeLearnActivity", "onClick: " + LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserConfig.class).get(0).getLastStartTime());
                                // 删除当天打卡记录
                                Calendar calendar = Calendar.getInstance();
                                LitePal.deleteAll(UserData.class, "year = ? and month = ? and date = ? and userId = ?"
                                        , calendar.get(Calendar.YEAR) + ""
                                        , (calendar.get(Calendar.MONTH) + 1) + ""
                                        , calendar.get(Calendar.DAY_OF_MONTH) + ""
                                        , ConfigData.getLoggedNum() + "");
                                Toast.makeText(ChangeLearnActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChangeLearnActivity.this, "计划没有改变", Toast.LENGTH_SHORT).show();
                            }
                            ActivityCollector.startOtherActivity(ChangeLearnActivity.this, MainActivity.class);
                        }
                    } else {
                        Toast.makeText(ChangeLearnActivity.this, "请输入合理的范围", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangeLearnActivity.this, "请输入值再继续", Toast.LENGTH_SHORT).show();
                }
            }

    });

}

//    设置对应的数字

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ChangeLearnActivity", "onStart: ");
        // 获得数据，根据限定用户id查询数据库
        List<UserConfig> userConfigs = LitePal.where("userId = ?", ConfigData.getLoggedNum() + "").find(UserConfig.class);

        // 检查列表是否为空
        if (!userConfigs.isEmpty()) {
//            不为空，取第一个对象，获取当前选用bookid的值
            UserConfig userConfig = userConfigs.get(0);
            currentBookId = userConfig.getCurrentBookId();
//通过当前选用书籍的单词数设置对应的最大单词数
            WordsMax = ConstantData.BookWordNum(currentBookId);

            // 设置单词书相关数据
            text_WordsMaxNum.setText(String.valueOf(WordsMax));
            Log.d("ChangeLearnActivity", "currentBookId: " + currentBookId);
            Log.d("ChangeLearnActivity","maxNum: "+WordsMax);
            // 设置书名
            text_Book.setText(ConstantData.WordBookName(currentBookId));
        } else {
            Log.e("ChangeLearnActivity", "onStart: No userConfigs found");
            // 处理列表为空的情况，例如显示错误消息或重定向用户
        }

    }
//    同其他返回
    @Override
    public void onBackPressed(){
        List<UserConfig> userConfigs = LitePal.where("userId=?", ConfigData.getLoggedNum()+"").find(UserConfig.class);
//        当设置完后，执行返回操作关闭活动
        if (userConfigs.get(0).getWordNeedReciteNum()!=0){
            super.onBackPressed();
        }
        else {
//            没有直接跳转会选择词书
            ActivityCollector.startOtherActivity(ChangeLearnActivity.this, ChooseWordBookActivity.class);
        }
    }
}