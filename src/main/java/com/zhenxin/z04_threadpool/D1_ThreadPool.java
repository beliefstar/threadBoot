package z04_threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class D1_ThreadPool {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ExecutorService pool = null;
		//�̶��������̳߳�
		pool = Executors.newFixedThreadPool(10);
		//ֻ��һ���̵߳��̳߳�
		pool = Executors.newSingleThreadExecutor();
		//һ��ʼû���̣߳���һ������һ��������ʹ�ÿ����̣߳��߳���������ΪInteger.MAX_VALUE
		pool = Executors.newCachedThreadPool();
		//������ִ������
		ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(10);
	}
}
