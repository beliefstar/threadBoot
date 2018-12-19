package z06_concurrent_container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D1_Wrap {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
		List<String> list = Collections.synchronizedList(new ArrayList<>());
	}
}
