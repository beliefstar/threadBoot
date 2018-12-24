package com.zhenxin.z15_completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Created: 15:51 23/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Demo2_completableFuture implements Runnable {

    private CompletableFuture<Integer> future = null;

    public Demo2_completableFuture(CompletableFuture<Integer> future) {
        this.future = future;
    }

    @Override
    public void run() {
        int tmp = 0;
        try {
            tmp = future.get() * future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(tmp);
    }

    public static void main(String[] args) throws InterruptedException {
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        new Thread(new Demo2_completableFuture(future)).start();
        TimeUnit.SECONDS.sleep(2);
        future.complete(10);
    }

}
