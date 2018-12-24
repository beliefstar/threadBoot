package com.zhenxin.z13_nio_aio.server_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Created: 22:02 22/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class EchoClient {
    private Selector selector;

    public EchoClient() {
        try {
            selector = Selector.open();
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(8000));

            channel.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void work() throws IOException {
        while (true) {
            if (!selector.isOpen()) {
                break;
            }
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();
                if (next.isConnectable()) {
                    connect(next);
                }
                else if (next.isWritable()) {
                    doWrite(next);
                }
                else if (next.isReadable()) {
                    doRead(next);
                }
            }
        }
    }

    private void connect(SelectionKey next) {
        SocketChannel channel = (SocketChannel) next.channel();
        try {
            if (channel.isConnectionPending()) {
                channel.finishConnect();
            }
            next.interestOps(SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(SelectionKey next) {
        SocketChannel channel = (SocketChannel) next.channel();
        try {
            channel.write(ByteBuffer.wrap("Hello World".getBytes()));
            next.interestOps(SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doRead(SelectionKey next) {
        SocketChannel channel = (SocketChannel) next.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            channel.read(byteBuffer);
            byteBuffer.flip();
            String str = new String(byteBuffer.array());
            System.out.println("message: " + str);
            channel.close();
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new EchoClient().work();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
