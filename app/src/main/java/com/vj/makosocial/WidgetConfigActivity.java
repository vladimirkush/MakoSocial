package com.vj.makosocial;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


public class WidgetConfigActivity extends Activity {
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    AlertDialog dlg;

    final String LOG_TAG =                      "widget";
    public final static String WIDGET_PREF =    "widget_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        // retreive widget ID
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // check if it's correct
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Log.d(LOG_TAG, "widgetID incorrect");
            finish();
        }

        // check if logged in
        ParseUser current_user = ParseUser.getCurrentUser();
        if(current_user == null || ParseAnonymousUtils.isLinked(current_user)){
            Log.d(LOG_TAG, "not logined");
            dlg = new AlertDialog.Builder(WidgetConfigActivity.this).create();

            dlg.setMessage("Please log in/sign up to the application first");
            // Setting OK Button
            dlg.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dlg.dismiss();
                    finish();
                }
            });

            dlg.show();
        }

        Log.d(LOG_TAG, "logined");


        // build response intent
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // negative response
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.activity_widget_config);
    }





}
