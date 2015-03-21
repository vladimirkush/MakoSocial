package com.vj.makosocial;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;


public class AppState extends Application {
    public final static String LOG_TAG =            "push";
    @Override
    public void onCreate() {
        // init Parse
        Parse.initialize(this, "vTOFv5b5IhCPhTrl0uqqCCXDiZSojjwt7FtzSMsU", "YAL4h7JMBz2gPClEnuQHXTyZv4R3YAnYV4Lt74JK");
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(LOG_TAG, "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e(LOG_TAG, "failed to subscribe for push", e);
                }
            }
        });
    }

}

