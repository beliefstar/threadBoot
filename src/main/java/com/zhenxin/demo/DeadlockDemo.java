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
                System.out.println("��ѧ��1�õ���һ�Ѳ��� , Ҫȥ����һ��������");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (object2) {
                    System.out.println("��ѧ��1��ʼ�Է�.����");
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (object2) {
                System.out.println("��ѧ��2�õ���һ�Ѳ��� , Ҫȥ����һ��������");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (object1) {
                    System.out.println("��ѧ��2��ʼ�Է�.����");
                }
            }
        });
        thread1.setName("��ѧ��1");
        thread2.setName("��ѧ��2");
        thread1.start();
        thread2.start();

        /*
        * CMD
        * jps �鿴�߳�
        * jstack [ID]
        * */
    }
}
