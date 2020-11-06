package com.nick.order.common;

public abstract class Operation extends MessageBody {

    /**
     * 执行相应的业务操作并返回结果
     *
     * @return
     */
    public abstract OperationResult execute();

}
