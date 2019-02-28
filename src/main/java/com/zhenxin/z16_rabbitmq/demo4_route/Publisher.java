package com.zhenxin.z16_rabbitmq.demo4_route;

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
 * @created 15:42 26/01/2019
 * @description TODO
 */
public class Publisher {

    public static final String host = "172.18.252.124";
    public static final String exchangeName = "route_demo";

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

    public void publish(String msg, String level) {
        try {
            Channel channel = connection.createChannel();

            /**
             * 声明路由模式的 exchange
             */
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true);

            channel.basicPublish(exchangeName, level, null, msg.getBytes(StandardCharsets.UTF_8));

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
            String[] split = line.split("\\s");
            publisher.publish(split[1], split[0]);
        }
    }
}
