package com.securenative.snlogic;

import org.slf4j.LoggerFactory;

public class Logger{
    private static org.slf4j.Logger logger = new ImpotentLogger();

    public static org.slf4j.Logger getLogger() {
        return logger;
    }

    public static void setLoggingEnable(boolean isLoggingEnabledInput) {
        if(isLoggingEnabledInput){
            logger = LoggerFactory.getLogger(Logger.class);
            logger.info("Secure Native logger is enabled");
            return;
        }
        logger = new ImpotentLogger();

    }
}

