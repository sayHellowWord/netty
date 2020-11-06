package com.nick.order.common;

import lombok.Data;

@Data
public class MessageHeader {

    /**
     * 协议版本，用于后续兼容
     */
    private int version = 1;
    /**
     * 操作类型，用于后续策略模式
     */
    private int operatingType;
    /**
     * 流ID，记录某次网络操作，可实现操作与业务逻辑的对应
     */
    private long streamId;


}
