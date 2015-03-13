package widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

import com.vj.makosocial.R;
import com.vj.makosocial.WidgetConfigActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static android.content.SharedPreferences.Editor;


public class MakoWidget extends AppWidgetProvider {

    public final static String LOG_TAG     =  "widget";
    public final static String FORMAT = "EEE MMM dd kk:mm";

    SharedPreferences sp;
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "onReceive");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate "  + Arrays.toString(appWidgetIds));

         sp = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, sp, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDelete "  + Arrays.toString(appWidgetIds));

        Editor editor = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            editor.remove(WidgetConfigActivity.WIDGET_NAME + widgetID );
            editor.remove(WidgetConfigActivity.WIDGET_DATE + widgetID);
            File f = new File(WidgetConfigActivity.WIDGET_FILENAME+widgetID+".png");
            f.delete();
            editor.remove(WidgetConfigActivity.WIDGET_PIC_PATH + widgetID);
        }
        editor.commit();
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                             SharedPreferences sp, int widgetID) {
        Log.d(LOG_TAG, "updateWidget " + widgetID);

        //read preferences

        String name = sp.getString(WidgetConfigActivity.WIDGET_NAME+widgetID,null);
        if (name==null) return;
        String dateString = sp.getString(WidgetConfigActivity.WIDGET_DATE+widgetID, null);
        if(dateString==null)
            Log.d(LOG_TAG, "*** DATESTRING = NULL ***");

        SimpleDateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zz yyyy");
        try {
            Date date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date date = new Date(dateString);
        String pic_path = sp.getString(WidgetConfigActivity.WIDGET_PIC_PATH+widgetID,null);
        Log.d(LOG_TAG, "Pic_path: "+ pic_path);
        try {
            FileInputStream fi = new FileInputStream(pic_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(pic_path);
        if(bitmap==null)
            Log.d(LOG_TAG, "*** BITMAP = NULL ***");


        RemoteViews widgetView = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        widgetView.setImageViewBitmap(R.id.iv_wg_pic, bitmap);
        widgetView.setTextViewText(R.id.tv_wg_title, name);
        widgetView.setTextViewText(R.id.tv_wg_timer, dateString);

        //updating widget
        appWidgetManager.updateAppWidget(widgetID, widgetView);

    }

}
