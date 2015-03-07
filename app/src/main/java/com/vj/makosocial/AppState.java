package com.vj.makosocial;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseUser;


public class AppState extends Application {

    @Override
    public void onCreate() {
        Parse.initialize(this, "vTOFv5b5IhCPhTrl0uqqCCXDiZSojjwt7FtzSMsU", "YAL4h7JMBz2gPClEnuQHXTyZv4R3YAnYV4Lt74JK");
        
    }

}

