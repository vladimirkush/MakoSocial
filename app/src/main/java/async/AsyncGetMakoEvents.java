package async;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import adapters.ListViewMakoAdapter;
import dataHolders.MakoListHolder;
import dbObjects.MakoEvent;

public class AsyncGetMakoEvents extends AsyncTask<Void,Void,ArrayList<MakoEvent>> {

    private final String NAME_COL           = "Name";
    private final String DESCRIPTION_COL    = "Description";
    private final String NUM_LIKES_COL      = "NumLikes";
    private final String START_DATE_COL     = "StartDate";
    private final String RATING_COL         = "Rating";
    private final String NUM_RATED_COL      = "numRated";
    private final String PICTURE_COL        = "Picture";
    private final String PARSE_LOGCAT_TAG   = "Parse";

    private Context context;
    private ArrayList<MakoEvent>    makoEventsList;
    private ProgressDialog          progressDialog;
    private ListViewMakoAdapter     adapter;

    // Constructor
    public AsyncGetMakoEvents(Context context, ArrayList<MakoEvent> list, ListViewMakoAdapter adapter){
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
                mEvent.setDescription(i.getString(DESCRIPTION_COL));
                mEvent.setLikes(i.getInt(NUM_LIKES_COL));
                mEvent.setName(i.getString(NAME_COL));
                mEvent.setRating((float)i.getDouble(RATING_COL));
                mEvent.setStartDate(i.getDate(START_DATE_COL));

                //get picture
                ParseFile file = (ParseFile) i.get(PICTURE_COL);
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
        progressDialog.dismiss();
        this.adapter.updateEntries(meList);
        // set meList to static holder field
        MakoListHolder.setmList(meList);
    }
}
