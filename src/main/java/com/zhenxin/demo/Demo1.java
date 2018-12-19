package demo;

public class Demo1 {
	static Integer number = 0;
	static class SynDemo implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 10000; i++) {
				synchronized (number) {
					number++;
				}
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		SynDemo demo = new SynDemo();
		Thread t1 = new Thread(demo);
		Thread t2 = new Thread(demo);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println(number);
	}
}
