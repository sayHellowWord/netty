package com.nick.order.server;

import com.nick.order.codec.OrderFrameEncode;
import com.nick.order.codec.server.OrderProtocolEncode4Server;
import com.nick.order.codec.OrderFrameDecode;
import com.nick.order.codec.server.OrderProtocolDecode4Server;
import com.nick.order.handler.server.OrderServerProcessHandler;
import com.nick.order.monitor.MetricHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by wubo15 on 2020/11/6.
 *
 * @author wubo15
 * @date 2020/11/6
 */
public class OrderServer {


    public static void main(String[] args) throws Exception {

        StopWatch stopWatch = new StopWatch();
        // 开始时间
        stopWatch.start();

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        EventLoopGroup workerGroup = new NioEventLoopGroup(new DefaultThreadFactory("work"));
        try {
            ServerBootstrap b = new ServerBootstrap();
            /**
             * 性能检测-通用
             */
            MetricHandler metricHandler = new MetricHandler();

            b.group(bossGroup, workerGroup)
                    //此处设置里的channel的factory
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    //boss的handler
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //work的handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("metricHandler", metricHandler);
                            p.addLast("orderFrameDecode", new OrderFrameDecode());
                            p.addLast("orderFrameEncode", new OrderFrameEncode());
                            p.addLast("orderProtocolDecode4Server", new OrderProtocolDecode4Server());
                            p.addLast("orderProtocolEncode4Server", new OrderProtocolEncode4Server());
                            p.addLast("orderServerProcessHandler", new OrderServerProcessHandler());
                            p.addLast("orderServerProcessHandler", new OrderServerProcessHandler());
                        }
                    });

            // Start the server.
            ChannelFuture f = b
                    .bind(8080)
                    .sync();

            // 结束时间
            stopWatch.stop();
            // 统计执行时间（秒）
            System.out.println("执行时长：" + stopWatch.getTime(TimeUnit.SECONDS) + " 秒.");

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
