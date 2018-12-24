package com.zhenxin.z13_nio_aio.server_nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Created: 21:13 22/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class EchoServer2 {
    private Selector selector;

    private static ExecutorService pool = Executors.newCachedThreadPool();

    private ConcurrentHashMap<Socket, Long> hashMap = new ConcurrentHashMap<>();

    public EchoServer2() {
        try {
            selector = Selector.open();

            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);

            InetSocketAddress inetAddress = new InetSocketAddress(8000);
            ssc.socket().bind(inetAddress);

            ssc.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServer() {
        try {
            for (;;) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();

                    if (next.isAcceptable()) {
                        doAccept(next);
                    }
                    else if (next.isValid() && next.isReadable()) {
                        doRead(next);
                        hashMap.put(((SocketChannel) next.channel()).socket(), System.currentTimeMillis());
                    }
                    else if (next.isValid() && next.isWritable()) {
                        doWrite(next);
                        Long s = hashMap.remove(((SocketChannel) next.channel()).socket());
                        long e = System.currentTimeMillis();
                        System.out.println("time: " + (e - s) + "ms");
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doAccept(SelectionKey next) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) next.channel();
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);

            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);

            Client client = new Client();
            selectionKey.attach(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void doRead(SelectionKey next) {
        SocketChannel channel = (SocketChannel) next.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            int read = channel.read(byteBuffer);
            if (read < 0) {
                disConnect(next);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            disConnect(next);
            return;
        }
        byteBuffer.flip(); // 只能执行一次
        System.out.println("msg: " + new String(byteBuffer.array()));
        pool.execute(new handleMsg(next, byteBuffer));
    }

    private void doWrite(SelectionKey next) {
        SocketChannel channel = (SocketChannel) next.channel();
        Client client = (Client) next.attachment();
        try {
            ByteBuffer byteBuffer = client.getOutput().poll();
            int write = channel.write(byteBuffer);
            if (write < 0) {
                disConnect(next);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            disConnect(next);
            return;
        }
        if (client.getOutput().size() == 0) {
            next.interestOps(SelectionKey.OP_READ);
        }
    }

    private void disConnect(SelectionKey next) {
        SocketChannel channel = (SocketChannel) next.channel();
        try {
            channel.socket().close();
            channel.close();
            next.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Client {
        private ConcurrentLinkedQueue<ByteBuffer> input;
        private ConcurrentLinkedQueue<ByteBuffer> output;
        public Client() {
            input = new ConcurrentLinkedQueue<>();
            output = new ConcurrentLinkedQueue<>();
        }
        public ConcurrentLinkedQueue<ByteBuffer> getInput() {
            return input;
        }
        public ConcurrentLinkedQueue<ByteBuffer> getOutput() {
            return output;
        }
    }
    class handleMsg implements Runnable {
        private SelectionKey key;
        private ByteBuffer byteBuffer;

        public handleMsg(SelectionKey key, ByteBuffer byteBuffer) {
            this.key = key;
            this.byteBuffer = byteBuffer;
        }

        @Override
        public void run() {
            Client client = (Client) key.attachment();
            client.getOutput().offer(byteBuffer);
            client.getInput().offer(byteBuffer);

            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            key.selector().wakeup();
        }
    }

    public static void main(String[] args) {
        new EchoServer2().startServer();
    }
}
