package com.zhenxin.z16_rabbitmq.demo2_work;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * @author xzhen
 * @created 14:03 26/01/2019
 * @description TODO
 */
public class Consumer {

    private ConnectionFactory factory;
    private Connection connection;

    public Consumer() {
        factory = new ConnectionFactory();
        factory.setHost(Producer.host);
        factory.setUsername("zhenxin");
        factory.setPassword("123456");
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private Channel getChannel() {
        try {
            return connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取通道失败");
        }
    }

    private void receive(com.zhenxin.z16_rabbitmq.Consumer consumer) {
        try {
            Channel channel = getChannel();

            channel.queueDeclare(Producer.queue, true, false, false, null);

            /**
             * 公平派遣
             * 在处理并确认前一个消息之前，不要向工作人员发送新消息。相反，它会将它发送给下一个仍然很忙的工人。
             * prefetchCount : 可以同时处理的消息数量
             */
            int prefetchCount = 1;
            channel.basicQos(prefetchCount);

            /**
             * 关闭自动通知, 在处理完消息后再通知
             */
            boolean autoAck = false;
            channel.basicConsume(Producer.queue, autoAck, new DeliverCallback() {
                @Override
                public void handle(String consumerTag, Delivery message) throws IOException {
                    String msg = new String(message.getBody(), StandardCharsets.UTF_8);
                    try {
                        // doWork
                        consumer.receive(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                    }
                }
            }, consumerTag -> {});

            channel.confirmSelect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        consumer.receive(msg -> {
            System.out.println("...");
            try {
                Thread.sleep(1000 * 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(msg);
        });

        LockSupport.park();
    }
}
