package com.zhenxin.z13_nio_aio.old_io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Created: 9:46 21/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class SocketServer {
    private final ExecutorService pool;

    private ServerSocket serverSocket;

    public SocketServer() {
        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool = Executors.newCachedThreadPool();
    }

    private void start() {
        try {
            System.out.println("socket server start success!\nwait client connection...");
            while (true) {
                Socket socketClient = serverSocket.accept();
                pool.execute(new ClientHandler(socketClient));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            long s = System.currentTimeMillis();
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    out.println(line);
                    long e = System.currentTimeMillis();
                    System.out.println("time: " + (e - s) + "ms");
                    s = e;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null) socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new SocketServer().start();
    }
}
