package top.catnies.learnjava.Netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpWebServer {
    private final int port;

    public HttpWebServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    // 入站
                                    .addLast("decoder", new HttpRequestDecoder())
                                    .addLast("handler", new HttpServerHandler())

                                    // 出站
                                    .addLast("encoder", new HttpResponseEncoder());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("HTTP服务器启动成功，监听端口: " + port);
            System.out.println("访问地址: http://localhost:" + port);

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


    static class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
            if (msg instanceof HttpRequest) {
                HttpRequest request = (HttpRequest) msg;

                // 创建响应内容
                String content = createHtmlContent();

                // 创建HTTP响应
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HTTP_1_1,
                        OK,
                        Unpooled.copiedBuffer(content, CharsetUtil.UTF_8)
                );

                // 设置响应头
                response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);

                // 发送响应并关闭连接
                ctx.writeAndFlush(response)
                        .addListener(ChannelFutureListener.CLOSE); // 当任务完成之后, 执行CLOSE任务.
            }
        }


        private String createHtmlContent() {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Netty HTTP Server</title>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            text-align: center;\n" +
                    "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                    "            color: white;\n" +
                    "            margin: 0;\n" +
                    "            padding: 100px 20px;\n" +
                    "        }\n" +
                    "        .container {\n" +
                    "            background: rgba(255, 255, 255, 0.1);\n" +
                    "            border-radius: 20px;\n" +
                    "            padding: 50px;\n" +
                    "            max-width: 600px;\n" +
                    "            margin: 0 auto;\n" +
                    "            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);\n" +
                    "        }\n" +
                    "        h1 {\n" +
                    "            font-size: 3em;\n" +
                    "            margin-bottom: 20px;\n" +
                    "            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);\n" +
                    "        }\n" +
                    "        p {\n" +
                    "            font-size: 1.2em;\n" +
                    "            margin-bottom: 10px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <h1>Hello World!</h1>\n" +
                    "        <p>欢迎使用 Netty HTTP 服务器</p>\n" +
                    "        <p>服务器运行正常 ✓</p>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

    }


    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new HttpWebServer(port).start();
    }
}
