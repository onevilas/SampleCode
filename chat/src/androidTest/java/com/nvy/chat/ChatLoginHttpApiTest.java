package com.nvy.chat;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.nvy.chat.api.ChatLoginHttpApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.mock;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ChatLoginHttpApiTest extends ApplicationTestCase<Application> {


    public ChatLoginHttpApiTest() {
        super(Application.class);

    }


    @Test
    public void testCaseForServerCall() {
        ChatLoginHttpApi c = new ChatLoginHttpApi();

        try {
            JSONObject postData = new JSONObject();
            postData.put("u", "9035371510");
            postData.put("a", "little");
            postData.put("s", "abc");
            String json = postData.toString();
            String[] response = c.makeServerCall(getSystemContext(), json, "application/json");
            Log.e("Chat-response", response[0]);
            Log.e("Chat", response[1]);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}