package com.vj.makosocial;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;


public class AppState extends Application {

    @Override
    public void onCreate() {
        // init Parse
        Parse.initialize(this, "vTOFv5b5IhCPhTrl0uqqCCXDiZSojjwt7FtzSMsU", "YAL4h7JMBz2gPClEnuQHXTyZv4R3YAnYV4Lt74JK");
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

}

