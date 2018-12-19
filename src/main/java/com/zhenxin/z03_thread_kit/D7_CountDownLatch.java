package z03_thread_kit;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
/**
 * 倒计数器
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
			System.out.println("xxx检查完毕");
			latch.countDown();
		}
	}
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			new Thread(new Check()).start();
		}
		latch.await();
		System.out.println("所有已经就绪，出发！！！");
	}
}
