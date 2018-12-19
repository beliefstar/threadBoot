package z04_threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class D5_ThreadPoolExtend {
	public static void main(String[] args) {
		ExecutorService pool = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(), new ThreadPoolExecutor.AbortPolicy()) {
					@Override
					protected void beforeExecute(Thread t, Runnable r) {
						System.out.println("执行之前：--" + r);
					}
					@Override
					protected void afterExecute(Runnable r, Throwable t) {
						System.out.println("执行结束：--" + r);
					}
					@Override
					protected void terminated() {
						System.out.println("线程池关闭");
					}
		};
		for (int i = 0; i < 5; i++) {
			pool.execute(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
		pool.shutdown();
	}
}
