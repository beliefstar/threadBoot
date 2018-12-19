package z08_nolock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class D3_AtomicFieldUpdater {
    static class Condidate {
        int id;
        volatile int score;
    }
    public static final AtomicIntegerFieldUpdater<Condidate> updater =
            AtomicIntegerFieldUpdater.newUpdater(Condidate.class, "score");

    public static final AtomicInteger allscore = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = new Thread[10000];
        Condidate condidate = new Condidate();
        for (int i = 0; i < 10000; i++) {
            ts[i] = new Thread() {
                @Override
                public void run() {
                    if (Math.random() > 0.4) {
                        updater.incrementAndGet(condidate);
                        allscore.incrementAndGet();
                    }
                }
            };
            ts[i].start();
        }
        for (int i = 0; i < 10000; i++) {
            ts[i].join();
        }
        System.out.println("score:" + condidate.score + ", validation:" + allscore.get());
    }
}
