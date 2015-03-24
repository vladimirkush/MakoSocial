package Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.vj.makosocial.DetailedViewActivity;
import com.vj.makosocial.R;

import java.util.List;

import dbObjects.MakoEvent;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link notify_widget.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link notify_widget#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notify_widget extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = "subscribe";

    private static final String MAKO_EVENT_ID = "MakoEventID";
    private static final String MAKO_USER_ID = "UserID";
    private static final String SUBSCRIPTION_TABLE = "subscription";
    private static final String SUBSCRIBE = "Subscribe for news";
    private static final String UNSUBSCRIBE = "Unsubscribe";

    private final int UPDATE_TEXT_MILLIS = 320;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean subscr;
    private Handler handler;
    private DetailedViewActivity parentActivity;
    private MakoEvent currMakoEvent;
    private Button btnSubscribe;
    private View view;
    //private boolean subscribed;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment notify_widget.
     */
    // TODO: Rename and change types and number of parameters
    public static notify_widget newInstance(String param1, String param2) {
        notify_widget fragment = new notify_widget();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public notify_widget() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       // subscribed=false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notify, container, false);
        subscr=false;
        handler = new Handler();
        parentActivity = (DetailedViewActivity)getActivity();
        currMakoEvent = parentActivity.getCurrMakoEvent();

        btnSubscribe = (Button) view.findViewById(R.id.detview_bt_subscribe);
        btnSubscribe.setClickable(false);

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subscr){
                    unsubscribe();
                    setButtonText();
                }else{
                    subscribe();
                    setButtonText();
                }


            }
        });

        setButtonText();


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void subscribe(){
        String channel = currMakoEvent.getChannel();
        Log.d(LOG_TAG, "cliked subscr, Name " + channel);

        ParseUser user = ParseUser.getCurrentUser();
        String id = user.getObjectId();

            ParseObject subscription = new ParseObject(SUBSCRIPTION_TABLE);
            subscription.put(MAKO_USER_ID, id);
            subscription.put(MAKO_EVENT_ID, currMakoEvent.getId());
            subscription.saveInBackground();

            ParsePush.subscribeInBackground(channel, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(parentActivity, "error subscribing", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "error subscribing");
                    } else {
                        Toast.makeText(parentActivity, "successfull subscribing", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "successfull subscribing");
                    }
                }
            });
    }


    private void unsubscribe(){
        String channel = currMakoEvent.getChannel();
        Log.d(LOG_TAG, "cliked subscr, Name " + channel);

        ParseUser user = ParseUser.getCurrentUser();
        String id = user.getObjectId();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(SUBSCRIPTION_TABLE);
        query.whereStartsWith(MAKO_USER_ID, id);
        query.whereStartsWith(MAKO_EVENT_ID, currMakoEvent.getId());
        List<ParseObject> list=null;
        try {
             list =  query.find();
            if (list.size()==1) {
                list.get(0).deleteInBackground();
                Log.d(LOG_TAG, "deleting a pair from DB");
            }
        } catch (ParseException e) {
            Log.d(LOG_TAG, "in unsubscribe: error checking if a pair in DB");
            //e.printStackTrace();

        }


        ParsePush.unsubscribeInBackground(channel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(parentActivity, "error unsubscribing", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "error unsubscribing");
                } else {
                    Toast.makeText(parentActivity, "successfull unsubscribing", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "successfull unsubscribing");
                }
            }
        });

    }

    private void setButtonText(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnSubscribe.setClickable(false);
                subscr = false;
                String channel = currMakoEvent.getChannel();
                Log.d(LOG_TAG, "cliked subscr, Name " + channel);

                ParseUser user = ParseUser.getCurrentUser();
                String id = user.getObjectId();

                ParseQuery<ParseObject> query = ParseQuery.getQuery(SUBSCRIPTION_TABLE);
                query.whereStartsWith(MAKO_USER_ID, id);
                query.whereStartsWith(MAKO_EVENT_ID, currMakoEvent.getId());
                try {
                    List<ParseObject> list =  query.find();
                    if (list.size()==1)
                        subscr = true;
                } catch (ParseException e) {
                    Log.d(LOG_TAG, "error checking if a pair in DB");
                    //e.printStackTrace();
                    subscr=false;
                }

                if (subscr){
                    btnSubscribe.setText(UNSUBSCRIBE);
                }
                else{
                    btnSubscribe.setText(SUBSCRIBE);
                }
                btnSubscribe.setClickable(true);
            }
        },UPDATE_TEXT_MILLIS);
    }

}
