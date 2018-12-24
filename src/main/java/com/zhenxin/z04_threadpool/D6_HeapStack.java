package com.zhenxin.z04_threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class D6_HeapStack {
	static class DivTask implements Runnable {
		int a,b;
		public DivTask(int a, int b) {
			this.a = a;
			this.b = b;
		}
		@Override
		public void run() {
			double rel = a / b;
			System.out.println(rel);
			//Integer.parseInt(null);
		}
	}
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 5; i++) {
			//pool.submit(new DivTask(100, i));
			pool.execute(new DivTask(100, i));
		}
	}
}
