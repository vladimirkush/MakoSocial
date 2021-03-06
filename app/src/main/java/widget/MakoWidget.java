package widget;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.vj.makosocial.Dispatcher;
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
    public final int WIDGET_UPDATE_MILLIS = 60000;

    private static boolean flag5min = true;
    private static boolean flag0min = true;
    private static RemoteViews widgetView;

    private SharedPreferences sp;

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
                WIDGET_UPDATE_MILLIS, pIntent);

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

        // read shared prefs
        sp = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        // catch broadcast message
        if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {
            ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);

            // update widgets
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

        // read shared prefs
        sp = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        // update widgets
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

        // delete shared prefs
        for (int widgetID : appWidgetIds) {
            editor.remove(WidgetConfigActivity.WIDGET_NAME + widgetID);
            editor.remove(WidgetConfigActivity.WIDGET_DATE + widgetID);
            File f = new File(WidgetConfigActivity.WIDGET_FILENAME + widgetID + ".png");
            f.delete();             //delete picture file from storage
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
        if (name == null) return;               // if no name - no prefs sent, exit

        // configure count-down timer
        Date eventDate = getFormattedDateFromSP(widgetID, sp);
        String timer = getDateDiff(eventDate);
        Log.d(LOG_TAG, "timer: " + timer);

        // get bitmap
        Bitmap bitmap = getBitmapFromSP(widgetID, sp);

        // fill widget with extracted data
        widgetView =  new RemoteViews(context.getPackageName(),R.layout.widget_layout);
        setWidgetViews(bitmap,name,timer);

        raiseNotification(timer, context, name, bitmap);

        // setting on click behaviour
        Intent configIntent = new Intent(context, NavDrawerActivity.class);
        setOnWidgetClick(context, widgetView, configIntent, widgetID);

        //updating widget
        appWidgetManager.updateAppWidget(widgetID, widgetView);
        Log.d(LOG_TAG, "widget " + widgetID + " updated");

    }

    private static void setWidgetViews(Bitmap bitmap, String name, String timer){

        widgetView.setImageViewBitmap(R.id.iv_wg_pic, bitmap);
        widgetView.setTextViewText(R.id.tv_wg_title, name);
        widgetView.setTextViewText(R.id.tv_wg_timer, timer);
    }

    private static Date getFormattedDateFromSP(int widgetID, SharedPreferences sp){

        String dateString = sp.getString(WidgetConfigActivity.WIDGET_DATE + widgetID, null);
        if (dateString == null)
            Log.d(LOG_TAG, "*** DATESTRING = NULL ***");
        Log.d(LOG_TAG, "dateString = " + dateString);
        Date eventDate = null;
        SimpleDateFormat format = new SimpleDateFormat(STD_FORMAT);
        format.setTimeZone(TimeZone.getDefault());
        try {
            eventDate = format.parse(dateString);

        } catch (ParseException e) {
            Log.d(LOG_TAG, "*** Parsing date exeption *** ");
            e.printStackTrace();
        }
        return eventDate;
    }

    private static Bitmap getBitmapFromSP (int widgetID, SharedPreferences sp){
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

        return bitmap;
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
                res = String.format("%dd %dh %dm", 0, 0, 0);
            else
                res = String.format("%dd %dh %dm", days, hours % 24, minutes % 60);
        }

        return res;
    }

    private static void setOnWidgetClick(Context ctx, RemoteViews v, Intent intent, int widgetID) {

        intent.setAction(Intent.CATEGORY_LAUNCHER);
        PendingIntent pIntent = PendingIntent.getActivity(ctx, widgetID,
                intent, 0);
        v.setOnClickPendingIntent(R.id.wg_layout, pIntent);
    }

    private static void raiseNotification(String timer, Context ctx, String title, Bitmap pic) {
        String descr = "";


        Log.d(LOG_TAG, "in Raise notification");
        if (timer.equals("0d 0h 5m") && flag5min) {
            Log.d(LOG_TAG, "5 min ready**");
            descr = "there is only 5 minutes left!";
            notify(ctx, pic, title, descr);

            flag5min=false; // disable further notifications

        } else if (timer.equals("0d 0h 0m") && flag0min) {
            Log.d(LOG_TAG, "0 min ready**");
            descr = "Starting now!";
            notify(ctx, pic, title, descr);

            flag0min=false; // disable further notifications
        }
    }

    private static void notify(Context ctx, Bitmap icon, String title, String desc) {
        Log.d(LOG_TAG, "in notify **");
        Intent intent = new Intent(ctx, Dispatcher.class);
        intent.setAction(Intent.CATEGORY_LAUNCHER);
        PendingIntent pIntent = PendingIntent.getActivity(ctx, 0,
                intent, 0);
        int notificationId = 001;


        Notification notif = new NotificationCompat.Builder(ctx).
                setSmallIcon(R.drawable.ic_launcher).
                setContentTitle(title).
                setContentText(desc).
                setLargeIcon(icon).
                setDefaults(Notification.DEFAULT_SOUND |Notification.DEFAULT_VIBRATE ).
                setContentIntent(pIntent).build();
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notifM = (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
        notifM.notify(notificationId, notif);
    }


}
