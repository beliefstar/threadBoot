package com.zhenxin.z16_rabbitmq.demo1_simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * @author xzhen
 * @created 16:52 25/01/2019
 * @description TODO
 */
public class Consumer {

    private ConnectionFactory factory;

    private Connection connection;

    private void factory() {
        factory = new ConnectionFactory();
        factory.setHost(Production.host);
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public Consumer() {
        factory();
    }

    private Channel getChannnel() {
        try {
            return connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("连接失败");
        }
    }

    public void receive(com.zhenxin.z16_rabbitmq.Consumer consumer) {
        try {
            Channel channel = getChannnel();

            channel.queueDeclare(Production.QueueName, false, false, false, null);

            channel.basicConsume(Production.QueueName, true, (consumerTag, delivery) -> {
                byte[] body = delivery.getBody();

                try {
                    consumer.receive(new String(body, StandardCharsets.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        consumer.receive(msg -> {
            System.out.println(msg);
            System.out.println("...");
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ok");
        });

        LockSupport.park();
    }
}
