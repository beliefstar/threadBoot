package z04_threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class D4_ThreadFactory {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ExecutorService pool = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<>(), 
				new ThreadFactory(){
					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(r);
						System.out.println("create : " + t.getName());
						return null;
					}
				});
		
	}
}
