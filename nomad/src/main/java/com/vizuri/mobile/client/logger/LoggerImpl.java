package com.vizuri.mobile.client.logger;

import org.urish.gwtit.titanium.API;

class LoggerImpl implements Logger {
    private final String name;

    public LoggerImpl(String name) {
        this.name = name;
    }

    protected String decorate(String message) {
        return "[" + name + "] " + message;
    }

    public void info(String message) {
        API.info(decorate(message));
    }

    public void warn(String message) {
        API.warn(decorate(message));
    }

    public void error(String message) {
        API.error(decorate(message));
    }

    public void debug(String message) {
        API.debug(decorate(message));
    }
}
