package de.seifi.rechnung_manager_app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RechUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RechUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.error("UncaughtException", e);
    }
}
