package com.vj.makosocial;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.sql.RowSetListener;

import makosocial.NavDrawerActivity;


public class Login extends ActionBarActivity {

    EditText    login_username;
    EditText    login_password;
    Button      login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setViews();
        setListeners();
    }

    private void setViews() {
        setContentView(R.layout.activity_login);

        login_username  = (EditText) findViewById(R.id.login_username);
        login_password  = (EditText) findViewById(R.id.login_password);
        login_button    = (Button)   findViewById(R.id.login_btn);

    }

    private void setListeners() {

        login_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!loginValid()) {
                    Toast.makeText(getApplicationContext(), "Wrong login", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent(Login.this, NavDrawerActivity.class);
                startActivity(i);

            }

        });
    }

    private boolean loginValid() {

        // TODO: implement validation via parse

        if (login_username.length() == 0 ||
            login_password.length() == 0)
            return false;

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
