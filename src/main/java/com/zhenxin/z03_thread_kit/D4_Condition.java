package z03_thread_kit;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/**
 * ���ú�wait��notify
 * @author zhenxin
 *
 */
public class D4_Condition implements Runnable{
	public static ReentrantLock lock = new ReentrantLock();
	public static Condition condition = lock.newCondition();
	@Override
	public void run() {
		try {
			lock.lockInterruptibly();
			System.out.println("��ʼ����");
			Thread.sleep(2000);
			System.out.println("�������ˣ�����Ϣһ�ᣬ�ǵý�����");
			
			condition.await();
			
			System.out.println("������    ��������");
			Thread.sleep(2000);
			System.out.println("������ɣ�");
		} catch (InterruptedException e) {
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new D4_Condition());
		t1.start();
		Thread.sleep(4000);
		System.out.println("�����Ѱ�,����������");
		
		lock.lock();
		
		condition.signal();
		
		lock.unlock();
	}
}
