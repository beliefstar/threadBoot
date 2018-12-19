package z01_before;
/**
 * 32位才有效果
 * 原子性
 * @author zhenxin
 *
 */
public class Atomicity {
	public static long t;
	public Atomicity() {
		t = 0;
	}
	public void start() {
		new ChangT(111L).start();
		new ChangT(-999L).start();
		new ChangT(333L).start();
		new ChangT(-444L).start();
		new ReadT().start();
	}
	public class ChangT extends Thread {
		private long to;
		public ChangT(long c) {
			to = c;
		}
		@Override
		public void run() {
			while (true) {
				Atomicity.t = to;
				Thread.yield();
			}
		}
	}
	public class ReadT extends Thread {
		@Override
		public void run() {
			while (true) {
				long temp = Atomicity.t;
				if (temp != 111L && temp != -999L && temp != -444L && temp != 333L) {
					System.out.println(temp);
				}
				Thread.yield();
			}
		}
	}
	public static void main(String[] args) {
		System.out.println("ok");
		new Atomicity().start();
	}
}
