package Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.vj.makosocial.DetailedViewActivity;
import com.vj.makosocial.R;

import java.util.HashMap;

import adapters.EventCommentsListAdapter;
import dbObjects.MakoEvent;

public class comment_on_show extends Fragment {

    DetailedViewActivity parentActivity;
    private MakoEvent currMakoEvent;

    private View view;
    private ListView listOfComments;
    EventCommentsListAdapter adapter;

    EditText editText_newComment;
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

        parentActivity = (DetailedViewActivity)getActivity();
        currMakoEvent = parentActivity.getCurrMakoEvent();

        view = inflater.inflate(R.layout.fragment_comment_on_show, container, false);
        editText_newComment = (EditText) view.findViewById(R.id.new_comment);
        sendComment = (Button) view.findViewById(R.id.send_comment);

        listOfComments = (ListView) view.findViewById(R.id.list_of_comments);
        adapter = new EventCommentsListAdapter(currMakoEvent.getComments(), getActivity().getBaseContext());
        listOfComments.setAdapter(adapter);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText_newComment == null)
                    return;

                if (editText_newComment.getText().toString().matches(""))
                    return;

                final HashMap<String, String> comment = new HashMap<String, String>();
                comment.put("whoCommented", ParseUser.getCurrentUser().getString("username"));
                comment.put("theComment", editText_newComment.getText().toString());
                currMakoEvent.getComments().add(comment);

                String id = currMakoEvent.getId();
                ParseObject pointer = ParseObject.createWithoutData("MakoEvent", id);
                pointer.put(MakoEvent.COMMENTS_COL, currMakoEvent.getComments());

                // Save
                pointer.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Saved successfully.
                            adapter.notifyDataSetChanged();
                            editText_newComment.setText("");
                            InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                        } else {
                            // The save failed.
                            currMakoEvent.getComments().remove(currMakoEvent.getComments().size() - 1);
                            Log.d("Update Comments Failed", e.toString());
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
