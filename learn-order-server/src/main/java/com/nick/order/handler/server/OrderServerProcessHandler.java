package com.nick.order.handler.server;

import com.nick.order.common.RequestMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 服务端处理逻辑
 *s's <p>
 * Created by wubo15 on 2020/11/6.
 *
 * @author wubo15
 * @date 2020/11/6
 */
public class OrderServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws Exception {
        ctx.writeAndFlush(msg.getMessageBody().execute());
    }
}
