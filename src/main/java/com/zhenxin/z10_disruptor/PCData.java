package com.zhenxin.z10_disruptor;

/**
 * 代表数据
 *
 * -- 生产者不断产生整数，消费者读取生产者的整数，并计算其平方
 * @Created: 19:59 16/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class PCData {

    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
