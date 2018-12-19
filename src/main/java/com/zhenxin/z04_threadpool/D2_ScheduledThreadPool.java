package z04_threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class D2_ScheduledThreadPool {
	public static void main(String[] args) {
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);
		
		pool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				System.out.println(System.currentTimeMillis() / 1000);
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 0, 2, TimeUnit.SECONDS);
		
		pool.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				System.out.println(System.currentTimeMillis() / 1000);
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 0, 2, TimeUnit.SECONDS);
	}
}
