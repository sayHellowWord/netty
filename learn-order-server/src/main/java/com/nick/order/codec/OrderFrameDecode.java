package com.nick.order.codec;


import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by wubo15 on 2020/11/6.
 *
 * @author wubo15
 * @date 2020/11/6
 */
public class OrderFrameDecode extends LengthFieldBasedFrameDecoder {

    public OrderFrameDecode() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }
}
