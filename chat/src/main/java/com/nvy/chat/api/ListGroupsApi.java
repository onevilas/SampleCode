package com.nvy.chat.api;

import com.nvy.chat.utils.AppConstants;

/**
 * Created by zovi on 2/12/15.
 */
public class ListGroupsApi extends BaseHttpApi {

    public ListGroupsApi(String userId) {
//        this.url = "http://node4.kswag.in:3000/accounts/groups?u="
//                +userId+"&a="+ AppConstants.APP_ID+"&s="+AppConstants.SECRET_KEY;
        this.url="http://"+HttpConstants.HTTP_SERVER_NAME+":"+HttpConstants.HTTP_PORT+"/accounts/groups?u="
                +userId+"&a="+ AppConstants.APP_ID+"&s="+AppConstants.SECRET_KEY;
        this.httpMethod = HttpConstants.HTTP_METHOD_GET;
    }
}
