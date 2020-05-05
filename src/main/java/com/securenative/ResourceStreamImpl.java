package com.securenative;

import java.io.InputStream;

public class ResourceStreamImpl implements ResourceStream {
    @Override
    public InputStream getInputStream(String name) {
        return SecureNative.class.getClassLoader().getResourceAsStream(name);
    }
}
