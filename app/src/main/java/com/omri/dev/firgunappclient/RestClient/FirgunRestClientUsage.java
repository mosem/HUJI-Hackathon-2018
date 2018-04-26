package com.omri.dev.firgunappclient.RestClient;

import org.json.*;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;


/**
 * Created by omri on 4/26/18.
 */

public class FirgunRestClientUsage {
    public static void getAllFirguns() throws JSONException {
        FirgunRestClient.get("posts/1", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                JSONObject o = response;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                JSONArray o = timeline;
//                // Pull out the first event on the public timeline
//                JSONObject firstEvent = timeline.get(0);
//                String tweetText = firstEvent.getString("text");
//
//                // Do something with the response
//                System.out.println(tweetText);
            }
        });
    }
}
