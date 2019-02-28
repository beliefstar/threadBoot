package com.zhenxin.z16_rabbitmq.demo3_publish_subscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author xzhen
 * @created 14:38 26/01/2019
 * @description
 *
 * 【发布、订阅】
 *
 * 发布的日志消息将被广播给所有接收者
 *
 */
public class Publisher {

    public static final String host = "172.18.252.124";

    public static final String exchangeName = "logs";

    private Connection connection;

    public Publisher() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void publish(String msg) {
        try {
            Channel channel = connection.createChannel();

            /**
             * 定义一个交换机 exchange
             */
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);

            //向交换机 exchange 发送数据
            channel.basicPublish(exchangeName, "", null, msg.getBytes(StandardCharsets.UTF_8));

            System.out.println(" >>> send success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Publisher publisher = new Publisher();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            publisher.publish(line);
        }
    }
}
