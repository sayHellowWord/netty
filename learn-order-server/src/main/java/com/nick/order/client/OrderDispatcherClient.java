package com.nick.order.client;

import com.nick.order.codec.OrderFrameDecode;
import com.nick.order.codec.OrderFrameEncode;
import com.nick.order.codec.client.OrderProtocolDecode4Client;
import com.nick.order.codec.client.OrderProtocolEncode4Client;
import com.nick.order.common.OperationResult;
import com.nick.order.common.RequestMessage;
import com.nick.order.common.order.OrderOperation;
import com.nick.order.dispatcher.DispatcherCenter;
import com.nick.order.dispatcher.OperationResultFuture;
import com.nick.order.handler.client.OrderServerProcessDispatcherHandler;
import com.nick.order.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by wubo15 on 2020/11/6.
 *
 * @author wubo15
 * @date 2020/11/6
 */
public class OrderDispatcherClient {


    public static void main(String[] args) throws Exception {

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(new OrderFrameDecode());
                            p.addLast(new OrderFrameEncode());
                            p.addLast(new OrderProtocolDecode4Client());
                            p.addLast(new OrderProtocolEncode4Client());
                            p.addLast(new OrderServerProcessDispatcherHandler());
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect("127.0.0.1", 8080).sync();
            long nextId = IdUtil.nextId();
            RequestMessage requestMessage = new RequestMessage(nextId, new OrderOperation(555, "土豆土豆，我是地瓜"));
            OperationResultFuture operationResultFuture = new OperationResultFuture();
            DispatcherCenter.add(nextId, operationResultFuture);

            f.channel().writeAndFlush(requestMessage);

            OperationResult operationResult = operationResultFuture.get();
            System.out.println("result:" + operationResult);


            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

}
