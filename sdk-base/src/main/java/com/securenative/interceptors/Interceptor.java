package com.securenative.interceptors;

public interface Interceptor {
    Boolean canExecute();
    void getModule();
}
