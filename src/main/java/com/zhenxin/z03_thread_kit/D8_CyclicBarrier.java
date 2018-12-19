package z03_thread_kit;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 循环栅栏   -->  倒计数器循环起来（每达到一个数字就执行一次指定线程）
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
				System.out.println("士兵 "+i+" 报道");
				cyclic.await();
				Thread.sleep(new Random().nextInt(10) * 1000);
				System.out.println("士兵 "+i+" 任务完成！");
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
				System.out.println("司令：开始工作");
				flag = false;
			} else {
				System.out.println("司令：工作结束了，一起去吃饭");
			}
		}
	}
	public static void main(String[] args) {
		int n = 10;
		boolean flag = true;
		System.out.println(n+"个士兵，速速来集合");
		cyclic = new CyclicBarrier(n, new Commander(flag));
		for (int i = 0; i < n; i++) {
			new Thread(new Soldier(i)).start();
		}
	}
}
