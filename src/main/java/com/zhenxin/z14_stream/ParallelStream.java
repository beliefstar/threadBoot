package com.zhenxin.z14_stream;

import java.util.ArrayList;
import java.util.Random;

/**
 * @Created: 23:24 22/12/2018
 * @Author: xzhen
 * @Description: 从集合中得到并行流
 */
public class ParallelStream {
    static class Student {
        private int score;

        public Student() {
            score = new Random().nextInt(100);
        }

        public int getScore() {
            return score;
        }
    }

    static interface StudentFactory<T extends Student> {
        T create();
    }

    public static void main(String[] args) {
        ArrayList<Student> arrayList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            arrayList.add(new Student());
        }

        StudentFactory aNew = Student::new;
        Student student = aNew.create();


//        long s = System.currentTimeMillis();
//        double asDouble = arrayList.stream().mapToInt(Student::getScore).average().getAsDouble();
//        long l = System.currentTimeMillis();
//        System.out.println(asDouble + ", time: " + (l - s) + "ms");
//
//        double asDouble1 = arrayList.parallelStream().mapToInt(Student::getScore).average().getAsDouble();
        long ls = System.currentTimeMillis();
//        System.out.println(asDouble1 + ", time: " + (ls - l) + "ms");

        double asDouble2 = arrayList.stream().parallel().mapToInt(Student::getScore).average().orElse(0);
        long lss = System.currentTimeMillis();
        System.out.println(asDouble2 + ", time: " + (lss - ls) + "ms");

        /**
         * 从集合中得到并行流
         *
         * parallelStream
         *
         */
    }
}
