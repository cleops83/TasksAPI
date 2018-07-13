package com.celonis.challenge.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... args) {
        logger.debug("Throwable ", throwable);
        logger.debug("method ", method);

        for (Object arg : args) {
            logger.debug("Argument value - " + arg);
        }

    }
}
