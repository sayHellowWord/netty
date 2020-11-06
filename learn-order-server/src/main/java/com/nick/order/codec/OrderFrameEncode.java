package com.nick.order.codec;


import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by wubo15 on 2020/11/6.
 *
 * @author wubo15
 * @date 2020/11/6
 */
public class OrderFrameEncode extends LengthFieldPrepender {

    public OrderFrameEncode() {
        super(2);
    }
}
