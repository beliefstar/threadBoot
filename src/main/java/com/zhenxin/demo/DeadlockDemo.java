package demo;

import java.util.concurrent.TimeUnit;

/**
 * @Created: 17:02 16/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class DeadlockDemo {

    private static Object object1 = new Object();
    private static Object object2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (object1) {
                System.out.println("哲学家1拿到了一把叉子 , 要去拿另一个叉子了");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (object2) {
                    System.out.println("哲学家1开始吃饭.。。");
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (object2) {
                System.out.println("哲学家2拿到了一把叉子 , 要去拿另一个叉子了");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (object1) {
                    System.out.println("哲学家2开始吃饭.。。");
                }
            }
        });
        thread1.setName("哲学家1");
        thread2.setName("哲学家2");
        thread1.start();
        thread2.start();

        /*
        * CMD
        * jps 查看线程
        * jstack [ID]
        * */
    }
}
