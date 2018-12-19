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
		 * 读取没锁
		 * 写入只锁了操作的节点对象
		 */
		Map<String, String> map = new ConcurrentHashMap<>();
		/**
		 * CAS算法
		 * 无锁实现
		 * 看懂源码！！！
		 */
		Queue<String> queue = new ConcurrentLinkedQueue<>();
		/**
		 * 高效读取
		 * 对数组的修改先复制原数组，在复制的数组中修改数据，再替换原数组
		 */
		List<String> list = new CopyOnWriteArrayList<>();
		/**
		 * 空间换时间的算法
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
