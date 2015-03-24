package async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import dbObjects.MakoEvent;
import dbObjects.MakoEventFact;

public class AsyncGetMakoEventsFacts extends AsyncTask<Void,Void,Boolean> {

    private Context context;
    private ArrayList<MakoEvent> makoEventsList;
    private ProgressDialog progressDialog;
    private BaseAdapter adapter;

    public AsyncGetMakoEventsFacts(Context context, ArrayList<MakoEvent> list, BaseAdapter adapter){
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
    protected Boolean doInBackground(Void... params) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("MakoEventFact");
        try {

            List<ParseObject> list = query.find();
            for (ParseObject i :list) {

                MakoEventFact fact = new MakoEventFact();

                String makoEventId = i.getString(MakoEventFact.MAKO_EVENT_ID_COL);

                fact.setFactID(i.getObjectId());
                fact.setMakoEventID(makoEventId);
                fact.setContent(i.getString(MakoEventFact.CONTENT_COL));
                fact.setContentType(i.getString(MakoEventFact.CONTENT_TYPE_COL));
                fact.setURL(i.getString(MakoEventFact.URL_COL));

                for (MakoEvent event :makoEventsList) {
                    if(!event.getId().equals(makoEventId))
                        continue;
                    event.addFact(fact);
                }

                Log.d("Parse", "added fact, id "+fact.getFactID());
            }

        } catch (ParseException e) {
            Log.d("Parse", "Fetching error");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    protected void onPostExecute(Boolean status) {
        progressDialog.dismiss();
        adapter.notifyDataSetChanged();
    }


}
