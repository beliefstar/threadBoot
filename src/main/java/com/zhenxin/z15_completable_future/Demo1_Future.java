package com.zhenxin.z15_completable_future;

import java.util.concurrent.*;

/**
 * @Created: 21:48 23/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Demo1_Future implements Callable<Integer> {

    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    int num;
    public Demo1_Future(int n) {
        this.num = n;
    }

    @Override
    public Integer call() throws Exception {
        TimeUnit.SECONDS.sleep(2);

        return num * 2;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main1: " + System.currentTimeMillis());
        Future<Integer> submit = pool.submit(new Demo1_Future(2));
        System.out.println("main2: " + System.currentTimeMillis());
        Integer integer = submit.get();
        System.out.println("main3: " + integer);
    }

}
