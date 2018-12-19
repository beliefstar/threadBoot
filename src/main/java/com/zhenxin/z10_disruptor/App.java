package com.zhenxin.z10_disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ThreadFactory;

/**
 * @Created: 20:35 16/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class App {

    public static void main(String[] args) throws InterruptedException {

        PCDataFactory factory = new PCDataFactory();

        int bufferSize = 1024;

        Disruptor<PCData> disruptor = new Disruptor<>(factory,
                bufferSize,
                (ThreadFactory) Thread::new,
                ProducerType.MULTI,
                new SleepingWaitStrategy());
        /**
         * 等待策略
         * - BlockingWaitStrategy【默认】 和BlockingQueue非常类似
         * - SleepingWaitStrategy 对CPU使用率很低，循环中不断等待数据，使用yield和LockSupport.parkNanos(1) 不占用CPU 场景：异步日志
         *
         */

        disruptor.handleEventsWithWorkerPool(
                new Consumer(),
                new Consumer(),
                new Consumer(),
                new Consumer()
                );

        disruptor.start();

        RingBuffer<PCData> ringBuffer = disruptor.getRingBuffer();

        Producer producer = new Producer(ringBuffer);

        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            byteBuffer.putLong(l);
            Thread.sleep(1000);
            producer.pushData(byteBuffer);

        }
    }
}
