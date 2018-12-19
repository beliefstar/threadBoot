package com.zhenxin.z10_disruptor;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * 生产者
 *
 * @Created: 20:14 16/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Producer {

    private final RingBuffer<PCData> ringBuffer;

    public Producer(RingBuffer<PCData> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void pushData(ByteBuffer byteBuffer) {
        long sequence = ringBuffer.next();

        try {
            PCData event = ringBuffer.get(sequence);

            event.setValue(byteBuffer.getLong(0));
            System.out.println("produce: " + event.getValue());
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
