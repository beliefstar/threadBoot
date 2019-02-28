package com.zhenxin.z16_rabbitmq.demo3_publish_subscribe;

import com.rabbitmq.client.*;
import com.zhenxin.z16_rabbitmq.Consumer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * @author xzhen
 * @created 15:07 26/01/2019
 * @description TODO
 */
public class Subscriber {

    private Connection connection;

    public Subscriber() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Publisher.host);
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(Consumer consumer) {
        try {
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(Publisher.exchangeName, BuiltinExchangeType.FANOUT);

            /**
             * 声明一个临时队列 (断开连接后会自动删除, 队列名是随机字符串)
             * 接受最新消息
             */
            String queue = channel.queueDeclare().getQueue();

            channel.queueBind(queue, Publisher.exchangeName, "");

            channel.basicConsume(queue, true, new DeliverCallback() {
                @Override
                public void handle(String consumerTag, Delivery message) throws IOException {

                    String msg = new String(message.getBody(), StandardCharsets.UTF_8);
                    consumer.receive(msg);

                }
            }, consumerTag -> {});

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Subscriber subscriber = new Subscriber();
        subscriber.subscribe(msg -> {
            System.out.println(msg);
        });
        LockSupport.park();
    }
}
