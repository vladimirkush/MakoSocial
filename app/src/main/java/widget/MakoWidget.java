package widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

import com.vj.makosocial.NavDrawerActivity;
import com.vj.makosocial.R;
import com.vj.makosocial.WidgetConfigActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static android.content.SharedPreferences.Editor;


public class MakoWidget extends AppWidgetProvider {

    public final static String UPDATE_ALL_WIDGETS = "update_all_widgets";
    public final static String LOG_TAG =            "widget";
    public final static String STD_FORMAT =         "EEE MMM d HH:mm:ss zz yyyy";
    SharedPreferences sp;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");

        // start own broadcast each minute
        Intent intent = new Intent(context, MakoWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                10000, pIntent);

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        // stop own broadcast when last widget deleted
        Intent intent = new Intent(context, MakoWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "onReceive");
        sp = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {
            ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) {
                updateWidget(context, appWidgetManager, sp, appWidgetID);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));

        sp = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, sp, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDelete " + Arrays.toString(appWidgetIds));

        Editor editor = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            editor.remove(WidgetConfigActivity.WIDGET_NAME + widgetID);
            editor.remove(WidgetConfigActivity.WIDGET_DATE + widgetID);
            File f = new File(WidgetConfigActivity.WIDGET_FILENAME + widgetID + ".png");
            f.delete();
            editor.remove(WidgetConfigActivity.WIDGET_PIC_PATH + widgetID);
        }
        editor.commit();


    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                                    SharedPreferences sp, int widgetID) {
        Log.d(LOG_TAG, "updateWidget " + widgetID);

        if (sp == null) {
            Log.d(LOG_TAG, "* sp=NULL * ");
            return;
        }
        //read preferences

        String name = sp.getString(WidgetConfigActivity.WIDGET_NAME + widgetID, null);
        if (name == null) return;

        String dateString = sp.getString(WidgetConfigActivity.WIDGET_DATE + widgetID, null);
        if (dateString == null)
            Log.d(LOG_TAG, "*** DATESTRING = NULL ***");
        Log.d(LOG_TAG, "dateString = "+dateString);
        Date eventDate = null;
        SimpleDateFormat format = new SimpleDateFormat(STD_FORMAT);
        format.setTimeZone(TimeZone.getDefault());
        try {
            eventDate = format.parse(dateString);

        } catch (ParseException e) {
            Log.d(LOG_TAG, "*** Parsing date exeption *** ");
            e.printStackTrace();
        }

        String timer = getDateDiff(eventDate);
        Log.d(LOG_TAG, "timer: " + timer);


        String pic_path = sp.getString(WidgetConfigActivity.WIDGET_PIC_PATH + widgetID, null);
        Log.d(LOG_TAG, "Pic_path: " + pic_path);
        try {
            FileInputStream fi = new FileInputStream(pic_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(pic_path);
        if (bitmap == null)
            Log.d(LOG_TAG, "*** BITMAP = NULL ***");


        RemoteViews widgetView = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        widgetView.setImageViewBitmap(R.id.iv_wg_pic, bitmap);
        widgetView.setTextViewText(R.id.tv_wg_title, name);
        widgetView.setTextViewText(R.id.tv_wg_timer, timer);

        //updating widget
        Intent configIntent = new Intent(context, NavDrawerActivity.class);
        setOnWidgetClick(context,widgetView,configIntent,widgetID);

        appWidgetManager.updateAppWidget(widgetID, widgetView);
        Log.d(LOG_TAG, "widget " + widgetID +" updated");

    }

    private static String getDateDiff(Date eventDate) {
        String res = "";

        if (eventDate != null) {

            Date currDate = new Date();
            long diff = eventDate.getTime() - currDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (diff <= 0)
                res = String.format("%dd %dh %dm", 0, 0, 0 );

            res = String.format("%dd %dh %dm", days, hours % 24, minutes % 60);
        }

        return res;
    }

    private static void setOnWidgetClick(Context ctx,RemoteViews v, Intent intent, int widgetID){

        intent.setAction(Intent.CATEGORY_LAUNCHER);
        PendingIntent pIntent = PendingIntent.getActivity(ctx, widgetID,
                intent, 0);
        v.setOnClickPendingIntent(R.id.wg_layout, pIntent);
    }

}
