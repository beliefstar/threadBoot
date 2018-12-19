package z02_thread_api;

/**
 * 中断
 * @author zhenxin
 *
 */
public class D2_Interrupt {
	public static class WorkThread extends Thread {
		int i = 0;
		boolean stopme = false;
		public void stopMe() {
			stopme = true;
		}
		@Override
		public void run() {
			while (true) {
				if (Thread.currentThread().isInterrupted()) {
					break;
				}
				System.out.println("the Thread is working..."+(++i));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					//捕获中断异常，会消除中断标记(wait)
					System.out.println("捕获到了");
					Thread.currentThread().interrupt();//重新加上标记
				}
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		WorkThread t1 = new WorkThread();
		t1.start();
		Thread.sleep(1500);
		System.out.println("t1 was interrupt!");
		t1.interrupt();
	}
}
