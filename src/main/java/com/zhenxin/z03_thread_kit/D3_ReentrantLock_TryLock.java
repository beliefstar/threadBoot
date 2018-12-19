package z03_thread_kit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
/**
 * tryLock可以设参数等待时间，也可以不传参数，直接判断能否拿到锁
 * @author zhenxin
 *
 */
public class D3_ReentrantLock_TryLock implements Runnable {
	public static ReentrantLock lock = new ReentrantLock();
	@Override
	public void run() {
		String tName = Thread.currentThread().getName();
		try {
			System.out.println(tName + "我要拿锁");
			if (lock.tryLock(5, TimeUnit.SECONDS)) {
				System.out.println(tName + "拿到锁了，开始工作");
				Thread.sleep(6000);
				lock.unlock();
			} else {
				System.out.println(tName + "等不及了，拿不到锁，我要走了");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
	public static void main(String[] args) {
		Thread t1 = new Thread(new D3_ReentrantLock_TryLock());
		Thread t2 = new Thread(new D3_ReentrantLock_TryLock());
		t1.start();
		t2.start();
	}
}
