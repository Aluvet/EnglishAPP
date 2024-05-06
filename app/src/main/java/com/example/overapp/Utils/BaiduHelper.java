package com.example.overapp.Utils;

import android.util.Log;

import com.example.overapp.JSON.JsonAccess;
import com.example.overapp.listener.CallBackListener;
import com.google.gson.Gson;



import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//百度云的sdk 网络使用okhttp

public class BaiduHelper {
//设置变量调用百度云创建应用的的相关数据
    /*
    AppID:55494802
    API_KEY:1RaWYULLC4Sr5YoCY43A1JHj
    SECRET_KEY:KPLm5Swje3rfcnZ6n8T7VIBWX11gaRLH
   获得accesstoken必须：
    1.GRANT_TYPE固定为："client_credentials"
    2.      client_id
    3.   client_secret
    4.https://aip.baidubce.com/oauth/2.0/token  授权服务地址
    * */
    public static final String API_KEY = "1RaWYULLC4Sr5YoCY43A1JHj";

    public static final String SECRET_KEY = "KPLm5Swje3rfcnZ6n8T7VIBWX11gaRLH";

    public static final String GRANT_TYPE = "client_credentials";

    public static final String URL_GET_ASSESS_TOKEN = "https://aip.baidubce.com/oauth/2.0/token";

    public static String assessToken;

    //----------------------------------------------------------

    public static final String URL_GET_OCR = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";

//    获取百度云获取 Access_token
    public static void getAccessToken() {
 /*请求URL数据格式
向授权服务地址https://aip.baidubce.com/oauth/2.0/token发送请求（推荐使用POST），并在URL中带上以下参数：
grant_type： 必须参数，固定为client_credentials；
client_id： 必须参数，应用的API Key；
client_secret： 必须参数，应用的Secret Key；*/
//        使用formbody，请求百度云先创建请求体
        RequestBody formBody = new FormBody.Builder()
//                像表单中添加的对应参数，键值对
                .add("grant_type", GRANT_TYPE)
                .add("client_id", API_KEY)
                .add("client_secret", SECRET_KEY)
                .build();
//        创建request请求
        Request request = new Request.Builder()
//                设置请求方法，并用formBody作为请求体
                .post(formBody)
//                授权地址
                .url(URL_GET_ASSESS_TOKEN)
                .build();
//使用okhttp请求并响应
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request)
//                使用异步
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
//失败
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        使用json对http的字符串转化为Java对象 。选用GSON库进行
                        Gson resultGson = new Gson();
                        JsonAccess jsonAccess = resultGson.fromJson(response.body().string(), JsonAccess.class);
//                        将惊悚的token的值进行复制
                        assessToken = jsonAccess.getAccess_token();
//                        官方示例accesstoken：24.6c5e1ff107f0e8bcef8c46d3424a0e78.2592000.1485516651.282335
                        Log.d("BaiduHelper", assessToken);
                    }
                });
    }

//    通用文字识别
    /*
    * 请求URL ：POST /rest/2.0/ocr/v1/general_basic
    * 需要参数：access_token
    * 请求Body参数
       Form字段数据结构说明
    *1.imag：和 url/pdf_file/ofd_file 四选一：
    * 图像数据，base64编码后进行urlencode，
    * 要求base64编码和urlencode后大小不超过8M，
    * 最短边至少15px，最长边最大4096px，
    * 支持jpg/jpeg/png/bmp格式 优先级：image > url > pdf_file，
    * 当image字段存在时，url、pdf_file字段失效
     *2. language_type 语言类型：识别英文   ENG：英文 -
     *3.  detect_direction  是否检测图像朝向  true：检测朝向；
    * */
    public static void analysePicture(String imgUrl, final CallBackListener callBackListener) {
//        构建请求体  ，添加需要的键值对，送给服务器
        RequestBody formBody = new FormBody.Builder()
                .add("image", imgUrl)
                .add("detect_direction", "true")
                .add("language_type", "ENG")
                .build();
//        构建请求 ，使用推荐的post 请求
        Request request = new Request.Builder()
                .post(formBody)
//                拼接
                .url(URL_GET_OCR + "?access_token=" + assessToken)
                .build();
/*响应体字段数据结构说明
        字段	是否必选	类型	说明
         direction	否	int32	图像方向，当 detect_direction=true 时返回该字段。
         - - 1：未定义，- 0：正向，- 1：逆时针90度，- 2：逆时针180度，- 3：逆时针270度
              log_id	是	uint64	唯一的log id，用于问题定位
           words_result_num	是	uint32	识别结果数，表示words_result的元素个数
            words_result	是	array[]	识别结果数组*/
//        发送请求:
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request)
//                * `onFailure`: 当请求失败时，会调用此方法，并传递`Call`对象和发生的异常。
//                * `onResponse`: 当请求成功时，会调用此方法，并传递`Call`对象和响应对象。
//                .enqueue方法表示异步执行请求
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callBackListener.onFailure(call, e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        callBackListener.onResponse(call, response);
                    }
                });
    }
}
