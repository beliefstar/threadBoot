package z03_thread_kit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
/**
 * tryLock����������ȴ�ʱ�䣬Ҳ���Բ���������ֱ���ж��ܷ��õ���
 * @author zhenxin
 *
 */
public class D3_ReentrantLock_TryLock implements Runnable {
	public static ReentrantLock lock = new ReentrantLock();
	@Override
	public void run() {
		String tName = Thread.currentThread().getName();
		try {
			System.out.println(tName + "��Ҫ����");
			if (lock.tryLock(5, TimeUnit.SECONDS)) {
				System.out.println(tName + "�õ����ˣ���ʼ����");
				Thread.sleep(6000);
				lock.unlock();
			} else {
				System.out.println(tName + "�Ȳ����ˣ��ò���������Ҫ����");
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
