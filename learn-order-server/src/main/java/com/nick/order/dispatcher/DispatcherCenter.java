package com.nick.order.dispatcher;

import com.nick.order.common.OperationResult;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wubo15 on 2020/11/6.
 *
 * @author wubo15
 * @date 2020/11/6
 */
public class DispatcherCenter {

    private static final Map<Long, OperationResultFuture> dispatcherCenterMap = new ConcurrentHashMap<>();

    public static void add(Long streamId, OperationResultFuture operationResultFuture) {
        dispatcherCenterMap.put(streamId, operationResultFuture);
    }


    public static void set(Long streamId, OperationResult operationResult) {
        OperationResultFuture operationResultFuture = dispatcherCenterMap.get(streamId);
        if (Objects.nonNull(operationResultFuture)) {
            operationResultFuture.setSuccess(operationResult);
            dispatcherCenterMap.remove(streamId);
        }

    }

}
