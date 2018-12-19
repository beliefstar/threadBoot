package demo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.ReentrantLock;

public class AtomicReferenceFieldUpdaterDemo {
    static class Condition{
        int id;
        volatile Integer score = 0;
    }

    public static AtomicReferenceFieldUpdater<Condition, Integer> updater =
            AtomicReferenceFieldUpdater.newUpdater(Condition.class, Integer.class, "score");

    public static AtomicInteger allScore = new AtomicInteger();
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        final Condition condition = new Condition();
        Thread[] ts = new Thread[10000];

        long oldSpan = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            ts[i] = new Thread() {
                @Override
                public void run() {
                    for (;;) {
                        Integer m = updater.get(condition);
                        if (updater.compareAndSet(condition, m, m + 10)) {
                            break;
                        }
                    }
                    //allScore.addAndGet(10);
                }
            };
            ts[i].start();
        }
        for (int i = 0; i < 10000; i++) {
            ts[i].join();
        }
        long timeStamp = System.currentTimeMillis() - oldSpan;
        System.out.println("FieldUpdater:" + condition.score);
        System.out.println("Atomic time:" + timeStamp);

        System.out.println("-********************-");

        oldSpan = System.currentTimeMillis();
        condition.score = 0;
        for (int i = 0; i < 10000; i++) {
            ts[i] = new Thread() {
                @Override
                public void run() {
                    lock.lock();
                    condition.score += 10;
                    lock.unlock();
                }
            };
            ts[i].start();
        }
        for (int i = 0; i < 10000; i++) {
            ts[i].join();
        }
        timeStamp = System.currentTimeMillis() - oldSpan;
        System.out.println("lock:" + condition.score);
        System.out.println("lock time:" + timeStamp);
    }

}
