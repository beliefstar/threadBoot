package com.zhenxin.z16_rabbitmq;

/**
 * @author xzhen
 * @created 16:06 25/01/2019
 * @description
 *
 * 【exchange】 type: direct, topic, headers and fanout
 *
 * fanout:
 *      将收到的所有消息广播到它知道的所有队列中。(广播)
 *      当exchange中没有队列绑定时，此时向exchange发送的消息会被丢弃
 *
 *
 */
public interface Producer {

    void send(String msg);
}
