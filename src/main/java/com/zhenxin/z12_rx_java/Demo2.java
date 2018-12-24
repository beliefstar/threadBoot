package com.zhenxin.z12_rx_java;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @Created: 14:23 20/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Demo2 {
    public static void main(String[] args) throws InterruptedException {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                String name = Thread.currentThread().getName();
                System.out.println("emitter thread name : " + name);
                System.out.println("------------emitter begin-----------");
                observableEmitter.onNext("1");
                observableEmitter.onNext("2");
                observableEmitter.onNext("3");
                System.out.println("------------emitter over-----------");
                observableEmitter.onComplete();
            }
        })
                /*
                subscribeOn() 指定的就是发射事件的线程，observerOn 指定的就是订阅者接收事件的线程。
                 */
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
//
                .map(item -> {
                    return item + "__";
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String operatorId) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable disposable) {
//
//                    }
//
//                    @Override
//                    public void onNext(String operatorId) {
//                        String name = Thread.currentThread().getName();
//                        System.out.println("onNext thread name : " + name);
//                        System.out.println(operatorId);
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        System.out.println("call onComplete");
//                    }
//                });

                Thread.sleep(2000);
//        System.out.println(str);
    }
}
