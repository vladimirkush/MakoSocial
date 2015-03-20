package Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.vj.makosocial.DetailedViewActivity;
import com.vj.makosocial.R;

import java.text.DecimalFormat;
import java.util.HashMap;

import adapters.EventCommentsListAdapter;
import dbObjects.MakoEvent;

public class comment_on_show extends Fragment {

    DetailedViewActivity parent;
    private MakoEvent currMakoEvent;

    private View view;
    private ListView listOfComments;
    EventCommentsListAdapter adapter;

    EditText newComment;
    Button sendComment;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static comment_on_show newInstance(String param1, String param2) {
        comment_on_show fragment = new comment_on_show();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public comment_on_show() {
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

        parent = (DetailedViewActivity)getActivity();
        currMakoEvent = parent.getCurrMakoEvent();

        view = inflater.inflate(R.layout.fragment_comment_on_show, container, false);
        newComment = (EditText) view.findViewById(R.id.new_comment);
        sendComment = (Button) view.findViewById(R.id.send_comment);

        listOfComments = (ListView) view.findViewById(R.id.list_of_comments);
        adapter = new EventCommentsListAdapter(currMakoEvent.getComments(), getActivity().getBaseContext());
        listOfComments.setAdapter(adapter);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newComment == null)
                    return;

                if (newComment.getText().toString() == "")
                    return;

                final HashMap<String, String> comment = new HashMap<String, String>();
                comment.put("key","value");
                currMakoEvent.getComments().add(comment);

                String updatedComments = currMakoEvent.getComments().toString();

                String id = currMakoEvent.getId();
                ParseObject pointer = ParseObject.createWithoutData("MakoEvent", id);
                pointer.put(MakoEvent.COMMENTS_COL, updatedComments);

                // TODO: comments not updated on parse.com.
                // Save
                pointer.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // TODO: new comment not showing. keyboard not closed.
                            // Saved successfully.
                            currMakoEvent.getComments().add(comment);
                            adapter.notifyDataSetChanged();
                        } else {
                            // The save failed.
                            Log.d("Update Comments", e.toString());
                        }
                    }
                });

            }
        });

        // Inflate the layout for this fragment
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
