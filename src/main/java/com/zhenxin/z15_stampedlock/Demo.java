package com.zhenxin.z15_stampedlock;

import java.util.concurrent.locks.StampedLock;

/**
 * @Created: 10:23 24/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Demo {
    private int x;

    private int y;

    private static StampedLock stampedLock;

    private void doWrite(int x, int y) {
        long writeLock = stampedLock.writeLock();

    }

    private void doRead() {

    }
}
