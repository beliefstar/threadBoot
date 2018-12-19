package z08_nolock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class D1_Atomic {
    public static final AtomicInteger aint = new AtomicInteger();
    public static final CountDownLatch cdl = new CountDownLatch(10);
    public static class Task implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                aint.incrementAndGet();
            }
            cdl.countDown();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(new Task()).start();
        }
        cdl.await();
        System.out.println(aint.get());
    }
}
