package adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import dbObjects.MakoEvent;
import com.vj.makosocial.DetailedViewPicFragment;


public class DetailedViewPicAdapter extends FragmentStatePagerAdapter {
    private ArrayList<MakoEvent> mEvents;


    public DetailedViewPicAdapter(FragmentManager fm, ArrayList<MakoEvent> list) {
        super(fm);
        mEvents =list;
    }

    @Override
    public Fragment getItem(int i) {
        DetailedViewPicFragment fragment = new DetailedViewPicFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(DetailedViewPicFragment.ARG_OBJECT, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }
}
