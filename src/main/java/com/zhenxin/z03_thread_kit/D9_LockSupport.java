package z03_thread_kit;

import java.util.concurrent.locks.LockSupport;

public class D9_LockSupport {
	static Object object = new Object();
	static class Change implements Runnable {
		@Override
		public void run() {
			synchronized(object){
				System.out.println("in" + Thread.currentThread().getName());
				LockSupport.park();
//				Thread.currentThread().suspend();
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new Change(), "T1");
		t1.start();
		Thread t2 = new Thread(new Change(), "T2");
		t2.start();
		Thread.sleep(100);
		LockSupport.unpark(t1);
		LockSupport.unpark(t2);
//		t1.resume();
//		t2.resume();
	}
}
