package com.example.overapp.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
//继承AppBarLayout.Behavior并重新设置给AppBarLayout来修改AppBarLayout的默认滚动行为，实现AppBarLayout的弹性越界效果就可以通过这种方式实现。
//处理AppBarLayout在CoordinatorLayout中的滚动行为
public class FixAppBarLayoutBehavior extends AppBarLayout.Behavior {
    //第一不步重写构造方法
    public FixAppBarLayoutBehavior() {
        super();
    }

    public FixAppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//处理嵌套滚动事件。当coordinatorLayout中发生滚动时，这个方法会被调用
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
//                               dxConsumed和dyConsumed表示已经被消费掉的滚动距离，dxUnconsumed和dyUnconsumed表示还未被消费的滚动距离 type滚动事件的类型
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, type);
//        调用了stopNestedScrollIfNeeded来判断是否需要停止嵌套滚动
        stopNestedScrollIfNeeded(dyUnconsumed, child, target, type);
    }
//处理预滚动事件。预滚动事件发生在滚动开始之前，用于预测滚动是否会发生
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
                                  View target, int dx, int dy, int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
//        同上判断是否需要判断滚动
        stopNestedScrollIfNeeded(dy, child, target, type);
    }
//        判断滚动是否停止      dy表示滚动的垂直距离，type表示滚动事件的类型
    private void stopNestedScrollIfNeeded(int dy, AppBarLayout child, View target, int type) {
//        判断滚动类型是非触摸类型，直接获取偏移量以及判断偏移量符合范围吗
        if (type == ViewCompat.TYPE_NON_TOUCH) {
//            getTopAndBottomOffset()方法用于获取AppBarLayout的当前偏移量
            final int currOffset = getTopAndBottomOffset();
//            偏移量为0（表示AppBarLayout已经完全展开）或者当前偏移量等于AppBarLayout的总滚动范围（表示AppBarLayout已经完全折叠）
            if ((dy < 0 && currOffset == 0)
//                    AppBarLayout的总滚动范围（通过getTotalScrollRange()方法获取，当AppBarLayout折叠时，这个偏移量会是负值
                    || (dy > 0 && currOffset == -child.getTotalScrollRange())) {
//                调用ViewCompat.stopNestedScroll来停止嵌套滚动
                ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH);
            }
        }
    }

}
