package com.example.overapp.Utils;

import java.util.Random;
//数字工具类用于生成随机数
public class NumberControl {
//    设置随机数
// 得到区间里的一个随机数
// 两个端点都能取到
public static int getRandomNumber(int min, int max) {
//    判断区间有效
    if (min < max) {
//        创建random生成随机数
        Random random = new Random();
//        使用nextInt方法生成一个在[0, max - min]范围内的随机数，然后加上min
        // 这样就确保了随机数在[min, max]范围内，并且包括min和max
        return random.nextInt(max - min + 1) + min;
    } else {

        return min; // 如果min和max相等，直接返回min,只有一个数字可取
    }
}
    // 得到区间里的N个随机数
    // 参数n必须大于0
//    随机数数组传参 最小 最大 ,以及个数
    public static int[] getRandomNumberList(int min, int max, int n) {
        // 判断n是否大于区间内可能的不同随机数的数量，或者max是否小于min
//        即判断是否是无效区间
        if (n > (max - min + 1) || max < min) {
            return null;
        }
//        创建大小为n的数组来存放结果
        int[] result = new int[n];
        // 初始化计数器为0，追踪已经生成的随机数的数量
        int count = 0;
//        循环到n
        while (count < n) {
//            生成随机数,而且是生成了n个不重复的随机数
            int num = getRandomNumber(min, max);
//            设置flag,标志上
            boolean flag = true;
//
            for (int j = 0; j < count; j++) {
                // 如果找到了相同的数，设置flag为false并跳出循环
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
//            // 如果数是新的（即flag为true），则将其添加到结果数组中，并增加计数器
            if (flag) {
                result[count] = num;
                count++;
            }
        }
//        // 返回包含n个不重复随机数的数组
        return result;
    }

}
