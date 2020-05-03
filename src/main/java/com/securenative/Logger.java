package com.securenative;

import org.slf4j.LoggerFactory;

enum LogLevel {
    TRACE("trace"),
    DEBUG("trace"),
    INFO("info"),
    WARN("warn"),
    ERROR("error");


    private final String text;

    LogLevel(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

interface ILogger {
    void trace(String var1, Object... var2);
    void debug(String var1, Object... var2);
    void info(String var1, Object... var2);
    void warn(String var1, Object... var2);
    void error(String var1, Object... var2);
}

public class Logger implements ILogger {
    private static LogLevel _logLevel = LogLevel.ERROR;
    private org.slf4j.Logger _logger = null;

    private Logger(Class<?> clazz){
        this._logger = LoggerFactory.getLogger(clazz);
    }

    static void initLogger(String logLevel) {
        try{
            _logLevel = LogLevel.valueOf(logLevel);
        }catch (IllegalArgumentException ex){
            _logLevel = LogLevel.ERROR;
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }

    @Override
    public void trace(String var1, Object... var2) {
        if(_logLevel == LogLevel.TRACE){
            _logger.error(var1, var2);
        }
    }

    @Override
    public void debug(String var1, Object... var2) {
        if(_logLevel == LogLevel.DEBUG){
            _logger.debug(var1, var2);
        }
    }

    @Override
    public void info(String var1, Object... var2) {
        if(_logLevel == LogLevel.INFO){
            _logger.error(var1, var2);
        }
    }

    @Override
    public void warn(String var1, Object... var2) {
        if(_logLevel == LogLevel.WARN){
            _logger.warn(var1, var2);
        }
    }

    @Override
    public void error(String var1, Object... var2) {
        if(_logLevel == LogLevel.ERROR){
            _logger.error(var1, var2);
        }
    }
}


