package com.vj.makosocial;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.DetailedViewPicAdapter;
import animations.ZoomOutPageTransformer;
import dataHolders.MakoListHolder;
import dbObjects.MakoEvent;


public class DetailedViewActivity extends ActionBarActivity {

    private static String POS_TAG = "position";
    private Toolbar toolbar;
    private ArrayList<MakoEvent> mEvents;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private int clickedPos;
    private Intent incIntent;
    private ActionBar aBar;
    private ShareActionProvider mShareActionProvider;

    private final static String LOG_TAG = "DetailedView";

    private TextView tvDescription;
//    private TextView tvRating;
//    private TextView tvLikes;
//    private TextView tvComments;

    private MakoEvent currMakoEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view_layout);

        incIntent = getIntent();
        clickedPos = incIntent.getIntExtra(POS_TAG,-1);
        setViews();

        mEvents = MakoListHolder.getmList();
        if (MakoListHolder.isEmpty())
            Log.d(LOG_TAG ,"mEvents static field is empty");
        else
            Log.d(LOG_TAG ,"length "+ mEvents.size());

        //currMakoEvent = mEvents.get(clickedPos);
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
        //--
        //Menu menu= toolbar.getMenu();
        //MenuItem share= menu.findItem(R.id.menu_action_share);
        //share.setIntent(getShareIntent("pos: "+ clickedPos));

        //--

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                updateScreen(i);

                // set share content
                getSupportActionBar().setTitle(currMakoEvent.getName());
                mShareActionProvider.setShareIntent(getShareIntent(currMakoEvent.getLink()));
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

        // Locate MenuItem with ShareActionProvider
        MenuItem share = menu.findItem(R.id.menu_action_share);



        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(share);
        setShareIntent(getShareIntent(currMakoEvent.getLink()));



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "in OnOptionClick");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_action_share) {
            //setShareIntent(getShareIntent("stam1"));
            Log.d(LOG_TAG, "menu clicked item id: "+id);
        }
        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    private void setViews(){
        tvDescription = (TextView)findViewById(R.id.tv_det_view_Descr);
//        tvComments = (TextView)findViewById(R.id.tv_det_view_comments);
//        tvRating = (TextView)findViewById(R.id.tv_det_view_rating);
//        tvLikes = (TextView)findViewById(R.id.tv_det_view_likes);
    }

    private  void updateScreen(int pos){
        currMakoEvent = mEvents.get(pos);

        tvDescription.setText(currMakoEvent.getDescription());
//        tvRating.setText(currMakoEvent.getRating()+"");
//        tvComments.setText(currMakoEvent.getNumComments()+"");
//        tvLikes.setText(currMakoEvent.getLikes()+"");

    }



    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent getShareIntent( String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setType("text/plain");
        return shareIntent;
    }


}
