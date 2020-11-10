package com.nick.order.server;

import com.nick.order.codec.OrderFrameEncode;
import com.nick.order.codec.server.OrderProtocolEncode4Server;
import com.nick.order.codec.OrderFrameDecode;
import com.nick.order.codec.server.OrderProtocolDecode4Server;
import com.nick.order.handler.server.OrderServerProcessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by wubo15 on 2020/11/6.
 *
 * @author wubo15
 * @date 2020/11/6
 */
public class OrderServer {


    public static void main(String[] args) throws Exception {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new OrderFrameDecode());
                            p.addLast(new OrderFrameEncode());
                            p.addLast(new OrderProtocolDecode4Server());
                            p.addLast(new OrderProtocolEncode4Server());
                            p.addLast(new OrderServerProcessHandler());
                        }
                    });

            // Start the server.
            ChannelFuture f = b.bind(8080).sync();

            // Wait until the server socket is closed.
            //让线程进入 wait 状态 ？？
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
