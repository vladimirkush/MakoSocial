package com.vj.makosocial;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


public class Dispathcer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dispathcer);

        ParseUser current_user = ParseUser.getCurrentUser();
        if(current_user != null && !ParseAnonymousUtils.isLinked(current_user))
            startActivity(new Intent(this, NavDrawerActivity.class));
        else
            startActivity(new Intent(this, Login.class));

    }

}
