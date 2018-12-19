package z04_threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class D1_ThreadPool {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ExecutorService pool = null;
		//固定数量的线程池
		pool = Executors.newFixedThreadPool(10);
		//只有一个线程的线程池
		pool = Executors.newSingleThreadExecutor();
		//一开始没有线程，来一个创建一个，优先使用空闲线程，线程数量上限为Integer.MAX_VALUE
		pool = Executors.newCachedThreadPool();
		//周期性执行任务
		ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(10);
	}
}
