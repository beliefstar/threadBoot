package com.zhenxin.z16_rabbitmq.demo2_work;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author xzhen
 * @created 13:53 26/01/2019
 * @description TODO
 */
public class Producer implements com.zhenxin.z16_rabbitmq.Producer {

    public static final String queue = "taskQueue";
    public static final String host = "172.18.252.124";

    private ConnectionFactory factory;

    private Connection connection;

    public Producer() {
        factory = new ConnectionFactory();
        factory.setHost(host);
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

    @Override
    public void send(String msg) {
        try {
            Channel channel = getChannel();

            //申明队列  -  持久化队列
            /**
             * 队列持久化
             */
            boolean durable = true;
            channel.queueDeclare(queue, durable, false, false, null);

            // 持久化消息
            /**
             * MessageProperties.PERSISTENT_TEXT_PLAIN 消息持久化
             */
            channel.basicPublish("", queue,
                    new AMQP.BasicProperties().builder()
                            .contentType("application/json")
                            .deliveryMode(2)
                            .build(),
                    msg.getBytes(StandardCharsets.UTF_8));

            channel.confirmSelect();

            ConfirmListener confirmListener = channel.addConfirmListener(new ConfirmCallback() {
                @Override
                public void handle(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("---------------");
                    System.out.println(deliveryTag);
                    System.out.println("---------------");
                }
            }, (a, b) -> {
            });

            channel.waitForConfirms();

            System.out.println("  >>> 发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Producer producer = new Producer();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            producer.send(line);
        }
    }
}
