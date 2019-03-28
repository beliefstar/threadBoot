package com.zhenxin.z05_fork_join;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author xzhen
 * @created 9:49 28/03/2019
 * @description TODO
 */
public class D2_ForkJoinTask {


    static class TaskDemo extends RecursiveTask<Long> {

        private static long THRESHOLD = 10000;

        private long start;

        private long end;

        TaskDemo(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (start > end) return 0L;
            long step = end - start;
            if (step <= THRESHOLD) {
                long sum = 0;
                for (long i = start; i < end; i++) {
                    sum += i;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("%s start: %s, end: %s, sum: %s",
                        Thread.currentThread().getName(), start, end, sum));
                return sum;
            }

            /** 把任务一分为二 */
            long half = step / 2;

            TaskDemo taskDemoLeft = new TaskDemo(start, start + half);
            TaskDemo taskDemoRight = new TaskDemo(start + half + 1, end);

            /** 当前线程也会加入到任务中 */
            invokeAll(taskDemoLeft, taskDemoRight);

            System.out.println(Thread.currentThread().getName() + " waiting...");

            return taskDemoLeft.join() + taskDemoRight.join();
        }
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(4);
        TaskDemo taskDemo = new TaskDemo(0, 40000L);
        long l = System.currentTimeMillis();
        ForkJoinTask<Long> task = pool.submit(taskDemo);
        try {
            Long sum = task.get();
            long e = System.currentTimeMillis() - l;
            System.out.println("result: " + sum + ", " + e + "ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
