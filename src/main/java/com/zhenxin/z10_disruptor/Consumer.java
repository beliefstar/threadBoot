package com.zhenxin.z10_disruptor;

import com.lmax.disruptor.WorkHandler;

/**
 * 消费者
 *
 * @Created: 20:04 16/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Consumer implements WorkHandler<PCData> {


    @Override
    public void onEvent(PCData event) throws Exception {
        System.out.println("-------------消费一条-----------------");
        System.out.println("Thread ID : " + Thread.currentThread().getId());
        System.out.println(Math.pow(event.getValue(), 2));
        System.out.println("--------------------------------------");
    }
}
