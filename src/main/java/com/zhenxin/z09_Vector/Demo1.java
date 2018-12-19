package z09_Vector;

import java.util.concurrent.CountDownLatch;

public class Demo1 {
    static CountDownLatch cdl = new CountDownLatch(200);
    public static void main(String[] args) throws InterruptedException {
        int a = 5;
//        if (a > 0) demo: {
//            System.out.println("1");
//            if (a > 5) {
//                System.out.println("2");
//            }
//            if (a > 5) {
//                System.out.println("3");
//                break demo;
//            }
//            System.out.println("end");
//        }
        //LockFreeVector<Integer> vector = new LockFreeVector<>();
        //vector.push_back(11);
        //System.out.println(vector.get(0));

        LockFreeVector2<Integer> vector = new LockFreeVector2<>();
        Thread[] ts = new Thread[200];
        for (int i = 0; i < 200; i++) {
            ts[i] = new Thread(() -> {
                    for (int j = 0; j < 200; j++) {
                        vector.add(j);
                    }
                    cdl.countDown();
            });
            ts[i].start();
        }
        cdl.await();
        System.out.println(vector.get(399));
        System.out.println(vector.size());
    }
}
