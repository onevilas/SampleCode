package com.nvy.chat.api;

/**
 * Created by zovi on 2/12/15.
 */
public class ChatLoginHttpApi extends BaseHttpApi {

    public ChatLoginHttpApi()
    {
//        this.url = "http://node4.kswag.in:3000/accounts/authenticate/";
        this.url="http://"+HttpConstants.HTTP_SERVER_NAME+":"+HttpConstants.HTTP_PORT+"/accounts/authenticate/";
        this.httpMethod = HttpConstants.HTTP_METHOD_POST;
    }
}
