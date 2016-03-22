package com.nvy.chat;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.nvy.chat.api.ChatLoginHttpApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.mock;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest extends ApplicationTestCase<Application> {


    Context mMockContext;

    public ApplicationTest() {
        super(Application.class);
        mMockContext=getApplication();
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockContext=getSystemContext();
    }

    @Test
    public void testCaseForServerCall(){
        ChatLoginHttpApi c=new ChatLoginHttpApi();



        try {
            JSONObject postData = new JSONObject();
            postData.put("u", "user id of user");
            postData.put("a", "app id");
            postData.put("s", "app secret key");
            String json = postData.toString();
            String[] response= c.makeServerCall(getSystemContext() ,json, "application/json");
            Log.i("Chat-response", response[0]);
            Log.i("Chat",response[1]);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}