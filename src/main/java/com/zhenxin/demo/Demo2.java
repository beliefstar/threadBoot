package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Demo2 {
	public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        list = list.stream().filter(item -> item > 5).collect(Collectors.toList());

        System.out.println(list);

        list.stream().flatMap(m -> {
            System.out.println(m);
            return null;
        });
    }
}
