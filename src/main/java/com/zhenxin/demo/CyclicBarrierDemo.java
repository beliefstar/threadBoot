package demo;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Created: 16:08 16/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class CyclicBarrierDemo {

    private static AtomicInteger goods = new AtomicInteger(0);

    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(10, new Factory());

    private static class Productor implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(new Random().nextInt(10) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = goods.incrementAndGet();
                System.out.println("生产了一个商品,还剩：" + i);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Factory implements Runnable {

        @Override
        public void run() {
            int goodsNumber = goods.getAndSet(0);
            System.out.println("出厂: " + goodsNumber);
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            pool.execute(new Productor());
        }
//        pool.shutdown();
    }
}
