package com.linkallcloud.core.lang.stream;

import java.io.IOException;
import java.io.InputStream;

public class VoidInputStream extends InputStream {

    @Override
    public int read() throws IOException {
        return -1;
    }

}
