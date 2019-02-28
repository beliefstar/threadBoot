package com.zhenxin.z16_rabbitmq;

/**
 * @author xzhen
 * @created 16:51 25/01/2019
 * @description TODO
 */
public interface Consumer {

    void receive(String msg);
}
