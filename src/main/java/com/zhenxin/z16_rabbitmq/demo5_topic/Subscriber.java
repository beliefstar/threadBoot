package com.zhenxin.z16_rabbitmq.demo5_topic;

import com.rabbitmq.client.*;
import com.zhenxin.z16_rabbitmq.Consumer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * @author xzhen
 * @created 15:59 26/01/2019
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
            String[] routeKeys = new String[]{"#.info.#", "#.sys.#"};

            channel.queueDeclare("my_queue", true, false, false, null);

            for (String key : routeKeys) {
                channel.queueBind("my_queue", Publisher.exchangeName, key);
                System.out.println("listen to " + key);
            }

//            channel.basicConsume(queue, true, new DeliverCallback() {
//                @Override
//                public void handle(String consumerTag, Delivery message) throws IOException {
//                    String msg = new String(message.getBody(), StandardCharsets.UTF_8);
//                    System.out.println("1: ");
//                    consumer.receive(msg);
//                }
//            }, consumerTag -> {});

            channel.basicConsume("my_queue", false, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, StandardCharsets.UTF_8);
                    consumer.receive(msg);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            });
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
