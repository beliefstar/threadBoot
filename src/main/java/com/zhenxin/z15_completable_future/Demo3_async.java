package com.zhenxin.z15_completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Created: 21:33 23/12/2018
 * @Author: xzhen
 * @Description:
 *
 * supplyAsync   有返回值
 * runAsync      没有返回值
 *
 * exceptionally 异常处理
 */
public class Demo3_async {
    private static Integer cal(Integer integer) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis());
        return integer * integer / 0;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> cal(50))
//                .exceptionally(e -> {
//                    System.out.println(e.getMessage());
//                    return 0;
//                })
                .thenApply(i -> (i - 500))

                .exceptionally(e -> {
                    System.out.println(e.getMessage());
                    return 0;
                })

                .thenApply(i -> Integer.toString(i))
                .thenApply(i -> "+" + (9/0) + "+")

                .exceptionally(e -> {
                    System.out.println(e.getMessage());
                    return "oo";
                });

//                .thenAccept(System.out::println);

        System.out.println("main:" + System.currentTimeMillis());
        System.out.println("return: " + future.get());

        CompletableFuture<Void> async = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("-----------");
        });
        System.out.println("++++++++++++++");
        Void aVoid = async.get();
        System.out.println(aVoid);
    }
}
