package com.linkallcloud.core.http.sender;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.linkallcloud.core.http.HttpException;
import com.linkallcloud.core.http.Request;
import com.linkallcloud.core.http.Response;
import com.linkallcloud.core.http.Sender;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Streams;

public class PostSender extends Sender {

    public PostSender(Request request) {
        super(request);
    }

    @Override
    public Response send() throws HttpException {
        try {
            openConnection();
            InputStream ins = request.getInputStream();
            setupRequestHeader();
            if (ins != null
                && request.getHeader() != null
                && ins instanceof ByteArrayInputStream
                && this.request.getHeader().get("Content-Length") == null)
                conn.addRequestProperty("Content-Length", "" + ins.available());
            setupDoInputOutputFlag();
            if (null != ins) {
                OutputStream ops = Streams.buff(getOutputStream());
                Streams.write(ops, ins);
                Streams.safeClose(ins);
                Streams.safeFlush(ops);
                Streams.safeClose(ops);
            }
            return createResponse(getResponseHeader());
        }
        catch (Exception e) {
            throw new HttpException(request.getUrl().toString(), e);
        }
    }

    @Override
    public int getEstimationSize() throws IOException {
        if (request.getInputStream() != null) {
            return request.getInputStream().available();
        } else {
            if (null != request.getData()) {
                return request.getData().length;
            }
            try {
                return request.getURLEncodedParams().getBytes(request.getEnc()).length;
            }
            catch (UnsupportedEncodingException e) {
                throw Lang.wrapThrow(e);
            }
        }
    }
}
