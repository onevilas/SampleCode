package com.nvy.chat.api;

import com.nvy.chat.utils.AppConstants;
import com.nvy.chat.utils.RunTimeData;

/**
 * Created by zovi on 2/12/15.
 */
public class GetOnlineUserInGroupApi extends BaseHttpApi {

    public GetOnlineUserInGroupApi(String userId,String groupName) {
//        this.url = "http://node4.kswag.in:3000/accounts/groups/user/online?" +
//                "u="+userId+"&a="+ AppConstants.APP_ID+"&s="+AppConstants.SECRET_KEY+"&g="+groupName;
        this.url="http://"+HttpConstants.HTTP_SERVER_NAME+":"+HttpConstants.HTTP_PORT+"/accounts/groups/user/online?"+
                "u="+userId+"&a="+ AppConstants.APP_ID+"&s="+AppConstants.SECRET_KEY+"&g="+groupName;
        this.httpMethod = HttpConstants.HTTP_METHOD_GET;
        RunTimeData.groupId=groupName;
    }
}