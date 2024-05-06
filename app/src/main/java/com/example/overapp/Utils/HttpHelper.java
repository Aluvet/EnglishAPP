package com.example.overapp.Utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//        okhttp基本使用步骤如下
//
//构建客户端对象OkHttpClient
//构建请求Request
//生成Call对象
//Call发起请求（同步/异步）



//创建Http请求类，okhttp发送HTTP GET请求获取相应实力
public class HttpHelper {
    // 发送普通Request请求，获得对应的数据，返回字符串
//    传入URl
    public static String requestResult(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
//        OkHttpClient的newCall(request).execute()方法来发送请求，并获取一个Response对象
//        其中try-with-resource语句用于资源正确关闭
        try (Response response = client.newCall(request).execute()) {
//            从Response对象中获取响应体（ResponseBody），并将其转换为字符串（使用string()方法
            return response.body().string();
        }
    }

    // 发送普通Request请求，获得对应的数据，返回字节数组
    public static byte[] requestBytes(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
//     从Response对象中获取响应体（ResponseBody），并将其转换为字节（使用bytes()方法
            return response.body().bytes();
        }
    }

}
