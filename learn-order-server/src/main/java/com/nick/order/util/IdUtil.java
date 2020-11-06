package com.nick.order.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ID本地自增器
 */
public final class IdUtil {

    private static final AtomicLong IDX = new AtomicLong();

    private IdUtil() {
        //no instance
    }

    public static long nextId() {
        return IDX.incrementAndGet();
    }

}
