package z03_thread_kit;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * ѭ��դ��   -->  ��������ѭ��������ÿ�ﵽһ�����־�ִ��һ��ָ���̣߳�
 * @author zhenxin
 *
 */
public class D8_CyclicBarrier {
	private static CyclicBarrier cyclic;
	static class Soldier implements Runnable {
		int i;
		public Soldier(int index) {
			this.i = index;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(new Random().nextInt(10) * 1000);
				System.out.println("ʿ�� "+i+" ����");
				cyclic.await();
				Thread.sleep(new Random().nextInt(10) * 1000);
				System.out.println("ʿ�� "+i+" ������ɣ�");
				cyclic.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	}
	static class Commander implements Runnable {
		boolean flag;
		public Commander(boolean f) {
			this.flag = f;
		}
		@Override
		public void run() {
			if (flag) {
				System.out.println("˾���ʼ����");
				flag = false;
			} else {
				System.out.println("˾����������ˣ�һ��ȥ�Է�");
			}
		}
	}
	public static void main(String[] args) {
		int n = 10;
		boolean flag = true;
		System.out.println(n+"��ʿ��������������");
		cyclic = new CyclicBarrier(n, new Commander(flag));
		for (int i = 0; i < n; i++) {
			new Thread(new Soldier(i)).start();
		}
	}
}
