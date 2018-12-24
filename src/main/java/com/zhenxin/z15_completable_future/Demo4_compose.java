package com.zhenxin.z15_completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Created: 22:18 23/12/2018
 * @Author: xzhen
 * @Description:
 *
 *  thenCompose\thenCombine  组合多个CompletableFuture
 */
public class Demo4_compose {
    private static Integer cal(int i) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i * i;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> cal(20))
//                .thenComposeAsync((i) -> CompletableFuture.supplyAsync(() -> cal(i)))
                .thenCompose(i -> CompletableFuture.supplyAsync(() -> cal(i)))
                .thenApply(str -> "(" + str + ")")
                .thenAccept(System.out::println);
        future.get();


        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> cal(20));
        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> cal(10));

        future2.thenCombine(future3, (i, j) -> i + j)
                .thenApply(i -> "(" + i + ")")
                .thenAccept(System.out::println);

        future2.get();
    }
}
