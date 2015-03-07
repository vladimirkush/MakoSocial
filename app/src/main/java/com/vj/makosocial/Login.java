package com.vj.makosocial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.sql.RowSetListener;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.vj.makosocial.NavDrawerActivity;


public class Login extends ActionBarActivity {

    private final int MODE_LOGIN     = 100;
    private final int MODE_SIGNUP    = 101;

    EditText        login_username;
    EditText        login_password;
    LinearLayout    cont_repeat_password;
    EditText        login_repeat_password;
    Button          login_button;
    TextView        login_signup;
    Button          back;

    ProgressDialog  dlg;

    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mode = MODE_LOGIN;

        setViews();
        setListeners();
    }

    private void setViews() {
        setContentView(R.layout.activity_login);

        login_username          = (EditText)        findViewById(R.id.login_username);
        login_password          = (EditText)        findViewById(R.id.login_password);
        cont_repeat_password    = (LinearLayout)    findViewById(R.id.login_container_repeat_password);
        login_repeat_password   = (EditText)        findViewById(R.id.login_repeat_password);
        login_button            = (Button)          findViewById(R.id.login_btn);
        login_signup            = (TextView)        findViewById(R.id.login_signup);
        back                    = (Button)          findViewById(R.id.back_to_login_btn);

        dlg = new ProgressDialog(Login.this);
    }

    private void setListeners() {

        login_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch(mode) {
                    case MODE_LOGIN:
                        login();
                        break;
                    case MODE_SIGNUP:
                        signup();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
                }

            }

        });

        login_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mode = MODE_SIGNUP;

                cont_repeat_password.setVisibility(View.VISIBLE);
                login_button.setText("SIGN UP");
                login_signup.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mode = MODE_LOGIN;

                cont_repeat_password.setVisibility(View.GONE);
                login_repeat_password.setText("");
                login_button.setText("LOGIN");
                login_signup.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);

            }
        });
    }

    private void login() {

        if (!fieldsAreValid())
            return;

        dlg.setTitle("Logging in");
        dlg.setMessage("Please wait...");
        dlg.show();
        ParseUser.logInInBackground(
                login_username.getText().toString().trim(),
                login_password.getText().toString().trim(),
                new LogInCallback() {

                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        dlg.dismiss();
                        if(e!=null)
                            Toast.makeText(getApplicationContext(), e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        else {
                            Intent i = new Intent(Login.this, Dispathcer.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    }

                });
    }

    private void signup() {

        if (!fieldsAreValid())
            return;

        dlg.setTitle("Signing up");
        dlg.setMessage("Please wait...");
        dlg.show();

        ParseUser user = new ParseUser();
        user.setUsername(login_username.getText().toString().trim());
        user.setPassword(login_password.getText().toString().trim());
        user.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {
                dlg.dismiss();
                if(e!=null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Login.this, Dispathcer.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

        });

    }

    private boolean fieldsAreValid() {

        if(isEmpty(login_username) || isEmpty(login_password)) {
            Toast.makeText(getApplicationContext(), "Please fill in the fields",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mode == MODE_SIGNUP && !doPasswordsMatch(login_password, login_repeat_password)) {
            Toast.makeText(getApplicationContext(), "Passwords dont match",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private boolean isEmpty(EditText et) {

        return (et.getText().toString().trim().length() == 0);

    }

    private boolean doPasswordsMatch(EditText et1, EditText et2) {

        return et1.getText().toString().trim().equals(et2.getText().toString().trim());
    }

}
