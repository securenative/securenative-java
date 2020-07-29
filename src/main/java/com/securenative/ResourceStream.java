package com.securenative;

import java.io.InputStream;

public interface ResourceStream {
    InputStream getInputStream(String name);
}