package com.zhenxin.z10_disruptor;

import com.lmax.disruptor.EventFactory;

/**
 *
 * 产生PCData的工厂类
 * 会在Disruptor系统初始化时，构造所有的缓冲区中的对象实例
 *
 * @Created: 20:11 16/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class PCDataFactory implements EventFactory<PCData> {

    @Override
    public PCData newInstance() {
        return new PCData();
    }
}
