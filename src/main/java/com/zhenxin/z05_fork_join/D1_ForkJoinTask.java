package com.zhenxin.z05_fork_join;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class D1_ForkJoinTask {
	static class Demo extends RecursiveTask<Long> {
		private static final long serialVersionUID = 1L;
		private static final int THRESHOLD = 5000;
		private long start;
		private long end;
		public Demo(long s, long e) {
			this.start = s;
			this.end = e;
		}
		@Override
		protected Long compute() {
			boolean flag = (end - start) <= THRESHOLD;
			long sum = 0;
			if (flag) {
				for (long i = start; i <= end; i++) {
					sum += i;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + " > start: " + start + ", end: " + end + ", sum: " + sum);
			} else {

				/** 一次全分完， 没有二次拆分 */

				ArrayList<Demo> tasks = new ArrayList<>();
				double step = end - start;
				double v = Math.ceil(step / THRESHOLD);
				long left = start;
				long right = start + THRESHOLD;
				System.out.println("分给了 " + v + " 个线程");

				for (int i = 0; i < v; i++) {
					if (right > end) {
						right = end;
					}
					Demo task = new Demo(left, right);
					tasks.add(task);

					/** 分给其他线程执行，当前线程不参与任务 */
//					task.fork();

					left = right + 1;
					right = left + THRESHOLD - 1;
				}
				/** 当前线程也会加入到任务中 */
				invokeAll(tasks);

				System.out.println("waiting...");
				for (Demo demo : tasks) {
					sum += demo.join();
				}
			}
			return sum;
		}
	}
	public static void main(String[] args) {
		ForkJoinPool pool = new ForkJoinPool();
		Demo task = new Demo(0, 40000);
		long l = System.currentTimeMillis();

		/** 阻塞等待结果 */
		Long invoke = pool.invoke(task);

		/** 异步等待结果 */
		ForkJoinTask<Long> result = pool.submit(task);
		try {
			long rel = result.get();
			long e = System.currentTimeMillis() - l;
			System.out.println(e + "ms");
			System.out.println(rel);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		long sum = 0;
		for (long i = 0; i <= 40000; i++) {
			sum += i;
		}
		System.out.println(">>" + sum);
	}
}
