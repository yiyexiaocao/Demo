package com.gl.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by gl on 2016/11/12.
 */
public class NIOServer {

    //通道管理器
    private Selector selector;


    /**
     * 获得一个ServerSocket通道，并对该通道做一些初始化工作
     * @param port
     * @throws IOException
     */
    public void initServer(int port) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置通道为非阻塞的
        serverSocketChannel.configureBlocking(false);
        //将通道对应的ServerSocket绑定到端口上
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        //获取一个通道管理器
        this.selector = Selector.open();
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件，注册该事件后，当该事件到达时，selector.select()会返回，如果该事件没有到达selector.select()会一直阻塞
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen() throws IOException {
        System.out.println("服务端启动成功！");
        // 轮询访问selector
        while (true) {
            //当注册的事件到达时，方法返回；否则,该方法会一直阻塞
            selector.select();
            // 获得selector中选中的项的迭代器，选中的项为注册的事件
            Iterator ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
                // 删除已选的key,以防重复处理
                ite.remove();
                // 客户端请求连接事件
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key
                            .channel();
                    // 获得和客户端连接的通道
                    SocketChannel channel = server.accept();
                    // 设置成非阻塞
                    channel.configureBlocking(false);

                    //在这里可以给客户端发送信息哦
                    channel.write(ByteBuffer.wrap(new String("向客户端发送了一条信息").getBytes()));
                    //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                    channel.register(this.selector, SelectionKey.OP_READ);

                    // 获得了可读的事件
                } else if (key.isReadable()) {
                    NIOCommons.read(key);
                }
            }
        }
    }
    /**
     * 启动服务端测试
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        NIOServer server = new NIOServer();
        server.initServer(8000);
        server.listen();
    }
}
