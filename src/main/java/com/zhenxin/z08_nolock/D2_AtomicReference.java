package z08_nolock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class D2_AtomicReference {
    public static final AtomicStampedReference<Integer> money = new AtomicStampedReference<>(100, 0);
    static class MoneyTask implements Runnable {
        private boolean isGet;

        public MoneyTask(boolean model) {
            this.isGet = model;
        }

        @Override
        public void run() {
            for (;;) {
                Integer m = money.getReference();
                int tamp = money.getStamp();
                if (isGet) {
                    if (money.compareAndSet(m, m - 10, tamp, tamp + 1)) {
                        System.out.println("消费成功！当前余额：" + money.getReference());
                        break;
                    }
                } else {
                    if (money.compareAndSet(m, m + 10, tamp, tamp + 1)) {
                        System.out.println("充值成功！当前余额：" + money.getReference());
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(new MoneyTask(i >= 10)).start();
        }
    }
}
