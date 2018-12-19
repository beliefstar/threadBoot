package z02_thread_api;

/**
 * �ж�
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
					//�����ж��쳣���������жϱ��(wait)
					System.out.println("������");
					Thread.currentThread().interrupt();//���¼��ϱ��
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
