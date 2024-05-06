package com.example.overapp.Utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import androidx.annotation.NonNull;

//修改特定试图
//第二种方法是继承Span架构中的抽象类或者是实现特定接口。
// 需要注意的是，在上一篇文章中说的CharacterStyle,ParagraphStyle.UpdateAppearance和UpdateLayout
// 都是没有函数的，所以，我们无法直接继承或者实现它们。除了第一篇文章中所介绍的那些Span可以使用第一种方法进行继承。
// 我们一般都继承或者实现MetricAffectingSpan,ReplacementSpan或者LineBackgroundSpan。
//绘制日历中的背京
public class CircleBack implements LineBackgroundSpan {
//    重写drawcack设计自己的方法
    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
//  创建新的Paint对象，定义如何绘制圆形
        Paint paint1 =new Paint();
//        该方法作用是抗锯齿，让图形更平滑
        paint1.setAntiAlias(true);
        paint1.setColor(Color.parseColor("#90b8f9"));
//        设置蓝色
//        该方法用于在画布上绘制圆形，通过指定圆形圆心的坐标和半径来实现。该方法是绘制圆形的主要方法，同时也可以通过设置画笔的空心效果来绘制空心的圆形
//        半径8 ，水平中间，竖直的底部40像素位置
        canvas.drawCircle((right - left) / 2, (bottom - top) / 2 + 40, 8, paint1);
    }
}
