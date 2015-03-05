package com.vj.makosocial;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vj.makosocial.R;

import dataHolders.MakoListHolder;


public class DetailedViewPicFragment extends Fragment {

    public static final String ARG_OBJECT ="position" ;

    public DetailedViewPicFragment() {
        super();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V =  inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        Bundle args = getArguments();
        Bitmap pic;
        ImageView iv = (ImageView)V.findViewById(R.id.iv_frag);
        int position = args.getInt(ARG_OBJECT);
        pic = MakoListHolder.getmList().get(position).getPicture();
        iv.setImageBitmap(pic);

        return V;


    }
}
