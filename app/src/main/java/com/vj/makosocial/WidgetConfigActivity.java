package com.vj.makosocial;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import adapters.WifgetConfigListAdapter;
import async.AsyncGetMakoEvents;
import dbObjects.MakoEvent;
import widget.MakoWidget;

import static android.content.SharedPreferences.Editor;


public class WidgetConfigActivity extends Activity {



    Intent                  resultValue;
    AlertDialog             dlg;
    ArrayList<MakoEvent>    mEventList;
    WifgetConfigListAdapter adapter;
    SharedPreferences       sp;

    int option = -1;
    Button okBtn;
    ListView lvEventList;

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;

    public final static String LOG_TAG =            "widget";
    public final static String WIDGET_PREF =        "widget_pref";
    public final static String WIDGET_NAME =        "widget_name_";
    public final static String WIDGET_DATE =        "widget_date_";
    public final static String WIDGET_PIC_PATH =    "widget_pic_path_";
    public final static String WIDGET_FILENAME =    "wg_pic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);
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
        }else{
            Log.d(LOG_TAG, "logged in");
            setViews();


            adapter = new WifgetConfigListAdapter(this);
            lvEventList.setAdapter(adapter);
            lvEventList.setSelector(R.drawable.wgconf_selector);

            // download entities from Parse (asynctask)
            mEventList = new ArrayList<MakoEvent>();

            AsyncGetMakoEvents async = new AsyncGetMakoEvents(WidgetConfigActivity.this,mEventList,adapter);
            async.execute();

            //set listener on option selected
            lvEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(LOG_TAG, "clicked lv pos: "+ position);
                   option = position;

                }
            });


        }




        // build response intent
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // negative response
        setResult(RESULT_CANCELED, resultValue);

    }



    private void setViews(){
        okBtn = (Button)findViewById(R.id.bt_wgconf_ok);
        lvEventList = (ListView)findViewById(R.id.lv_wgconf);

    }

    public void btnOKclick(View v) {
        // on OK button click - confirm widget creation
        WifgetConfigListAdapter ad = (WifgetConfigListAdapter) lvEventList.getAdapter();
        Log.d(LOG_TAG, "adapter size = " + ad.getCount());
        Log.d(LOG_TAG, "option " + option);

        String path="";
        if(option==-1)// if nothing selected
            Toast.makeText(getApplicationContext(), "Nothing checked, tap back to cancel",
                    Toast.LENGTH_SHORT).show();
        else{
            MakoEvent mEvent = (MakoEvent)ad.getItem(option);
            sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);

            Editor editor = sp.edit();
            editor.putString(WIDGET_NAME + widgetID, mEvent.getName() );
            editor.putString(WIDGET_DATE + widgetID, mEvent.getStartDate().toString());

           // Log.d(LOG_TAG, WIDGET_DATE + widgetID + " tostr: " + mEvent.getStartDate().toString());
            FileOutputStream fo =null;
            try {
                // save picture to a file

                File f = new File(this.getFilesDir(), WIDGET_FILENAME+widgetID+".png");
                path = f.getAbsolutePath();
                Log.d(LOG_TAG, "abs path: "+ path);
                fo = new FileOutputStream(f);

                mEvent.getPicture().compress(Bitmap.CompressFormat.PNG, 40,fo);

                fo.close();
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "FNF",
                        Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "IOE",
                        Toast.LENGTH_LONG).show();
            }
            editor.putString(WIDGET_PIC_PATH+widgetID, path);

           editor.commit();


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            MakoWidget.updateWidget(this, appWidgetManager, sp, widgetID);

            // finished config  - set positive response
            setResult(RESULT_OK, resultValue);

            Log.d(LOG_TAG, "finish config " + widgetID);
            finish();
        }



    }

}
