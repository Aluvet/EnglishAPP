package com.example.overapp.config;

import android.Manifest;

// 存放不变的数据（常量）
public class ConstantData {
    // 数据存放目录（外置存储卡）
    public static final String DIR_TOTAL = "englishLearning";
    // 解压后的数据目录
    public static final String DIR_AFTER_FINISH = "json";
    // 通知渠道ID
    public static final String channelId = "default";
    // 通知渠道名称
    public static final String channelName = "默认通知";
//    创建数组定义相机使用的三个权限,对三个权限进行行授权
    public  static  String[] permissions =new String[]{
//            相机的三个权限
//        相机
        Manifest.permission.CAMERA,
//        读写
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
};
    // 背景图API
    public static final String IMG_API = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=7&n=1";
    public static final String IMG_API_BEFORE = "https://www.bing.com";

    // 每日一句API
    public static final String DAILY_SENTENCE_API = "https://open.iciba.com/dsapi/";

    // 有道英音发音
    public static final String YOU_DAO_VOICE_UK = "https://dict.youdao.com/dictvoice?type=1&audio=";

    // 有道美音发音
    public static final String YOU_DAO_VOICE_US = "https://dict.youdao.com/dictvoice?type=0&audio=";


//单词书分类及其id
    public static final int  KaoYan_MUST =1;
    public static final int   KaoYan_ALL =2;

    //获取单词书
    public static String WordBook(int bookId){
        String  BookAddress="";
        switch (bookId){
            case KaoYan_MUST:
                BookAddress="http://ydschool-online.nos.netease.com/1521164661106_KaoYanluan_1.zip";
                break;
            case KaoYan_ALL:
                BookAddress="http://ydschool-online.nos.netease.com/1521164654696_KaoYan_2.zip";
break;

    }

return BookAddress;
}
    // 根据书ID获取该书的图片
    public static String BookPic(int bookId) {
        String picAddress = "";
        switch (bookId) {
            case KaoYan_MUST:
                picAddress = "https://nos.netease.com/ydschool-online/1496632762670KaoYanluan_1.jpg";
                break;
            case KaoYan_ALL:
                picAddress = "https://nos.netease.com/ydschool-online/youdao_KaoYan_2.jpg";
                break;
        }
        return picAddress;
    }
//获取单词书名称
    public static String WordBookName(int bookId){
      String  BookName="";
      switch (bookId){
          case KaoYan_MUST:
              BookName="考研必考词汇";
              break;
          case KaoYan_ALL:
              BookName="考研英语全部词汇";
              break;

        }
return BookName;

    }

//    单词书的总量
    public static int BookWordNum(int bookId){
        int num = 0;
        switch (bookId) {

            case KaoYan_MUST:
                num = 1341;
                break;
            case KaoYan_ALL:
                num = 4533;
        }
        return num;
    }
    // 根据书ID获取该书的下载后的文件名
    public static String BookFileName(int bookId) {
        String FileName = "";
        switch (bookId) {
            case KaoYan_MUST:
                FileName = "KaoYanluan_1.zip";
                break;
            case KaoYan_ALL:
                FileName= "KaoYan_2.zip";
                break;
        }
        return FileName;
    }
    // 提示句子集合
    public static final String[] phrases = {
            "马行软地易失蹄，人贪安逸易失志",
            "没有热忱，世间便无进步",
            "有志者，事竟成，破釜沉舟，百二秦关终属楚",
            "有心人，天不负，卧薪尝胆，三千越甲可吞吴",
            "风尘三尺剑，社稷一戎衣",
            "只要站起来的次数比倒下去的次数多，那就是成功",
            "收拾一下心情，开始下一个新的开始",
            "你配不上自己的野心，也辜负了曾经历的苦难",
            "现实很近又很冷，梦想很远却很温暖",
            "前方无绝路，希望在转角",
            "没有人会让我输，除非我不想赢",
            "追踪着鹿的猎人是看不见山的",
            "有志始知蓬莱近，无为总觉咫尺远",
            "业精于勤而荒于嬉，行成于思而毁于随",
            "没有所谓失败，除非你不再尝试"};


}
