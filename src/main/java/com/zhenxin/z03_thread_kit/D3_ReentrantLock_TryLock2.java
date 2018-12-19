package z03_thread_kit;

import java.util.concurrent.locks.ReentrantLock;
/**
 * tryLock可以设参数等待时间，也可以不传参数，直接判断能否拿到锁
 * @author zhenxin
 *
 */
public class D3_ReentrantLock_TryLock2 {
	public static ReentrantLock lock1 = new ReentrantLock();
	public static ReentrantLock lock2 = new ReentrantLock();
	public static class T1 implements Runnable{
		@Override
		public void run() {
			String tName = Thread.currentThread().getName();
			while (true) {
				if (lock1.tryLock()) {
					try{
						System.out.println(tName + "拿到锁1了，要去拿锁2");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (lock2.tryLock()) {
							try{
								System.out.println(tName + "拿到锁2，开始工作");
								
								break;
							} finally {
								lock2.unlock();
							}
						}
					} finally {
						lock1.unlock();
					}
				}
				Thread.yield();
			}
		}
	}
	public static class T2 implements Runnable{
		@Override
		public void run() {
			String tName = Thread.currentThread().getName();
			while (true) {
				if (lock2.tryLock()) {
					try{
						System.out.println(tName + "拿到锁2了，要去拿锁1");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (lock1.tryLock()) {
							try{
								System.out.println(tName + "拿到锁1，开始工作");
								
								break;
							} finally {
								lock1.unlock();
							}
						}
					} finally {
						lock2.unlock();
					}
				}
				Thread.yield();
			}
		}
	}
	public static void main(String[] args) {
		Thread t1 = new Thread(new T1());
		Thread t2 = new Thread(new T2());
		t1.start();
		t2.start();
	}
}
