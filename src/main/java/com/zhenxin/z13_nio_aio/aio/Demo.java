package com.zhenxin.z13_nio_aio.aio;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Created: 22:46 22/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("");

        URI uri = URI.create("C:/Users/xzhen/Desktop/nginx-1.12.2.zip");
        AsynchronousFileChannel fileInputChannel = AsynchronousFileChannel.open(Paths.get(uri));
        URI uri2 = URI.create("C:/Users/xzhen/Desktop/nginx-1.12.2_copy.zip");
        AsynchronousFileChannel fileOutputChannel = AsynchronousFileChannel.open(Paths.get(uri2));

        AtomicInteger atomicInteger = new AtomicInteger(0);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        fileInputChannel.read(byteBuffer, 0, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                if (result > 0) {
                    byteBuffer.flip();
                    int i;
                    int l;
                    do {
                        i = atomicInteger.get();
                        l = i + byteBuffer.position();
                    } while (atomicInteger.compareAndSet(i, l));

                    fileOutputChannel.write(byteBuffer, i, null, new CompletionHandler<Integer, Object>() {
                        @Override
                        public void completed(Integer result, Object attachment) {
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {

                        }
                    });
                    byteBuffer.clear();
                    fileInputChannel.read(byteBuffer, l, null, this);
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });
    }
}
