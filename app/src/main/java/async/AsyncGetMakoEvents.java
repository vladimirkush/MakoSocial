package async;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import adapters.ListViewMakoAdapter;
import adapters.WifgetConfigListAdapter;
import dataHolders.MakoListHolder;
import dbObjects.MakoEvent;

public class AsyncGetMakoEvents extends AsyncTask<Void,Void,ArrayList<MakoEvent>> {

    public final static String PARSE_LOGCAT_TAG   = "Parse";
    public final static String WIDGET_LOGCAT_TAG  = "widget";

    private Context context;
    private ArrayList<MakoEvent>    makoEventsList;
    private ProgressDialog          progressDialog;
    private BaseAdapter             adapter;

    // Constructor
    public AsyncGetMakoEvents(Context context, ArrayList<MakoEvent> list, BaseAdapter adapter){
        this.context = context;
        this.makoEventsList = list;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Fetching Mako data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected ArrayList<MakoEvent> doInBackground(Void ...params) {

        // Fetch objects from Mako server
        // build and return a list of MakoEvent objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MakoEvent");
        try {

            List<ParseObject> list = query.find();

            // creating list of MakoEvents
            for (ParseObject i :list) {
                MakoEvent mEvent = new MakoEvent();

                mEvent.setId(i.getObjectId());
                mEvent.setDescription(i.getString(MakoEvent.DESCRIPTION_COL));
                mEvent.setShortDescription(i.getString(MakoEvent.SHORD_DESC_COL));
                mEvent.setLikes(i.getInt(MakoEvent.NUM_LIKES_COL));
                mEvent.setName(i.getString(MakoEvent.NAME_COL));
                mEvent.setRating((float)i.getDouble(MakoEvent.RATING_COL));
                mEvent.setStartDate(i.getDate(MakoEvent.START_DATE_COL));
                mEvent.setNumRated(i.getInt(MakoEvent.NUM_RATED_COL));
                try {
                    mEvent.populateComments(i.getJSONArray(MakoEvent.COMMENTS_COL));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //get picture
                ParseFile file = (ParseFile) i.get(MakoEvent.PICTURE_COL);
                try {
                    byte[] picBytes = file.getData();
                    Bitmap bmp = BitmapFactory.decodeByteArray(picBytes, 0, picBytes.length);
                    mEvent.setPicture(bmp);
                    Log.d(PARSE_LOGCAT_TAG, "Success in downloading picture");
                } catch (ParseException e1) {
                    Log.d(PARSE_LOGCAT_TAG, "Error downloading picture");
                }

                makoEventsList.add(mEvent);
                Log.d(PARSE_LOGCAT_TAG, "added mEvent, id "+mEvent.getId());
            }
        } catch (ParseException e) {
            Log.d(PARSE_LOGCAT_TAG,"Fetching error");
            e.printStackTrace();
        }

        return makoEventsList;
    }

    @Override
    protected void onPostExecute(ArrayList<MakoEvent> meList) {
        Log.d(WIDGET_LOGCAT_TAG, "async finished");
        progressDialog.dismiss();
        if(adapter instanceof ListViewMakoAdapter)
            ((ListViewMakoAdapter)this.adapter).updateEntries(meList);
        else if(adapter instanceof WifgetConfigListAdapter) {
            Log.d(WIDGET_LOGCAT_TAG, "updating WifgetConfigListAdapter with values");
            ((WifgetConfigListAdapter) this.adapter).updateEntries(meList);
        }
        // set meList to static holder field
        MakoListHolder.setmList(meList);
    }
}
