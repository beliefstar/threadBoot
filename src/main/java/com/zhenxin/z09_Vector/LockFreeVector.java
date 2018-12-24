package com.zhenxin.z09_Vector;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * ���� -> ��ά���� [30 * ^]
 *
 * 0 ��Ͱ������Ϊ 8
 * 1 ��Ͱ������Ϊ 8 * 2
 * 2 ��Ͱ������Ϊ 8 * 2 * 2
 *      .
 *      .
 *      .
 * 29��Ͱ������Ϊ 8 * 2^28 --> 2^31
 * һ�������� --> 2^3 + 2^4 + ... + 2^31
 *
 * @param <E>
 */
public class LockFreeVector<E> {
    public static final boolean debug = true;
    /**
     * ʹ�ö�ά����
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
     * ��һ��Ͱ
     * 8 : 0000 0000 0000 0000 0000 0000 0000 1000   ->  32
     * ǰ�� 0 �� 28 ��
     *
     * ����
     * (С��8��Ԫ��) ���� 8 С�� 16 (�����λ) -> ǰ�� 0 �� 28 ��
     *
     * ( 8 <= size < 16) ǰ�� 0 �� 28 �� [ �� 0 ��Ͱ ]
     * (16 <= size < 32) ǰ�� 0 �� 27 �� [ �� 1 ��Ͱ ]
     * (32 <= size < 64) ǰ�� 0 �� 26 �� [ �� 2 ��Ͱ ]
     *              .
     *              .
     *              .
     * ���Ե�ǰ�� 28 - ((size + 8)��ǰ�� 0 ) ���Ϊ��ǰ�� Ͱ���±�
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
            int bucketInd = zeroNumFirst - zeroNumPos;//Ͱ���±�

            if (buckets.get(bucketInd) == null) {
                /**
                 * ��ǰͰΪ null ,��ʼ��...
                 * ����Ϊ��һ���ĳ��� * 2
                 */
                int newLen = 2 * buckets.get(bucketInd - 1).length();
                if (debug)
                    System.out.println("new Lenth:" + newLen);
                buckets.compareAndSet(bucketInd, null, new AtomicReferenceArray<>(newLen));
            }
            /**
             * ����
             * size: 10   ->  0000 0000 0000 0000 0000 0000 0000 1010
             * 0x80000000 ->  1000 0000 0000 0000 0000 0000 0000 0000
             * >>> 27     ->  0000 0000 0000 0000 0000 0000 0001 0000
             * ^ (pos: 18 ->  0000 0000 0000 0000 0000 0000 0001 0010)
             * =              0000 0000 0000 0000 0000 0000 0000 0010   =>  2
             * �� 1 ��Ͱ �±�Ϊ 2 ��λ��
             *
             * ^ ��������������£���ͬ��Ϊ 0 ������ͬ��Ϊ 1
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
        int bucketInd = zeroNumFirst - zeroNumPos;//Ͱ��λ��
        int idx = (0x80000000 >>> zeroNumPos) ^ pos;//�±��λ��
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
