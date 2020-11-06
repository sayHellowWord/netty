package com.nick.order.common;

import com.nick.order.util.JsonUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.nio.charset.Charset;

/**
 * 网络传输消息体
 *
 * @param <T>
 */
@Data
public abstract class Message<T extends MessageBody> {

    /**
     * 消息头
     */
    private MessageHeader messageHeader;
    /**
     * 消息内容
     */
    private T messageBody;

    public T getMessageBody() {
        return messageBody;
    }

    /**
     * 消息编码，用于后续网络传输
     *
     * @param byteBuf
     */
    public void encode(ByteBuf byteBuf) {
        byteBuf.writeInt(messageHeader.getVersion());
        byteBuf.writeLong(messageHeader.getStreamId());
        byteBuf.writeInt(messageHeader.getOperatingType());
        byteBuf.writeBytes(JsonUtil.toJson(messageBody).getBytes());
    }

    /**
     * 根据操作类型获取对应消息体协议类
     *
     * @param opcode
     * @return
     */
    public abstract Class<T> getMessageBodyDecodeClass(int opcode);

    /**
     * 消息解码，接收到网络传输数据流后，解析为对应的消息体
     *
     * @param msg
     */
    public void decode(ByteBuf msg) {
        /**
         * 严格对应于消息编码时，字段熟悉类型+顺序
         */
        int version = msg.readInt();
        long streamId = msg.readLong();
        int operatingType = msg.readInt();

        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setVersion(version);
        messageHeader.setOperatingType(operatingType);
        messageHeader.setStreamId(streamId);
        this.messageHeader = messageHeader;

        /**
         * 策略模式，针对不同的操作类型，反序列化为不同的类型
         */
        Class<T> bodyClazz = getMessageBodyDecodeClass(operatingType);
        /**
         * 消息内容，根据类型反序列化
         */
        T body = JsonUtil.fromJson(msg.toString(Charset.forName("UTF-8")), bodyClazz);
        this.messageBody = body;
    }

}
