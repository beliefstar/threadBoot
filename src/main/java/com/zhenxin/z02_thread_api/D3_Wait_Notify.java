package z02_thread_api;
/**
 * wait��notify�����ڶ�������ʹ��ʱ����Ҫ����Ӧ�Ķ������������
 * @author zhenxin
 *
 */
public class D3_Wait_Notify {
	public static final Object object = new Object();
	
	public static class T1 extends Thread {
		@Override
		public void run() {
			synchronized(object) {
				System.out.println("T1 begin...");
				System.out.println("T1 is working...");
				try {
					System.out.println("T1 is waitting");
					object.wait();				//�ȴ������Ѻ�Ҫ�����õ����ż�������Ĺ���
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("T1 end!");
			}
			Thread.yield();
		}
	}
	public static class T2 extends Thread {
		@Override
		public void run() {
			synchronized(object) {
				System.out.println("T2 begin...");
				System.out.println("T2 is working...");
				try {
					System.out.println("T2 is notify");
					object.notify();//�����Ѻ������������
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("T2 end!");
			}
			Thread.yield();
		}
	}
	public static void main(String[] args) {
		new T1().start();
		new T2().start();
	}
}
