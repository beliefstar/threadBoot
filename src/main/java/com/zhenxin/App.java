package com.zhenxin;

import java.io.IOException;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {

        /** 环境变量 */
        Map<String, String> getenv = System.getenv();
        System.out.println(getenv);
/*
        Runtime runtime = Runtime.getRuntime();
        InputStream inputStream = runtime.exec("ipconfig").getInputStream();
        System.out.println("---------------");
        int len = 1024;
        byte[] bytes = new byte[len];
        while (inputStream.read(bytes) != -1) {
            System.out.println(new String(bytes));
        }*/

    }
}
