package com.ks.currencyexchange.infrastructure.common;

import cn.hutool.core.lang.Snowflake;

public class SnowflakeIdGenerator {

    private static final Snowflake snowflake;

    private static SnowflakeIdGenerator instance;

    static {
        snowflake = new Snowflake();
    }

    public static synchronized SnowflakeIdGenerator getInstance() {
        if (instance == null) {
            instance = new SnowflakeIdGenerator();
        }

        return instance;
    }

    public long generate() {
        return snowflake.nextId() % 100000000;
    }
}
