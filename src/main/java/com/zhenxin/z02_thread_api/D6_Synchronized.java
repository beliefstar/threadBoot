package z02_thread_api;
/**
 * 不锁可变对象   ，如Inreger, String
 * @author zhenxin
 *
 */
public class D6_Synchronized implements Runnable{
	static Integer i = 0;
	static D6_Synchronized instance = new D6_Synchronized();
	@Override
	public void run() {
		for (int j = 0; j < 100000; j++) {
			synchronized (instance) {//i
				i++;
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(instance);
		Thread t2 = new Thread(instance);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println(i);
	}
}
