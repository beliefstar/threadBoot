package com.zhenxin.z12_rx_java;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @Created: 13:59 20/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Demo {

    public static void main(String[] args) {
        // 被观察者
        Observable<String> stringObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                /*
                onNext方法可以无限调用，Observer（观察者）所有的都能接收到，
                onError和onComplete是互斥的，Observer（观察者）只能接收到一个，
                  OnComplete可以重复调用，但是Observer（观察者）只会接收一次，
                  onError不可以重复调用，第二次调用就会报异常。
                 */
                observableEmitter.onNext("1");
                observableEmitter.onNext("2");
                observableEmitter.onNext("3");
                observableEmitter.onComplete();
                observableEmitter.onNext("4");
            }
        });

        // 观察者
        Observer<String> observer = new Observer<String>() {
            Disposable disposable;
            @Override
            public void onSubscribe(Disposable disposable) {
                System.out.println("call onSubscribe");
                /*
                Disposable英文意思是可随意使用的，这里就相当于读者和连载小说的订阅关系，
                如果读者不想再订阅该小说了，
                可以调用 disposable.dispose()取消订阅，
                此时连载小说更新的时候就不会再推送给读者了。
                 */
                System.out.println(disposable.isDisposed());
                System.out.println(disposable);
                this.disposable = disposable;
            }

            @Override
            public void onNext(String o) {
                System.out.println("call onNext");
                System.out.println(o);
                if (o.equals("disposable")) {
                    this.disposable.dispose();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("call onError");
            }

            @Override
            public void onComplete() {
                System.out.println("observer receive complete");
            }
        };

        // 建立订阅关系
        stringObservable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String operatorId) throws Exception {
                System.out.println(operatorId);
            }
        });
    }
}
