package com.linkallcloud.core.http.sender;

import com.linkallcloud.core.http.HttpException;
import com.linkallcloud.core.http.Request;
import com.linkallcloud.core.http.Response;
import com.linkallcloud.core.http.Sender;

public class GetSender extends Sender {

    public GetSender(Request request) {
        super(request);
    }

    @Override
    public Response send() throws HttpException {
        try {
            openConnection();
            setupRequestHeader();
            return createResponse(getResponseHeader());
        }
        catch (Exception e) {
            throw new HttpException(request.getUrl().toString(), e);
        }
    }

}
