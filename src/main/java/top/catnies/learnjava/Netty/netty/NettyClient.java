package top.catnies.learnjava.Netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();  // 创建客户端的 Bootstrap
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group);   // 客户端只需要一个线程组（EventLoopGroup），负责连接和处理读写。
        bootstrap.channel(NioSocketChannel.class);  // 指定 Netty 客户端使用的通道类型. NioSocketChannel 是对 NIO 原生 SocketChannel 的封装.
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {  // 为 ServerBootstrap 添加一个子处理器
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline(); // 获取管道

                // 入站处理器
                pipeline.addLast(new MyLengthDecoder()); // 自定义的消息解码器, 防止黏包和拆包.
                pipeline.addLast(new StringDecoder());   // Buffer解码成字符串
                pipeline.addLast(new PrintHandler());    // 打印消息处理器

                // 出站处理器
                pipeline.addLast(new MyLengthEncoder()); // 将字符串添加长度信息
            }
        });

        ChannelFuture connectFuture = bootstrap.connect("127.0.0.1", 8987); // 指定Bootstrap的目标端口.
        connectFuture.addListener(connect -> {
            if (connect.isSuccess()) {
                System.out.println("客户端成功链接了 8987 端口.");

                // 定时心跳消息
                connectFuture.channel().eventLoop().scheduleAtFixedRate(() -> {
                    connectFuture.channel().writeAndFlush("bt: hello server, i'm client, now at" + System.currentTimeMillis());
                }, 0, 3, TimeUnit.SECONDS);

                // 新线程监听控制台输入
                new Thread( () ->  {
                    // 用户输入发送给服务端
                    try (Scanner scanner = new Scanner(System.in)) {
                        while (true) {
                            System.out.println("<< 请输入要发送给服务端的内容：");
                            String text = scanner.nextLine();
                            if (text.isEmpty()) continue;
                            if ("quit".equals(text)) {
                                group.shutdownGracefully();
                                ChannelFuture future = connectFuture.channel().close();
                                future.sync();
                                break;
                            }
                            else connectFuture.channel().writeAndFlush(text);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();


            } else { System.out.println("服务器链接失败, 请检查服务端是否在线!"); }
        });
    }


    static class PrintHandler extends SimpleChannelInboundHandler<String> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println("[日志] 收到来自服务器: " + ctx.channel().remoteAddress() + " 的消息, 内容是: " + msg);
        }
    }


    static class MyLengthDecoder extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            // 第一步：标记当前位置（以防回滚）
            in.markReaderIndex();

            // 第二步：检查是否有长度字段
            // 消息格式: [长度][内容]
            if (in.readableBytes() < 4) return;  // 不够读出长度, 长度是一个 4 字节 的int.
            int length = in.readInt();

            // 第三步：检查是否有完整数据
            if (in.readableBytes() < length) {
                in.resetReaderIndex(); // 代表出现了拆包现象, 内容数据不够，回滚指针
                return;
            }

            // 第四步：读取内容部分, 然后传递给字符串解码器
            ByteBuf content = in.readBytes(length);// 这里读取的就是 [内容] 部分
            out.add(content); // Netty 自动把它 fire 到下一个 handler
        }
    }


    static class MyLengthEncoder extends MessageToByteEncoder<String> {

        @Override
        protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
            byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
            int length = bytes.length;
            out.writeInt(length);  // 写入长度字段
            out.writeBytes(bytes); // 写入数据内容
            System.out.println("[Encoder] 发送消息: " + msg);
        }
    }



}
