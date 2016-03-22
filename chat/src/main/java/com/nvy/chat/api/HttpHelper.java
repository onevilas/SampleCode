package com.nvy.chat.api;

import android.content.Context;
import android.util.Log;

import com.nvy.chat.entity.AvailableUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vilas on 14/12/15.
 */
public class HttpHelper {

    private static HttpHelper sHttpHelper;

    Context mContext;

    public HttpHelper getInstance(Context context) {
        mContext = context;
        if (sHttpHelper == null) {
            sHttpHelper = new HttpHelper();
        }
        return sHttpHelper;
    }

    public String[] processLogin(String littleUserName, Boolean isXmppLoginDetailsNeeded) {

        String[] output = null;

        ChatLoginHttpApi chatLoginHttpApi = new ChatLoginHttpApi();
        try {
            if (isXmppLoginDetailsNeeded) {
                JSONObject postData = new JSONObject();
                postData.put("u", littleUserName);
                postData.put("a", "little");
                postData.put("s", "abc");
                String json = postData.toString();
                String[] response = chatLoginHttpApi.makeServerCall(mContext, json, "application/json");
                Log.i("LoginAsynch", response[0] + " " + response[1]);
                JSONObject jsonObject = new JSONObject(response[1]);
                output = new String[2];
                output[0] = jsonObject.getString("c");
                output[1] = jsonObject.getString("t");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output;
    }

    public List<String> processGroups(String senderNum) {
        List<String> groupList = new ArrayList<>();
//        String senderNum=PreferenceUtils.getUserXmppId(mContext)
//                .substring(PreferenceUtils.getUserXmppId(mContext).length() - 10,
//                        PreferenceUtils.getUserXmppId(mContext).length());
        ListGroupsApi listGroupsApi = new ListGroupsApi(senderNum);
        Log.i("GetGroupsAsynch", "Sender number" + senderNum);
        try {
            String[] response = listGroupsApi.makeServerCall(mContext, null, "");
            Log.i("GetGroupsAsynch", response[0] + " " + response[1]);
            JSONObject jsonObject = new JSONObject(response[1]);
            JSONArray groplist = jsonObject.getJSONArray("g");
            for (int i = 0; i < groplist.length(); i++) {
                JSONObject name = groplist.getJSONObject(i);
                groupList.add(name.getString("name"));
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return groupList;
    }

    public List<AvailableUser> processAvailableUsers(String senderNum){
        List<AvailableUser> userList = new ArrayList<>();

//        String senderNum=PreferenceUtils.getUserXmppId(mContext)
//                .substring(PreferenceUtils.getUserXmppId(mContext).length() - 10,
//                        PreferenceUtils.getUserXmppId(mContext).length());
        Log.i("AvailableUserAsync","Sender number"+senderNum);

        GetOnlineUserInGroupApi getOnlineUserInGroupApi = new GetOnlineUserInGroupApi(
                senderNum,"little");
        try {
            String[] response = getOnlineUserInGroupApi.makeServerCall(mContext, null, "");
            Log.i("AvailableUserAsync",response[0]+" "+response[1]);
            JSONObject jsonObject = new JSONObject(response[1]);
            JSONArray groplist = jsonObject.getJSONArray("o");
            for (int i = 0; i < groplist.length(); i++) {
                AvailableUser user = new AvailableUser();
                JSONObject name = groplist.getJSONObject(i);
                user.setUserName(name.getString("c"));
                user.setUserNickName(name.getString("n"));
                userList.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return userList;
    }

}
