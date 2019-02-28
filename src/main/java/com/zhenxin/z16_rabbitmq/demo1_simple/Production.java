package com.zhenxin.z16_rabbitmq.demo1_simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zhenxin.z16_rabbitmq.Producer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author xzhen
 * @created 16:05 25/01/2019
 * @description TODO
 */
public class Production implements Producer {

    public static final String QueueName = "demo1_simple";
    public static final String host = "172.18.252.124";

    private ConnectionFactory factory;

    private Connection connection;

    @Override
    public void send(String msg) {
        try {
            Channel channel = getChannel();

            channel.queueDeclare(QueueName, false, false, false, null);

            channel.basicPublish("", QueueName, null, msg.getBytes(StandardCharsets.UTF_8));

            System.out.println(">>>> 已发送");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
//        factory.setPort();
        return factory;
    }

    private void init() {
        factory = connectionFactory();
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
        }
        throw new RuntimeException("获取连接失败");
    }

    public Production() {
        init();
    }

    public static void main(String[] args) {
        Producer producer = new Production();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            producer.send(line);
        }
    }
}
