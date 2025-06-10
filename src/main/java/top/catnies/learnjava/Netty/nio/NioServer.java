package top.catnies.learnjava.Netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open(); // 创建一个服务器端通道，用于监听客户端连接
        ssc.configureBlocking(false); // 设置为非阻塞模式，避免 accept() 等操作阻塞线程
        ssc.bind(new InetSocketAddress("127.0.0.1", 8987)); // 绑定到指定 IP 和端口，监听客户端连接

        Selector selector = Selector.open(); // 创建一个监听器, 这个监听器可以监听大通道里的事件.
        ssc.register(selector, SelectionKey.OP_ACCEPT); // 代表这个监听器注册进大通道, 并且关心大通道的 accept 事件.

        while (true) {
            selector.select(); // 阻塞方法，等待至少一个注册通道的事件就绪（如 accept/read/write 等）
            Set<SelectionKey> selectionKeys = selector.selectedKeys(); // 获取当前已就绪的事件集合
            Iterator<SelectionKey> iterator = selectionKeys.iterator(); // 获取迭代器，逐个处理就绪的事件

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();  // 必须手动移除处理过的 key，否则下次循环会重复处理.

                // 然后我们需要检查这个key, 是否是可 Accept 的.
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel(); // 这个时候的 channel 这个函数拿到的是正在监听的 ServerSocketChannel.
                    SocketChannel client = serverSocketChannel.accept(); // 这个就是从大通道中获取的小通道, 接受客户端连接，返回一个客户端通道
                    client.configureBlocking(false); // 同样, 先设置成非阻塞的.
                    client.register(selector, SelectionKey.OP_READ); // 监听器注册进客户端, 关注什么时候是可以读的.
                }

                // 然后检查这个key, 是否是可以读的.
                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel)selectionKey.channel(); // 如果这个key是可读的, 那么这个 channel 一定是个 SocketChannel.
                    ByteBuffer buffer = ByteBuffer.allocate(1024); // 本质是字节数组的封装
                    int length = channel.read(buffer); // 从通道读取数据到缓冲区，返回读取的字节数
                    if (length == -1) { // 如果返回的是-1, 那么代表客户端已经断开链接.
                        channel.close();
                        selectionKey.cancel(); // 取消该 key 的注册
                    } else {
                        buffer.flip(); // 切换缓冲区从写入模式到读取模式，准备读取数据
                        byte[] bytes = new byte[buffer.remaining()]; // 创建字节数组，长度为缓冲区中剩余可读字节数
                        buffer.get(bytes); // 从缓冲区读取数据到字节数组
                        String message = new String(bytes); // 将字节数组转换为字符串
                        System.out.println(message); // 打印接收到的消息
                    }
                }

            }

        }
    }
}
