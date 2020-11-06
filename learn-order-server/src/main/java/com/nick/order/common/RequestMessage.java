package com.nick.order.common;

public class RequestMessage extends Message<Operation>{

    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationClazz();
    }

    public RequestMessage(){}

    public RequestMessage(Long streamId, Operation operation){
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setStreamId(streamId);
        messageHeader.setOperatingType(OperationType.fromOperation(operation).getOpCode());
        this.setMessageHeader(messageHeader);
        this.setMessageBody(operation);
    }

}
