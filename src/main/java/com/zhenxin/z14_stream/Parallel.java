package com.zhenxin.z14_stream;

import java.util.stream.IntStream;

/**
 * @Created: 23:13 22/12/2018
 * @Author: xzhen
 * @Description: 使用并行流过滤
 */
public class Parallel {

    private static boolean isPrime(int number) {
        int temp = number;
        if (temp < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(temp); i++) {
            if (temp % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
//        long l = System.currentTimeMillis();
//        long count = IntStream.range(1, 100000).filter(Parallel::isPrime).count();
        long e = System.currentTimeMillis();
//        System.out.println(count + " , time: " + (e - l) + "ms");
        long count1 = IntStream.range(1, 100000).parallel().filter(Parallel::isPrime).count();
        long a = System.currentTimeMillis();
        System.out.println(count1 + " , time: " + (a - e) + "ms");
        /**
         * 使用并行流过滤
         * parallel
         *
         */
    }
}
