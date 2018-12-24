package com.zhenxin.z13_nio_aio.file_nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * @Created: 14:43 21/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\xzhen\\Desktop\\nginx-1.12.2.zip");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        FileOutputStream out = new FileOutputStream("C:\\Users\\xzhen\\Desktop\\nginx-1.12.2_copy.zip");
        FileChannel outChannel = out.getChannel();

        byteBuffer.clear();
        int len = -1;
        while ((len = channel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            outChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        System.out.println("end");

    }
}
