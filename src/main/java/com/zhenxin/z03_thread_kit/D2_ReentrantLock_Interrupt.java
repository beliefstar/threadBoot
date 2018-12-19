package z03_thread_kit;

import java.util.concurrent.locks.ReentrantLock;

public class D2_ReentrantLock_Interrupt implements Runnable {
	public static ReentrantLock lock = new ReentrantLock();
	@Override
	public void run() {
		String tName = Thread.currentThread().getName();
		try {
			System.out.println(tName + "��Ҫ����");
			lock.lockInterruptibly();
			System.out.println(tName + "�õ����ˣ���ʼ����");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println(tName + "���ж���");
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new D2_ReentrantLock_Interrupt(), "T1");
		Thread t2 = new Thread(new D2_ReentrantLock_Interrupt(), "T2");
		t1.start();
		t2.start();
		Thread.sleep(1000);
		System.out.println("�Ȳ����ˣ���Ҫ�ж�����");
		t1.interrupt();
	}
}
