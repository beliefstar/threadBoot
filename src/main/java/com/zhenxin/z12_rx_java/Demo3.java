package com.zhenxin.z12_rx_java;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.internal.operators.flowable.FlowableBuffer;

/**
 * @Created: 14:38 20/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Demo3 {

    public static void main(String[] args) {
//        Flowable<String> stringFlowable = FlowableBuffer.create(new FlowableOnSubscribe<String>() {
//            @Override
//            public void subscribe(FlowableEmitter<String> flowableEmitter) throws Exception {
//                flowableEmitter.onNext("111");
//                flowableEmitter.onComplete();
//            }
//        }, BackpressureStrategy.BUFFER);
    }
}
