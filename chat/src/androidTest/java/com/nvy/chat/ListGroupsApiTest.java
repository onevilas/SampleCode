package com.nvy.chat;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.nvy.chat.api.ListGroupsApi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

/**
 * Created by zovi on 2/12/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class ListGroupsApiTest extends ApplicationTestCase<Application> {


    public ListGroupsApiTest() {
        super(Application.class);

    }


    @Test
    public void testCaseForServerCall() {
        ListGroupsApi c = new ListGroupsApi();

        try {
            String[] response = c.makeServerCall(getSystemContext(), null, "");
            Log.e("Chat-response", response[0]);
            Log.e("Chat", response[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
