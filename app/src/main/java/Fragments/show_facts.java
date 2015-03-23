package Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.vj.makosocial.DetailedViewActivity;
import com.vj.makosocial.R;
import adapters.EventFactsListAdapter;
import dbObjects.MakoEvent;

public class show_facts extends Fragment {

    DetailedViewActivity parentActivity;
    private MakoEvent currMakoEvent;

    private View view;
    private ListView listView_facts;
    private Button goto_url;
    private EventFactsListAdapter listView_adapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static show_facts newInstance(String param1, String param2) {
        show_facts fragment = new show_facts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public show_facts() {
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

        view = inflater.inflate(R.layout.fragment_show_facts, container, false);
        listView_facts = (ListView) view.findViewById(R.id.list_of_facts);
        listView_adapter = new EventFactsListAdapter(currMakoEvent.getFacts(), getActivity().getBaseContext());
        listView_facts.setAdapter(listView_adapter);

        return view;
    }

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
