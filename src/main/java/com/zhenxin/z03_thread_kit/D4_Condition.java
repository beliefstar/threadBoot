package z03_thread_kit;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 作用和wait、notify
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
			System.out.println("开始工作");
			Thread.sleep(2000);
			System.out.println("工作累了，我休息一会，记得叫醒我");
			
			condition.await();
			
			System.out.println("我醒了    继续工作");
			Thread.sleep(2000);
			System.out.println("工作完成！");
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
		System.out.println("你醒醒啊,起来工作啦");
		
		lock.lock();
		
		condition.signal();
		
		lock.unlock();
	}
}
