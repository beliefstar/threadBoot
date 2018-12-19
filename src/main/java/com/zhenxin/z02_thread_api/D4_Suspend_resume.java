package z02_thread_api;
/**
 * 使用suspend方法是不会释放在对象的锁，
 * 会影响到其他要访问被的对象
 * 所以使用wait和notify
 * @author zhenxin
 *
 */
public class D4_Suspend_resume {
	private static Object object = new Object();
	public static class T1 extends Thread {
		private boolean suspendme = false;
		private Object suspendobj = new Object();
		public void suspendMe(){
			suspendme = true;
		}
		public void resumeMe(){
			synchronized (suspendobj) {
				suspendobj.notify();
			}
			suspendme = false;
		}
		@Override
		public void run() {
			while (true) {
				synchronized (suspendobj) {
					while (suspendme) {
						try {
							suspendobj.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				synchronized (object) {
					System.out.println("T1 is working...");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Thread.yield();
			}
		}
	}
	public static class T2 extends Thread {
		@Override
		public void run() {
			while (true) {
				synchronized (object) {
					System.out.println("T2 is working...");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Thread.yield();
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		T1 t1 = new T1();
		T2 t2 = new T2();
		t1.start();
		t2.start();
		Thread.sleep(2500);
		System.out.println("T1 is suspend!!");
		//t1.suspend();
		t1.suspendMe();
		Thread.sleep(3000);
		System.out.println("T1 is resume!!");
		//t1.resume();
		t1.resumeMe();
	}
}
