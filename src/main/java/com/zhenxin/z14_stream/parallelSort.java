package com.zhenxin.z14_stream;

import java.util.Arrays;
import java.util.Random;

/**
 * @Created: 15:25 23/12/2018
 * @Author: xzhen
 * @Description: 并行排序
 */
public class parallelSort {
    public static void main(String[] args) {
        int len = 10000000;
        int[] arr = new int[len];
        Random r = new Random();

//        for (int i = 0; i < len; i++) {
//            arr[i] = r.nextInt(200);
//        }

        Arrays.setAll(arr, i -> r.nextInt(200));

        long s = System.currentTimeMillis();
        Arrays.parallelSort(arr);
//        Arrays.sort(arr);
        long e = System.currentTimeMillis();
        System.out.println("time: " + (e - s) + "ms");
    }
}
