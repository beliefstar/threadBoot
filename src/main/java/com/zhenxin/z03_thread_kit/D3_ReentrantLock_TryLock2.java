package z03_thread_kit;

import java.util.concurrent.locks.ReentrantLock;
/**
 * tryLock����������ȴ�ʱ�䣬Ҳ���Բ���������ֱ���ж��ܷ��õ���
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
						System.out.println(tName + "�õ���1�ˣ�Ҫȥ����2");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (lock2.tryLock()) {
							try{
								System.out.println(tName + "�õ���2����ʼ����");
								
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
						System.out.println(tName + "�õ���2�ˣ�Ҫȥ����1");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (lock1.tryLock()) {
							try{
								System.out.println(tName + "�õ���1����ʼ����");
								
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
