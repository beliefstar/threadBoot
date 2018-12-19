package z02_thread_api;
/**
 * wait��notify�����ڶ�������ʹ��ʱ����Ҫ����Ӧ�Ķ������������
 * @author zhenxin
 *
 */
public class D3_Wait_Notify2 {
	public static Object object = new Object();
	
	public static class T1 implements Runnable {
		@Override
		public void run() {
			synchronized(object) {
				System.out.println("���õ����ˣ���Ҫ��ʼ������");
				try {
					Thread.sleep(2000);
					System.out.println("�����ˣ�����Ϣһ�ᣬ�ǵý����ң�");
					object.wait();				//�ȴ������Ѻ�Ҫ�����õ����ż�������Ĺ���
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("�����ˣ���������");
				System.out.println("����������");
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new T1());
		t1.start();
		Thread.sleep(4000);
		synchronized (object) {
			System.out.println("���Ѱ�������������");
			object.notify();
		}
	}
}
