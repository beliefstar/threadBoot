package z03_thread_kit;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
/**
 * ��������
 * @author zhenxin
 *
 */
public class D7_CountDownLatch {
	private static CountDownLatch latch = new CountDownLatch(10);
	static class Check implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(new Random().nextInt(10) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("xxx������");
			latch.countDown();
		}
	}
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			new Thread(new Check()).start();
		}
		latch.await();
		System.out.println("�����Ѿ�����������������");
	}
}
