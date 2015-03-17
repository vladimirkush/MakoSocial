package com.vj.makosocial;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.DetailedViewPicAdapter;
import animations.ZoomOutPageTransformer;
import dataHolders.MakoListHolder;
import dbObjects.MakoEvent;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;



public class DetailedViewActivity extends ActionBarActivity {

    private static String POS_TAG = "position";
    private Toolbar toolbar;
    private ArrayList<MakoEvent> mEvents;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private int clickedPos;
    private Intent incIntent;

    private TextView tvDescription;
    private MakoEvent currMakoEvent;
    private ImageButton btn_rate;
    private ImageButton btn_comment;
    private ImageButton btn_facts;
    private ImageButton btn_notif_widg;

    private SlidingUpPanelLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view_layout);

        incIntent = getIntent();
        clickedPos = incIntent.getIntExtra(POS_TAG,0);
        setViews();
        setListeners();

        mEvents = MakoListHolder.getmList();
        if (MakoListHolder.isEmpty())
            Log.d("DetailedView:" ,"mEvents static field is empty");
        else
            Log.d("DetailedView:" ,"length "+ mEvents.size());

        currMakoEvent = mEvents.get(clickedPos);
        updateScreen(clickedPos);

        // set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mEvents.get(clickedPos).getName());

        viewPager = (ViewPager)findViewById(R.id.vp_pager);
        pagerAdapter = new DetailedViewPicAdapter(getSupportFragmentManager(), mEvents);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setCurrentItem(clickedPos);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                updateScreen(i);
                getSupportActionBar().setTitle(currMakoEvent.getName());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed_view_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    private void setViews(){
        tvDescription = (TextView)findViewById(R.id.tv_det_view_Descr);

        // SlidingPanel settings
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelState(PanelState.COLLAPSED);
        mLayout.setTouchEnabled(false);

        btn_rate = (ImageButton) findViewById(R.id.btn_rate);
        btn_comment = (ImageButton) findViewById(R.id.btn_comment);
        btn_facts = (ImageButton) findViewById(R.id.btn_facts);
        btn_notif_widg = (ImageButton) findViewById(R.id.btn_notif_and_widget);

    }

    private void setListeners() {
        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != PanelState.ANCHORED) {
                        mLayout.setAnchorPoint(0.1f);
                        mLayout.setPanelState(PanelState.ANCHORED);
                    }
                    else
                        mLayout.setPanelState(PanelState.COLLAPSED);
                }

            }
        });
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != PanelState.EXPANDED)
                        mLayout.setPanelState(PanelState.EXPANDED);
                    else
                        mLayout.setPanelState(PanelState.COLLAPSED);
                }

            }
        });
        btn_facts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != PanelState.EXPANDED)
                        mLayout.setPanelState(PanelState.EXPANDED);
                    else
                        mLayout.setPanelState(PanelState.COLLAPSED);
                }

            }
        });
        btn_notif_widg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != PanelState.EXPANDED)
                        mLayout.setPanelState(PanelState.EXPANDED);
                    else
                        mLayout.setPanelState(PanelState.COLLAPSED);
                }

            }
        });
    }

    private  void updateScreen(int pos){
        currMakoEvent = mEvents.get(pos);
        tvDescription.setText(currMakoEvent.getDescription());

    }
}
