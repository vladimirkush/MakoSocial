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
            startActivityForResult(new Intent(this, NavDrawerActivity.class),0);
        else
            startActivityForResult(new Intent(this, Login.class),0);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            finish();
        }
    }

}
