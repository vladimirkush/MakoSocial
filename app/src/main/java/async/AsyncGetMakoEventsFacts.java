package async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import dbObjects.MakoEvent;
import dbObjects.MakoEventFact;

public class AsyncGetMakoEventsFacts extends AsyncTask<Void,Void,ArrayList<MakoEventFact>> {

    private Context context;
    private ArrayList<MakoEventFact> factsList;
    private ProgressDialog progressDialog;
    private BaseAdapter adapter;

    @Override
    protected ArrayList<MakoEventFact> doInBackground(Void... params) {
        return null;
    }
}
