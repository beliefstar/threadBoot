package z03_thread_kit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
/**
 * 信号量
 * 同时可以有多少个线程访问一个资源
 * @author zhenxin
 *
 */
public class D5_Semaphore implements Runnable{
	public static final Semaphore semaphore = new Semaphore(5);
	@Override
	public void run() {
		try {
			semaphore.acquire();
			Thread.sleep(500);
			System.out.println(Thread.currentThread().getId() + "Done!");
			semaphore.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(20);
		final D5_Semaphore task = new D5_Semaphore();
		for (int i = 0; i < 20; i++) {
			pool.execute(task);
		}
	}
}
