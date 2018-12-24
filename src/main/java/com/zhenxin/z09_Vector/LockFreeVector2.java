package com.zhenxin.z09_Vector;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class LockFreeVector2<E> {
    private final int N_BUCKETS = 30;
    private final int FIRST_BUCKET_SIZE = 8;

    private final int zeroNumFirst = 28;
    private AtomicInteger size;

    private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets;

    public LockFreeVector2() {
        buckets = new AtomicReferenceArray<>(N_BUCKETS);
        buckets.set(0, new AtomicReferenceArray<>(FIRST_BUCKET_SIZE));
        size = new AtomicInteger();
    }

    public void add(E e) {
        int bucketIndex;
        int idx;
        do {

            int pos = size.get() + FIRST_BUCKET_SIZE;
            int zeroNumPos = Integer.numberOfLeadingZeros(pos);
            bucketIndex = zeroNumFirst - zeroNumPos;

            if (buckets.get(bucketIndex) == null) {
                int len = buckets.get(bucketIndex - 1).length() * 2;
                System.out.println("new Length: " + len);
                buckets.compareAndSet(bucketIndex, null, new AtomicReferenceArray<>(len));
            }

            idx = (0x80000000 >>> zeroNumPos) ^ pos;

        } while (!buckets.get(bucketIndex).compareAndSet(idx, null, e));
        size.incrementAndGet();
    }

    public E get(int index) {
        int pos = index + FIRST_BUCKET_SIZE;
        int zeroNumPos = Integer.numberOfLeadingZeros(pos);
        int bucketIndex = zeroNumFirst - zeroNumPos;
        int idx = (0x80000000 >>> zeroNumPos) ^ pos;
        return buckets.get(bucketIndex).get(idx);
    }

    public int size() {
        return size.get();
    }
}
