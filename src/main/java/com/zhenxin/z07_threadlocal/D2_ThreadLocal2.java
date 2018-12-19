package z07_threadlocal;

import java.util.Random;
import java.util.concurrent.*;

public class D2_ThreadLocal2 {
    public static final int THREADCOUNT = 4;
    public static final int GEN_COUNT = 10000000;
    public static final Random oneRandom = new Random(123);
    public static final ExecutorService pool = Executors.newFixedThreadPool(THREADCOUNT);
    public static final ThreadLocal<Random> tl = new ThreadLocal<Random>(){
        @Override
        protected Random initialValue() {
            return new Random(123);
        }
    };
    static class RngTask implements Callable<Long> {
        private int model;

        public RngTask(int model) {
            this.model = model;
        }

        public Random getRandom() {
            if (model == 1) {
                return oneRandom;
            } else {
                return tl.get();
            }
        }

        @Override
        public Long call() {
            Long oldStamp = System.currentTimeMillis();
            for (int i = 0; i < GEN_COUNT; i++) {
                getRandom().nextInt();
            }
            Long stamp = System.currentTimeMillis() - oldStamp;
            System.out.println(Thread.currentThread().getName() + " speed " + stamp);
            return stamp;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Long>[] futs = new Future[THREADCOUNT];
        for (int i = 0; i < THREADCOUNT; i++) {
            futs[i] = pool.submit(new RngTask(1));
        }
        long timer = 0;
        for (int i = 0; i < THREADCOUNT; i++) {
            timer += futs[i].get();
        }
        System.out.println("use single random time is " + timer);
        for (int i = 0; i < THREADCOUNT; i++) {
            futs[i] = pool.submit(new RngTask(0));
        }
        timer = 0;
        for (int i = 0; i < THREADCOUNT; i++) {
            timer += futs[i].get();
        }
        System.out.println("use threadlocal random time is " + timer);
    }
}
