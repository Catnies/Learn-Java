package top.catnies.learnjava.Netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8987);     // 创建一个服务器socket, 监听端口 2580;
        System.out.println("Server started, waiting...");

        while (true) {
            Socket clientSocket = serverSocket.accept();            // 使用accept监听链接, 这个操作是阻塞的.
            InputStream clientSocketInputStream = clientSocket.getInputStream();    // 获取客户端链接的输入流
            System.out.println("Client connected!");

            // 使用read方法读取输入流的内容, 并打印出来.
            int readLength;
            byte[] buffer = new byte[1024];
            while ((readLength = clientSocketInputStream.read(buffer)) != -1) {
                System.out.println("Received data: " + new String(buffer, 0 , readLength));
            }
        }
    }

}
