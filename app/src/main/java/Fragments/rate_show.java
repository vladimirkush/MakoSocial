package Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.vj.makosocial.DetailedViewActivity;
import com.vj.makosocial.R;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import dbObjects.MakoEvent;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link rate_show.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link rate_show#newInstance} factory method to
 * create an instance of this fragment.
 */
public class rate_show extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private RatingBar ratingBar;
    private TextView ratingNum;

    DetailedViewActivity parent;
    private MakoEvent currMakoEvent;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment rate_show.
     */
    // TODO: Rename and change types and number of parameters
    public static rate_show newInstance(String param1, String param2) {
        rate_show fragment = new rate_show();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public rate_show() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rate_show, container, false);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingNum = (TextView) view.findViewById(R.id.ratingNum);
        ratingNum.setText(""+ratingBar.getRating());

        parent = (DetailedViewActivity)getActivity();
        currMakoEvent = parent.getCurrMakoEvent();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // TODO: update on database

                float curRating = currMakoEvent.getRating();
                int curNumRated = currMakoEvent.getNumRated();
                final float userRated = ratingBar.getRating();
                int newNumRated = curNumRated + 1;
                final float newRating = ((curRating*curNumRated)+userRated)/newNumRated;
//                ((curRating + userRated/newNumRated) <= 5) ?
//                        curRating + userRated/newNumRated : 5;

                String id = currMakoEvent.getId();
                ParseObject pointer = ParseObject.createWithoutData("MakoEvent", id);
                pointer.put(MakoEvent.RATING_COL, Float.parseFloat(new DecimalFormat("#.#").format(newRating)));
                pointer.put(MakoEvent.NUM_RATED_COL, newNumRated);

                // Save
                pointer.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Saved successfully.
                            ratingNum.setText(new DecimalFormat("#.#").format(userRated));
                        } else {
                            // The save failed.
                            Log.d("Update Rating", e.toString());
                        }
                    }
                });

            }
        });

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

}
