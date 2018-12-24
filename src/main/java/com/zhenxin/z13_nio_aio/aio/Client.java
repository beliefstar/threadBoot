package com.zhenxin.z13_nio_aio.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.locks.LockSupport;

/**
 * @Created: 22:37 22/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Client {
    public static void main(String[] args) throws IOException {
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        client.connect(new InetSocketAddress("localhost", 8000), null, new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {
                client.write(ByteBuffer.wrap("Hello".getBytes()), null, new CompletionHandler<Integer, Object>() {
                    @Override
                    public void completed(Integer result, Object attachment) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        client.read(byteBuffer, null, new CompletionHandler<Integer, Object>() {
                            @Override
                            public void completed(Integer result, Object attachment) {
                                byteBuffer.flip();
                                System.out.println("msg: " + new String(byteBuffer.array()));
                                try {
                                    client.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(Throwable exc, Object attachment) {
                                System.out.println("read fail");
                            }
                        });
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        System.out.println("write fail");
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("connect fail");
            }
        });
        LockSupport.park();
    }
}
