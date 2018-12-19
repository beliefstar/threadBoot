package z06_concurrent_container;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class D2_Concurrent {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		/**
		 * ��ȡû��
		 * д��ֻ���˲����Ľڵ����
		 */
		Map<String, String> map = new ConcurrentHashMap<>();
		/**
		 * CAS�㷨
		 * ����ʵ��
		 * ����Դ�룡����
		 */
		Queue<String> queue = new ConcurrentLinkedQueue<>();
		/**
		 * ��Ч��ȡ
		 * ��������޸��ȸ���ԭ���飬�ڸ��Ƶ��������޸����ݣ����滻ԭ����
		 */
		List<String> list = new CopyOnWriteArrayList<>();
		/**
		 * �ռ任ʱ����㷨
	     *
	     * Head nodes          Index nodes
	     * +-+    right        +-+                      +-+
	     * |2|---------------->| |--------------------->| |->null
	     * +-+                 +-+                      +-+
	     *  | down              |                        |
	     *  v                   v                        v
	     * +-+            +-+  +-+       +-+            +-+       +-+
	     * |1|----------->| |->| |------>| |----------->| |------>| |->null
	     * +-+            +-+  +-+       +-+            +-+       +-+
	     *  v              |    |         |              |         |
	     * Nodes  next     v    v         v              v         v
	     * +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+
	     * | |->|A|->|B|->|C|->|D|->|E|->|F|->|G|->|H|->|I|->|J|->|K|->null
	     * +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+
	     *
		 */
		Map<String, String> skipMap = new ConcurrentSkipListMap<>();
	}
}
