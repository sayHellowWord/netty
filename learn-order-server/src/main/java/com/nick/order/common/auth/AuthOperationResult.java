package com.nick.order.common.auth;

import com.nick.order.common.OperationResult;
import lombok.Data;

@Data
public class AuthOperationResult extends OperationResult {

    private final boolean passAuth;

}
