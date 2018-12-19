package demo;

import java.util.concurrent.atomic.*;

public class AtomicReferenceDemo {

    public static final AtomicReference<Integer> money = new AtomicReference<>(100);

    static class MoneyTask implements Runnable {
        private boolean isGet;

        public MoneyTask(boolean isGet) {
            this.isGet = isGet;
        }

        @Override
        public void run() {
            for (;;) {
                Integer m = money.get();
                if (isGet) {
                    if (money.compareAndSet(m, m - 10)) {
                        System.out.println("消费成功！当前余额：" + money.get());
                        break;
                    }
                } else {
                    if (money.compareAndSet(m, m + 10)) {
                        System.out.println("充值成功！当前余额：" + money.get());
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = new Thread[100];
        for (int i = 0; i < 100; i++) {
            ts[i] = new Thread(new MoneyTask(i >= 50));
            ts[i].start();
        }
        for (int i = 0; i < 100; i++) {
            ts[i].join();
        }
        System.out.println("the end value:" + money.get());
    }
}
