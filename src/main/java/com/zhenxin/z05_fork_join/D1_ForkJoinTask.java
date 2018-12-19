package z05_fork_join;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class D1_ForkJoinTask {
	static class Demo extends RecursiveTask<Long> {
		private static final long serialVersionUID = 1L;
		private static final int THRESHOLD = 10000;
		private long start;
		private long end;
		public Demo(long s, long e) {
			this.start = s;
			this.end = e;
		}
		@Override
		protected Long compute() {
			boolean flag = (end - start) < THRESHOLD;
			long sum = 0;
			if (flag) {
				for (long i = start; i <= end; i++) {
					sum += i;
				}
			} else {
				long step = (start + end) / 100;
				ArrayList<Demo> tasks = new ArrayList<>();
				long left = start;
				for (int i = 0; i < 100; i++) {
					long right = left + step;
					if (right > end) {
						right = end;
					}
					Demo task = new Demo(left, right);
					tasks.add(task);
					right += step + 1;
					task.fork();
				}
				for (Demo demo : tasks) {
					sum += demo.join();
				}
			}
			return sum;
		}
	}
	public static void main(String[] args) {
		ForkJoinPool pool = new ForkJoinPool();
		Demo task = new Demo(0, 200000L);
		ForkJoinTask<Long> result = pool.submit(task);
		try {
			long rel = result.get();
			System.out.println(rel);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		long sum = 0;
		for (long i = 0; i < 200000L; i++) {
			sum += i;
		}
		System.out.println("--" + sum);
	}
}
