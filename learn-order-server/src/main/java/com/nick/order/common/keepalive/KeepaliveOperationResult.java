package com.nick.order.common.keepalive;

import com.nick.order.common.OperationResult;
import lombok.Data;

@Data
public class KeepaliveOperationResult extends OperationResult {

    private final long time;

}
