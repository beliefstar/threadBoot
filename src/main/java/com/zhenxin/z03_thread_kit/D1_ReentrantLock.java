package z03_thread_kit;

import java.util.concurrent.locks.ReentrantLock;

public class D1_ReentrantLock implements Runnable{
	public static ReentrantLock lock = new ReentrantLock();
	@Override
	public void run() {
		String tName = Thread.currentThread().getName();
		System.out.println(tName + "我要锁...");
		lock.lock();
		System.out.println(tName + "我拿到锁了，我要开始工作了");
		try {
			Thread.sleep(2000);
			System.out.println(tName + "工作结束");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	public static void main(String[] args) {
		Thread t1 = new Thread(new D1_ReentrantLock());
		Thread t2 = new Thread(new D1_ReentrantLock());
		t1.start();
		t2.start();
	}
}
