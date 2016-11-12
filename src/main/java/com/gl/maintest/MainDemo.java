package com.gl.maintest;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by gl on 2016/11/9.
 */
public class MainDemo {


    public static void main(String[] args) throws IOException {

        File directory  ;
        File file  ;

        String fileName = "C:\\Users\\gl\\Desktop";
        directory = new File(fileName);
        directory.mkdirs();
        if (!directory.exists()) {
            return;
        }
        file = new File(fileName + File.separator + "Date.txt");
        file.createNewFile();
        if (!file.isFile()) {
            return;
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        FileChannel channel = outputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(2014);
        String str = "geliang geliang geliang..";
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
        channel.close();
        outputStream.close();


    }
}
