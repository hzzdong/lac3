package com.linkallcloud.core.lang.stream;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.linkallcloud.core.lang.Encoding;
import com.linkallcloud.core.lang.Lang;

public class StringInputStream extends ByteArrayInputStream {

    public StringInputStream(CharSequence s, Charset charset) {
        super(toBytes(s, charset));
    }

    public StringInputStream(CharSequence s) {
        super(toBytes(s, Encoding.CHARSET_UTF8));
    }
    
    protected static byte[] toBytes(CharSequence str, Charset charset) {
        if (str == null)
            return new byte[0];
        if (charset == null)
            charset = Encoding.CHARSET_UTF8;
        try {
            return str.toString().getBytes(charset.name());
        }
        catch (UnsupportedEncodingException e) {
            throw Lang.wrapThrow(e);
        }
    }
}
