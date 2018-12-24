package com.zhenxin.z13_nio_aio.old_io;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * @Created: 10:48 21/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class SocketClient {
    private static ExecutorService pool = Executors.newCachedThreadPool();

    private static class Client implements Runnable {
        @Override
        public void run() {
            Socket socket = null;
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                socket = new Socket("localhost", 8000);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("1");
                System.out.println(in.readLine());
                LockSupport.parkNanos(1000 * 1000 * 1000);
                out.println("2");
                System.out.println(in.readLine());
                LockSupport.parkNanos(1000 * 1000 * 1000);
                out.println("3");
                System.out.println(in.readLine());
                LockSupport.parkNanos(1000 * 1000 * 1000);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null) socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("complete");
            }
        }
    }
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            pool.submit(new Client());
        }

        while (true) {
            LockSupport.parkNanos(1000 * 1000);
        }
    }
}
