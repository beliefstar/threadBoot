package com.zhenxin.z09_Vector;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * 容器 -> 二维数组 [30 * ^]
 *
 * 0 号桶的容量为 8
 * 1 号桶的容量为 8 * 2
 * 2 号桶的容量为 8 * 2 * 2
 *      .
 *      .
 *      .
 * 29号桶的容量为 8 * 2^28 --> 2^31
 * 一共的容量 --> 2^3 + 2^4 + ... + 2^31
 *
 * @param <E>
 */
public class LockFreeVector<E> {
    public static final boolean debug = true;
    /**
     * 使用二维数组
     */
    private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets;
    private final int N_BUCKET = 30;
    private final int FIRST_BUCKET_SIZE = 8;
    private final int zeroNumFirst = 28;

    private final AtomicReference<Descriptor<E>> descriptor;

    public LockFreeVector() {
        buckets = new AtomicReferenceArray<>(N_BUCKET);
        buckets.set(0, new AtomicReferenceArray<>(FIRST_BUCKET_SIZE));

        descriptor = new AtomicReference<>(new Descriptor<>(0, null));
    }

    /**
     *
     * 0 : 0000 0000 0000 0000 0000 0000 0000 0000   ->  32
     * 第一个桶
     * 8 : 0000 0000 0000 0000 0000 0000 0000 1000   ->  32
     * 前置 0 有 28 个
     *
     * 例：
     * (小于8个元素) 加上 8 小于 16 (不会进位) -> 前置 0 有 28 个
     *
     * ( 8 <= size < 16) 前置 0 有 28 个 [ 第 0 号桶 ]
     * (16 <= size < 32) 前置 0 有 27 个 [ 第 1 号桶 ]
     * (32 <= size < 64) 前置 0 有 26 个 [ 第 2 号桶 ]
     *              .
     *              .
     *              .
     * 所以当前的 28 - ((size + 8)的前置 0 ) 结果为当前的 桶的下标
     *
     *
     * @param e
     */
    public void push_back(E e) {
        Descriptor<E> desc;
        Descriptor<E> newd;
        do {
            desc = descriptor.get();
            desc.completeWrite();

            int pos = desc.size + FIRST_BUCKET_SIZE;
            int zeroNumPos = Integer.numberOfLeadingZeros(pos);
            int bucketInd = zeroNumFirst - zeroNumPos;//桶的下标

            if (buckets.get(bucketInd) == null) {
                /**
                 * 当前桶为 null ,初始化...
                 * 长度为上一个的长度 * 2
                 */
                int newLen = 2 * buckets.get(bucketInd - 1).length();
                if (debug)
                    System.out.println("new Lenth:" + newLen);
                buckets.compareAndSet(bucketInd, null, new AtomicReferenceArray<>(newLen));
            }
            /**
             * 例如
             * size: 10   ->  0000 0000 0000 0000 0000 0000 0000 1010
             * 0x80000000 ->  1000 0000 0000 0000 0000 0000 0000 0000
             * >>> 27     ->  0000 0000 0000 0000 0000 0000 0001 0000
             * ^ (pos: 18 ->  0000 0000 0000 0000 0000 0000 0001 0010)
             * =              0000 0000 0000 0000 0000 0000 0000 0010   =>  2
             * 第 1 号桶 下标为 2 的位置
             *
             * ^ 运算符：二进制下，相同的为 0 ，不相同的为 1
             */
            int idx = (0x80000000>>>zeroNumPos) ^ pos;
            newd = new Descriptor<>(desc.size + 1,
                    new WriteDescriptor<>(null, e, buckets.get(bucketInd), idx));

        } while (!descriptor.compareAndSet(desc, newd));
        descriptor.get().completeWrite();
    }

    public E get(int index) {
        int pos = index + FIRST_BUCKET_SIZE;
        int zeroNumPos = Integer.numberOfLeadingZeros(pos);
        int bucketInd = zeroNumFirst - zeroNumPos;//桶的位置
        int idx = (0x80000000 >>> zeroNumPos) ^ pos;//下标的位置
        return buckets.get(bucketInd).get(idx);
    }

    static class Descriptor<E> {
        public int size;
        volatile WriteDescriptor<E> writeop;

        public Descriptor(int size, WriteDescriptor<E> writeop) {
            this.size = size;
            this.writeop = writeop;
        }

        public void completeWrite() {
            final WriteDescriptor<E> tempop = writeop;
            if (tempop != null) {
                tempop.doIt();
                writeop = null;
            }
        }
    }

    static class WriteDescriptor<E> {
        public E oldValue;
        public E newValue;
        public AtomicReferenceArray<E> addr;
        public int addr_ind;

        public WriteDescriptor(E oldValue, E newValue, AtomicReferenceArray<E> addr, int addr_ind) {
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.addr = addr;
            this.addr_ind = addr_ind;
        }

        public void doIt() {
            addr.compareAndSet(addr_ind, oldValue, newValue);
        }
    }
}
