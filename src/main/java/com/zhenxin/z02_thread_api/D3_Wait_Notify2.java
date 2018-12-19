package z02_thread_api;
/**
 * wait和notify是用在对象上且使用时必须要给对应的对象加锁！！！
 * @author zhenxin
 *
 */
public class D3_Wait_Notify2 {
	public static Object object = new Object();
	
	public static class T1 implements Runnable {
		@Override
		public void run() {
			synchronized(object) {
				System.out.println("我拿到锁了，我要开始工作了");
				try {
					Thread.sleep(2000);
					System.out.println("我累了，我休息一会，记得叫醒我！");
					object.wait();				//等待被唤醒后要重新拿到锁才继续后面的工作
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("我醒了，继续工作");
				System.out.println("工作结束！");
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new T1());
		t1.start();
		Thread.sleep(4000);
		synchronized (object) {
			System.out.println("醒醒啊，工作啦！！");
			object.notify();
		}
	}
}
