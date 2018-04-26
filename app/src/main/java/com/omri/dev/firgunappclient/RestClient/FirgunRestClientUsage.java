package com.omri.dev.firgunappclient.RestClient;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;


/**
 * Created by omri on 4/26/18.
 */

public class FirgunRestClientUsage {
    public static void getAllFirguns(JsonHttpResponseHandler handler) throws JSONException {
        FirgunRestClient.get("", null, handler);
    }
}
