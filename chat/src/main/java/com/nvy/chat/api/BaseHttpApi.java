package com.nvy.chat.api;

import android.content.Context;

import com.nvy.common.HTTPClient;

import java.io.IOException;

/**
 * Created by Vilas on 2/12/15.
 */


public class BaseHttpApi {

    public String url;
    public String httpMethod;

    public String[] makeServerCall(Context context, String requestBody,String contentType) throws IOException
    {
        String[] response=null;
        switch (this.httpMethod)
        {
            case HttpConstants.HTTP_METHOD_GET :
                response= HTTPClient.get(context, this.url, requestBody);
                break;
            case HttpConstants.HTTP_METHOD_POST:
                response= HTTPClient.post(context, this.url, requestBody, contentType);
                break;


        }
        return response;
    }
}
