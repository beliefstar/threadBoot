package z03_thread_kit;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * ¶ÁÐ´Ëø
 * ¶Á¶Á²»»¥³â£¬ÆäËû¶¼»¥³â
 * @author zhenxin
 *
 */
public class D6_ReadWriteLock {
	private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private static Lock readLock = rwLock.readLock();
	private static Lock writeLock = rwLock.writeLock();
	static int i;
	static D6_ReadWriteLock instance = new D6_ReadWriteLock();
	public void readOparetion(Lock lock) {
		lock.lock();
		System.out.println("¶Á²Ù×÷£º"+i);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lock.unlock();
	}
	public void writeOparetion(Lock lock) {
		lock.lock();
		i = new Random().nextInt();
		System.out.println("Ð´²Ù×÷£º"+i);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lock.unlock();
	}
	public static void main(String[] args) {
		Thread rt = new Thread(() -> instance.readOparetion(readLock));
		Thread wt = new Thread(() -> instance.writeOparetion(writeLock));
		ExecutorService pool = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 20; i++) {
			if (i < 18) {
				pool.execute(rt);
			} else {
				pool.execute(wt);
			}
		}
		pool.shutdown();
		
	}
}
