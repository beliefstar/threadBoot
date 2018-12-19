package z02_thread_api;

/**
 * 已经废弃，会造成数据不一致，推荐使用中断
 * @author zhenxin
 *
 */
public class D1_Stop {
	public static class WorkThread extends Thread {
		int i = 0;
		boolean stopme = false;
		public void stopMe() {
			stopme = true;
		}
		@Override
		public void run() {
			while (true) {
				if (stopme) {
					break;
				}
				System.out.println("the Thread is working..."+(++i));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		WorkThread t1 = new WorkThread();
		t1.start();
		Thread.sleep(2500);
		System.out.println("t1 was stop!");
		t1.stopMe();
		//t1.stop();
	}
}
