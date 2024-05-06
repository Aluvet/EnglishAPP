package com.example.overapp.Utils;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.example.overapp.config.ConstantData;
//音频播放工具类
public class MediaPlayHelper {
//    播放音乐
    public static MediaPlayer mediaPlayer;
    // 美国发音
    public static final int VOICE_US = 0;

    // 英文发音
    public static final int VOICE_UK = 1;


    // 默认
    public static final int DEFAULT_VOICE = VOICE_UK;
//    该方法通过传入wordName 调用play方法实现单词播放传入单词的默认英文发音
    public static void play(String wordName) {
        play(DEFAULT_VOICE, wordName);
    }
//    真正的播放音频的方法，传入发音类型即英文或美式发音
    public static void play(int type, String wordName) {
//        检查是否存在，如果存在，释放资源创建新的资源
        if (mediaPlayer != null) {
            releasePlayer();
            mediaPlayer = new MediaPlayer();
        } else
//            不存在直接创建新的资源对象
            mediaPlayer = new MediaPlayer();
//        try——catch捕获并处理可能出现的异常
        try {
//            判断当前传入的是英文
            if (VOICE_UK == type)
/*               设置音频数据源将url与对应单词拼接进行设置
                美音：http://dict.youdao.com/dictvoice?type=0&audio=hello
                英音：http://dict.youdao.com/dictvoice?type=1&audio=hello
*/
                mediaPlayer.setDataSource(ConstantData.YOU_DAO_VOICE_UK + wordName);
            else
//                英文也设置数据源
                mediaPlayer.setDataSource(ConstantData.YOU_DAO_VOICE_US + wordName);
//         较大的资源或者网络资源建议使用prepareAsync方法,异步加载
            /*让资源启动,即start()起来,因为在异步中,
          需要设置监听事件setOnPreparedListener();来通知MediaPlayer资源已经获取到了,
          然后实现onPrepared(MediaPlayer mp)方法.在里面启动MediaPlayer
*/
//            数据原设置完成使用prepareAsync方法,异步加载
            mediaPlayer.prepareAsync();
//            设置监听通知启动
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
//            etOnCompletionListener，这里的说明指出该方法允许你注册一个回调。
//            当媒体资源或音频文件到达结束位置时会回调该方法，注意该方法的输入是OnCompletionListener
//            音频播放结束,调用方法释放资源
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    若media对象不为空
                    if (mediaPlayer != null) {
//                        调用release方法释放并在置为空
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
            });
        }
//        捕获并抛出异常
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void releasePlayer() {
        if (mediaPlayer != null) {
//            正在播放则停止
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
//            释放资源并置为空
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
