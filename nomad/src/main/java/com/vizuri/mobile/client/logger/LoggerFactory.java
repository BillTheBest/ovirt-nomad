package com.vizuri.mobile.client.logger;

public class LoggerFactory {
    public static <T> Logger get(Class<T> clazz) {
        return new LoggerImpl(clazz.getName());
    }
}
