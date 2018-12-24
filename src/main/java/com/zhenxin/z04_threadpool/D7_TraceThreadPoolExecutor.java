package com.zhenxin.z04_threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class D7_TraceThreadPoolExecutor {
	static class TraceThreadPoolExecutor extends ThreadPoolExecutor {
		public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
				BlockingQueue<Runnable> workQueue) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}
		@Override
		public void execute(Runnable command) {
			super.execute(wrap(command, client()));
		}
		@Override
		public Future<?> submit(Runnable task) {
			return super.submit(wrap(task, client()));
		}
		private Exception client() {
			return new Exception("Client stack trace");
		}
		private Runnable wrap(Runnable task, Exception t) {
			return new Runnable() {
				@Override
				public void run() {
					try {
						task.run();
					} catch (Exception e) {
						t.printStackTrace();
						throw e;
					}
				}
			};
		}
	}
	public static void main(String[] args) {
		ExecutorService pool = new TraceThreadPoolExecutor(5, 5, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
		for (int i = 0; i < 5; i++) {
			pool.execute(new D6_HeapStack.DivTask(100, i));
		}
	}
}
