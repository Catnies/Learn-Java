package top.catnies.learnjava.Netty.bio;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BioClient {
    public static void main(String[] args) throws InterruptedException {
        Thread tThread = startClient("first");
        Thread jThread = startClient("second");
        tThread.join();
        jThread.join();
    }

    public static Thread startClient(String name) {

        // 创建一个线程执行任务, 不断的发送消息
        Thread clientThread = new Thread(() -> {
            try {

                // 创建一个客户端链接并尝试链接.
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("localhost", 8987));

                // 获取客户端链接的输出流, 写入数据后关闭链接.
                OutputStream outputStream = socket.getOutputStream();
                for (int i = 10; i > 0; i--) {
                    String message = String.format("Hello, I am %s , message %d", Thread.currentThread().getName(), i);
                    outputStream.write(message.getBytes());
                    outputStream.flush();
                }

                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, name);

        // 开启线程
        clientThread.start();
        return clientThread;
    }
}
