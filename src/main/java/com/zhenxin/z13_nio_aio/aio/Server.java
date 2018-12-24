package com.zhenxin.z13_nio_aio.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * @Created: 22:20 22/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Server {
    private AsynchronousServerSocketChannel serverSocketChannel;

    public Server() {
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8000));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                System.out.println("thread: " + Thread.currentThread().getName());
                byteBuffer.clear();
                try {
                    Future<Integer> read = result.read(byteBuffer);
                    read.get(10, TimeUnit.SECONDS);
                    byteBuffer.flip();
                    Future<Integer> write = result.write(byteBuffer);

                    System.out.println("msg: " + new String(byteBuffer.array()));
                    serverSocketChannel.accept(null, this);
                    write.get();
                    result.close();
                } catch (InterruptedException | ExecutionException | TimeoutException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("fail: " + exc.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        new Server().start();
        LockSupport.park();
    }
}
