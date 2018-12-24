package com.zhenxin.z13_nio_aio.server_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @Created: 13:26 21/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Server {

    private Selector selector;
    private static ExecutorService pool = Executors.newCachedThreadPool();

    private ConcurrentHashMap<Socket, Long> hashMap;

    public Server() {
        try {
            this.selector = SelectorProvider.provider().openSelector();

            ServerSocketChannel serverSocketChannel = SelectorProvider.provider().openServerSocketChannel();
            serverSocketChannel.configureBlocking(false);

            InetSocketAddress address = new InetSocketAddress(8000);
            serverSocketChannel.socket().bind(address);

            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            this.hashMap = new ConcurrentHashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() throws IOException {
        for (;;) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();

                if (next.isAcceptable()) {
                    this.doAccept(next);
                }

                else if (next.isValid() && next.isReadable()) {
                    this.doRead(next);
                    hashMap.put(((SocketChannel)next.channel()).socket(), System.currentTimeMillis());
                }

                else if (next.isValid() && next.isWritable()) {
                    this.doWrite(next);
                    Long s = this.hashMap.remove(((SocketChannel)next.channel()).socket());
                    long e = System.currentTimeMillis();
                    System.out.println("time: " + (e - s) + "ms");
                }
            }
        }
    }

    private void doAccept(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            long l = System.currentTimeMillis();
            SocketChannel channel = ssc.accept();
            System.out.println("==" + (System.currentTimeMillis() - l));
            channel.configureBlocking(false);
            SelectionKey selectionKey = channel.register(this.selector, SelectionKey.OP_READ);

            EchoClient echoClient = new EchoClient();
            selectionKey.attach(echoClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doRead(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            int read = socketChannel.read(byteBuffer); // 没有阻塞
            if (read < 0) {
                disconnect(key);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            disconnect(key);
            return;
        }
        byteBuffer.flip();
        pool.execute(new HandleMsg(key, byteBuffer));

    }

    private void doWrite(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        EchoClient echoClient = (EchoClient) key.attachment();
        try {
            ConcurrentLinkedQueue<ByteBuffer> queue = echoClient.getQueue();
            ByteBuffer byteBuffer = queue.poll();
            int write = socketChannel.write(byteBuffer);
            if (write < 0) {
                disconnect(key);
                return;
            }
            if (queue.size() == 0) {
                key.interestOps(SelectionKey.OP_READ);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        try {
            socketChannel.socket().close();
            socketChannel.close();
            selectionKey.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class EchoClient {
        private ConcurrentLinkedQueue<ByteBuffer> msg;
        private EchoClient () {
            msg = new ConcurrentLinkedQueue<>();
        }
        private ConcurrentLinkedQueue<ByteBuffer> getQueue() {
            return msg;
        }
        private void offer(ByteBuffer byteBuffer) {
            msg.offer(byteBuffer);
        }
    }

    private static class HandleMsg implements Runnable {

        private SelectionKey key;

        private ByteBuffer byteBuffer;

        public HandleMsg(SelectionKey key, ByteBuffer byteBuffer) {
            this.key = key;
            this.byteBuffer = byteBuffer;
        }

        @Override
        public void run() {
            EchoClient echoClient = (EchoClient) key.attachment();
            echoClient.offer(byteBuffer);

            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            key.selector().wakeup();
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}
