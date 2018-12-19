package z07_threadlocal;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class D1_ThreadLocal {
	public static final int THREAD_COUNT = 4;
	public static final int GEN_COUNT = 10000000;
	public static Random rnd = new Random(123);
	public static final ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
	
	static ThreadLocal<Random> tl = new ThreadLocal<Random>() {
		protected Random initialValue() {
			return new Random(123);
		};
	};
	static class RndTask implements Callable<Long> {
		int mode = 0;
		public RndTask(int m) {
			mode = m;
		}
		private Random getRandom() {
			if (mode == 0) {
				return rnd;
			} else if (mode == 1) {
				return tl.get();
			} else {
				return null;
			}
		}
		@Override
		public Long call() throws Exception {
			long d = System.currentTimeMillis();
			for (int i = 0; i < GEN_COUNT; i++) {
				getRandom().nextInt();
			}
			long e = System.currentTimeMillis() - d;
			System.out.println(Thread.currentThread().getName() + " speed " + e);
			return e;
		}
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		Future<Long>[] futs = new Future[THREAD_COUNT];
		for (int i = 0; i < THREAD_COUNT; i++) {
			futs[i] = pool.submit(new RndTask(0));
		}
		long rel = 0;
		for (int i = 0; i < THREAD_COUNT; i++) {
			rel += futs[i].get();
		}
		System.out.println("多线程使用同一个random实例 ， 时间为：" + rel);
		rel = 0;
		for (int i = 0; i < THREAD_COUNT; i++) {
			futs[i] = pool.submit(new RndTask(1));
		}
		for (int i = 0; i < THREAD_COUNT; i++) {
			rel += futs[i].get();
		}
		System.out.println("使用ThreadLocal包装Random，时间为：" + rel);
	}
}












