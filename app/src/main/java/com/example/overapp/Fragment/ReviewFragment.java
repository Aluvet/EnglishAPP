package com.example.overapp.Fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.overapp.Activity.BaiDuOcrActivity;
import com.example.overapp.Activity.MatchActivity;
import com.example.overapp.Activity.SpeedActivity;
import com.example.overapp.ItemData.ItemMatchWord;
import com.example.overapp.JSON.JsonBaidu;
import com.example.overapp.JSON.JsonBaiduWords;

import com.example.overapp.R;

import com.example.overapp.Utils.BaiduHelper;
import com.example.overapp.Utils.Base64Utils;
import com.example.overapp.Utils.FileUtils;
import com.example.overapp.Utils.MyApplication;
import com.example.overapp.Utils.NumberControl;
import com.example.overapp.Utils.WordsControllor;
import com.example.overapp.config.ConfigData;
import com.example.overapp.database.Interpretation;
import com.example.overapp.database.Word;
import com.example.overapp.listener.CallBackListener;
import com.google.gson.Gson;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class ReviewFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout cardPhoto ;
    private LinearLayout cardSpeed,cardMatch;
    final String[] items ={"拍照","相册"};
    private final int REQUEST_IMAGE_CAPTURE = 3000;
    private final int REQUEST_CODE_IMAGE_ = 2000;
    private ProgressDialog progressDialog;
//    消息
    private final int LOAD_MATCH = 2;
    private final int LOAD_SPEED = 1;
    private final int WRONG =3;
    private  final  int FINISH =4;
//    接受message传来的信息
//    做出相应的更新
@SuppressLint("HandlerLeak")
//每次有消息发送到这个Handler时被调用
private Handler handler = new Handler() {
    @Override
    public void handleMessage(@NonNull Message message) {
        switch (message.what){

//          根据传来的线程进行相关操作
//            关闭对话框，后启动新的speedActivity
            case LOAD_SPEED:
                progressDialog.dismiss();
                Intent intent3 = new Intent();
                intent3.setClass(MyApplication.getContext(), SpeedActivity.class);
                startActivity(intent3, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
//                启动单词匹配
            case LOAD_MATCH:
                progressDialog.dismiss();
                Intent intent4 = new Intent();
                intent4.setClass(MyApplication.getContext(), MatchActivity.class);
                startActivity(intent4, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
//                拍照完成后跳转到新的界面，，关闭进度对话框，并启动一个新的BaiDuOcrActivity
            case FINISH:
                progressDialog.dismiss();
                Intent intent = new Intent(getActivity(), BaiDuOcrActivity.class);
                startActivity(intent);
                break;
//                关闭进度对话框，并显示一个短时间的Toast消息，告诉用户发生了错误，请稍后重试
            case WRONG:
                progressDialog.dismiss();
                Toast.makeText(MyApplication.getContext(), "发生错误，请稍后重试", Toast.LENGTH_SHORT).show();
                break;

        }
    }
};
    @Nullable
    @Override
//    fragment创建返回试图fragment辟必写
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        创建视图
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        return view;
    }
//视图创建后调用
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
     super.onActivityCreated(savedInstanceState);
//使用getActivity()方法获取与Fragment关联的Activity实例，然后通过findViewById()方法找到对应的视图组件
////初始化
        cardPhoto=getActivity().findViewById(R.id.layout_re_analysephoto);
        cardSpeed =getActivity().findViewById(R.id.layout_re_wordsspeed);
        cardMatch =getActivity().findViewById(R.id.layout_re_wordsmatch);
////        设置点击事件
        cardPhoto.setOnClickListener(this);
    cardSpeed.setOnClickListener(this);
    cardMatch.setOnClickListener(this);
    }
//    点击事件
    @Override
    public void onClick(View view) {
//        切换页面
//        在非activity中启动activity要加Flag——ACTIVITY_NEW_TASK
        Intent intent =new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//switch
        switch (view.getId()){
            case R.id.layout_re_analysephoto:
//通过构建diolog供用户选择
                AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
                builder.setTitle("请选择方式")
//                        使用setSingleChoiceItems()方法设置单选
//1.items：定义的单选选项数组
//2.checkedItem：默认被选中的选项。默认不选中为-1，选中数组第一个为0，选中数组第二个为1，选择数组第三个为2
//3.listener：侦听器
//                        -1为默认不选
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
//                            对设置选项用户选择进行不同操作
                            public void onClick(DialogInterface dialogInterface, int which) {
                                final int num= which;
//                                开启新线程延迟启动
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        switch (num){
                                            case 0:
//                                                启动相机，intent设置相机动作
                                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                                startActivityForResult方法来启动相机应用
                                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                                break;
                                            case 1:
//                                                选择相册  Intent一个action 和 url使用startActivityForResult(intent2, REQUEST_CODE_IMAGE_);启动
                                                Intent intent2 =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                startActivityForResult(intent2, REQUEST_CODE_IMAGE_);
                                                break;

                                        }
                                        dialogInterface.dismiss();
                                    }
                                },200);
                            }
                        })
                        .show();
                break;
//                单词速过
            case R.id.layout_re_wordsspeed:
//                显示
                showWaitProgressDialog("等待中");
//                开启新线程加载数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        根据用户设定加在单词，先得到单词数量
                        loadSpeedWords(ConfigData.getSpeedPlanNum());
                        Message message = new Message();
                        message.what = LOAD_SPEED;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            case R.id.layout_re_wordsmatch:
                showWaitProgressDialog("等待中");
//                开启新线程加载数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadMatchWords();
                        Message message = new Message();
                        message.what = LOAD_MATCH;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
        }

    }
//    准备速记模块的数据
private void loadSpeedWords(int num) {
//        通用先判断是否为空，若非，则清空，下同
    if (!SpeedActivity.wordList.isEmpty())
        SpeedActivity.wordList.clear();
    // 准备单词数据
    List<Word> words = LitePal.select("wordId", "word").find(Word.class);
    // 调用random方法随机匹配单词ID
    int[] randomId = NumberControl.getRandomNumberList(0, words.size() - 1, num);
    for (int i = 0; i < num; ++i) {
        // 添加数据  。randomId[i]从words列表中选择一个单词
        SpeedActivity.wordList.add(words.get(randomId[i]));
    }
}
//准备匹配数据
private void loadMatchWords() {
//        同上
    MatchActivity.itemMatchWordArrayListAll.clear();
    if (!MatchActivity.wordList.isEmpty())
        MatchActivity.wordList.clear();
    if (!MatchActivity.itemMatchWordArrayList.isEmpty())
        MatchActivity.itemMatchWordArrayList.clear();
    List<Word> words = LitePal.select("wordId", "word").find(Word.class);
    int[] randomId = NumberControl.getRandomNumberList(0, words.size() - 1, ConfigData.getMatchPlanNum());
//    循环遍历随机生成的数字数组 randomId，为每个随机数字执行添加操作
    for (int i = 0; i < randomId.length; ++i) {

        MatchActivity.itemMatchWordArrayList.add(new ItemMatchWord(randomId[i], words.get(randomId[i]).getWord(), false, false));
        MatchActivity.itemMatchWordArrayListAll.add(new ItemMatchWord(words.get(randomId[i]).getWordId(), words.get(randomId[i]).getWord(), false, false));
//查询与当前随机单词 wordId 相关的所有解释（Interpretation 对象），并构建一个字符串 stringBuilder
        List<Interpretation> interpretations = LitePal.where("wordId = ?", words.get(randomId[i]).getWordId() + "").find(Interpretation.class);
//    十一拼接
        StringBuilder stringBuilder = new StringBuilder();
        for (int ii = 0; ii < interpretations.size(); ++ii) {
            if (ii != (interpretations.size() - 1))
                stringBuilder.append(interpretations.get(ii).getWordType() + ". " + interpretations.get(ii).getCHSMeaning() + "\n");
            else
                stringBuilder.append(interpretations.get(ii).getWordType() + ". " + interpretations.get(ii).getCHSMeaning());
        }
        MatchActivity.itemMatchWordArrayList.add(new ItemMatchWord(randomId[i], stringBuilder.toString(), false, false));
    }

//  最后打乱打乱
    Collections.shuffle(MatchActivity.itemMatchWordArrayList);

}
//    构建谈话狂方法
private void showWaitProgressDialog(String content) {
    progressDialog = new ProgressDialog(getActivity());
    progressDialog.setTitle("请稍后");
    progressDialog.setMessage(content);
    progressDialog.setCancelable(false);
    progressDialog.show();
}
//在请求香鸡跳转时发送请求吗处理返回的数据

  @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
        final Intent intent=data;

        switch (requestCode){
//            根据传入请求吗进行相机和相册操作
            case REQUEST_IMAGE_CAPTURE:
//                使用bitmap解析返回的图片
                if(data.getExtras() != null){
showWaitProgressDialog("正在识别分析中");
//耗时操作开新线程图片操作，数据分析以及数据下载等操作花费大量时间要使用异步处理
new Thread(new Runnable() {
    @Override
    public void run() {
      //        从附加数据中获取图片（Bitmap对象），后进行照片操作，传入入百度
//        将接收的数据转化为bitmap
//        墙砖
//        Intent intent=getIntent();
//        Bundle b=intent.getExtras();
//        Bitmap bmp=(Bitmap) b.getParcelable("bitmap");

        Bitmap bitmap =(Bitmap)intent.getExtras().get("data");
//        使用base64解码等操作使用百度发送请求
//        先对图片压缩后使用encode进行编码转化，再用百度help将图片传入百度云进行照片分析，CallBackListener是一个回调监听器，用于处理百度分析的响应结果
        BaiduHelper.analysePicture(Base64Utils.encode(FileUtils.bitmapCompress(bitmap, 3999)), new CallBackListener() {

            @Override
//            网络请求失败时调用此方法
            public void onFailure(okhttp3.Call call, IOException e) {
                Message message = new Message();
                message.what = WRONG;
                handler.sendMessage(message);
            }
//成功后进行下面操作
            /*1.将响应内容转换为字符串。
              2.使用Gson库将字符串转换为Java对象。
              3. 调用analyseJsonData方法处理解析后的数据。
              4.处理成功，发送消息通知UI线程任务已完成；如果处理失败，则发送错误消息。*/
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
//              将网络请求响应转化为字符串
                String str = response.body().string();
                Log.d("ReviewFragment", str);
                Gson gson = new Gson();
                try {
                    analyseJsonData(str, gson);
                    Message message = new Message();
                    message.what = FINISH;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = new Message();
                    message.what = WRONG;
                    handler.sendMessage(message);
                }
            }


        });
    }
}).start();
                }
                break;
//                相册信息，当传入参数为相册时，基本操作同上，开心线程
            case REQUEST_CODE_IMAGE_:
                showWaitProgressDialog("正在识别分析中");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Uri selectedImage = intent.getData(); //获取系统返回的照片的Uri
//                        MediaStore.Images.Media.DATA  媒体存储中图片文件的实际路径列 ，创建字符串数组，用于指定从媒体存储中查询的列
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        当我们往手机上放图片或者音乐的时候，会在手机内存中某个位置上的某个database中存放图片或者音乐的信息，
//                        而我们的应用程序是能够通过ContentResolver去读取到这些数据的
//                        使用应用上下文的内容解析器（ContentResolver）和前面定义的URI，查询媒体存储以获取图片的实际文件路径。查询结果存储在Cursor对象中
                        Cursor cursor = MyApplication.getContext().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
//                   获取并关闭游标
//游标移动到第一行记录
                        cursor.moveToFirst();
//                        getColumnIndex(): 获取指定列名
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        根据之前获取的列索引，从Cursor中读取该列的数据，并将其作为字符串返回
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
//                        将图片解码为bitmap，在同上方操作压缩，编码，以及发送
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        BaiduHelper.analysePicture(Base64Utils.encode(FileUtils.bitmapCompress(bitmap, 2000)), new CallBackListener() {

                            @Override
                            public void onFailure(okhttp3.Call call, IOException e) {
                                Message message = new Message();
                                message.what = WRONG;
                                handler.sendMessage(message);
                            }

                            @Override
                            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                                String str = response.body().string();
                                Gson gson = new Gson();
                                try {
                                    analyseJsonData(str, gson);
                                    Message message = new Message();
                                    message.what = FINISH;
                                    handler.sendMessage(message);
                                } catch (Exception e) {
                                    Message message = new Message();
                                    message.what = WRONG;
                                    handler.sendMessage(message);
                                }
                            }
                        });

                    }
                }).start();
                break;
        }
        } else {
            // 取消了
        }
        super.onActivityResult(requestCode, resultCode, data);



}
//shiyongJSon解析数据
    private void analyseJsonData(String str, Gson gson) {
//        将字符串str转化为小写，解析为jsonbaidu对象
        JsonBaidu jsonBaidu = gson.fromJson(str.toLowerCase(), JsonBaidu.class);
//       解析成功
        if (jsonBaidu != null) {
//            从jsonBaidu对象中获取words_result列表 及分析返回数据
            List<JsonBaiduWords> jsonBaiduWordsResult = jsonBaidu.getWords_result();
//            stringbuilder 追加空格
//            遍历jsonBaiduWordsResult列表，将每个JsonBaiduWords对象的words属性添加到StringBuilder中，每个词后面加一个空格
            StringBuilder stringBuilder = new StringBuilder();
            for (JsonBaiduWords jsonBaiduWords : jsonBaiduWordsResult) {
                stringBuilder.append(jsonBaiduWords.getWords() + " ");
            }
      Log.d("ReviewFragment", stringBuilder.toString());
//            字符串使用分割后变为字符串数组
            String[] result = stringBuilder.toString().split(" ");
            Log.d("ReviewFragment", Arrays.toString(result));
//            如果结果数组的长度大于或等于1（分析有结果），
//            则清空WordsControllor.needLearnWords列表，并创建一个新的HashMap
            if (result.length >= 1) {
                WordsControllor.needLearnWords.clear();
                HashMap<Integer, Integer> map = new HashMap<>();
//                循环结果数组
                for (int i = 0; i < result.length; ++i) {
                    Log.d("ReviewFragment", i + result[i]);
//                    使用`LitePal`查询数据库，查找与当前词匹配的记录。
//                    如果找到匹配的记录，并且这个词的ID还没有在`map`中，则将其添加到`map`中
//                    containsValue() 方法检查 hashMap 中是否存在指定的 value 对应的映射关系
                    List<Word> words = LitePal.where("word = ?", result[i]).select("wordId", "word").find(Word.class);
                    if (!words.isEmpty()) {
                        Log.d("ReviewFragment", i + "我找到了" + words.get(0).getWord());
                        if (!map.containsValue(words.get(0).getWordId())) {
                            map.put(i, words.get(0).getWordId());
                            Log.d("ReviewFragment", "我已添加" + words.get(0).getWord());
                        }
                    }
                }
//                将map中的词ID添加到WordsControllor.needLearnWords列表中
                for (int ii : map.keySet()) {
                    WordsControllor.needLearnWords.add(map.get(ii));
                }
                Log.d("ReviewFragment", "长度：" + WordsControllor.needLearnWords.size());
            }
        }
    }


}