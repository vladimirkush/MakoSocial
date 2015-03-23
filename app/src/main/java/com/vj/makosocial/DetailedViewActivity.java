package com.vj.makosocial;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;

import Fragments.comment_on_show;
import Fragments.notify_widget;
import Fragments.rate_show;
import Fragments.show_facts;
import adapters.DetailedViewPicAdapter;
import animations.ZoomOutPageTransformer;
import dataHolders.MakoListHolder;
import dbObjects.MakoEvent;

public class DetailedViewActivity extends ActionBarActivity
        implements rate_show.OnFragmentInteractionListener,
        comment_on_show.OnFragmentInteractionListener,
        show_facts.OnFragmentInteractionListener,
        notify_widget.OnFragmentInteractionListener {

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
    private MakoEvent currMakoEvent;
    private ImageButton btn_rate;
    private ImageButton btn_comment;
    private ImageButton btn_facts;
    private ImageButton btn_notif_widg;
    private ImageButton selected_button;

    private SlidingUpPanelLayout mLayout;

    private FragmentManager fm;
    private Fragment frag_rate;
    private Fragment frag_comment;
    private Fragment frag_facts;
    private Fragment frag_notif_widg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view_layout);

//        clickedPos = getIntent().getIntExtra(POS_TAG,0);
        incIntent = getIntent();
        clickedPos = incIntent.getIntExtra(POS_TAG,-1);

        setViews();
        createFragments();
        setListeners();

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

                createFragments();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public MakoEvent getCurrMakoEvent() {
        return currMakoEvent;
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
        else if(id == android.R.id.home){
            Log.d(LOG_TAG, "up clicked");
            finish();
        }
        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    private void createFragments() {
        frag_rate = new rate_show();
        frag_comment = new comment_on_show();
        frag_facts = new show_facts();
        frag_notif_widg = new notify_widget();
        fm = getSupportFragmentManager();
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

    private void selectButton(ImageButton btn) {
        selected_button = btn;

        FragmentTransaction transaction = fm.beginTransaction();

        // Set Colors + Select Fragment
        if (btn_rate == btn) {
            deselectAllButtons();
            selected_button = btn;
            btn_rate.setBackgroundColor(Color.parseColor("#a85f5a"));
            transaction.replace(R.id.slide_up_content, frag_rate, "rate");
        }
        if (btn_comment == btn) {
            deselectAllButtons();
            selected_button = btn;
            btn_comment.setBackgroundColor(Color.parseColor("#ac866b"));
            transaction.replace(R.id.slide_up_content,frag_comment, "comment");
        }
        if (btn_facts == btn) {
            deselectAllButtons();
            selected_button = btn;
            btn_facts.setBackgroundColor(Color.parseColor("#7fa87f"));
            transaction.replace(R.id.slide_up_content,frag_facts, "facts");
        }
        if (btn_notif_widg == btn) {
            deselectAllButtons();
            selected_button = btn;
            btn_notif_widg.setBackgroundColor(Color.parseColor("#457a9c"));
            transaction.replace(R.id.slide_up_content,frag_notif_widg, "notif");
        }

        // Change fragment
        transaction.commit();

    }

    private void deselectAllButtons() {
        selected_button = null;
        btn_rate.setBackgroundColor(Color.parseColor("#6e4f4d"));
        btn_comment.setBackgroundColor(Color.parseColor("#6e594a"));
        btn_facts.setBackgroundColor(Color.parseColor("#4a6354"));
        btn_notif_widg.setBackgroundColor(Color.parseColor("#495a65"));

    }

    private void setListeners() {

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != PanelState.ANCHORED) {
                        mLayout.setAnchorPoint(0.1f);
                        mLayout.setPanelState(PanelState.ANCHORED);
                        selectButton(btn_rate);
                    }
                    else {
                        mLayout.setPanelState(PanelState.COLLAPSED);
                        deselectAllButtons();
                    }
                }

            }
        });
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != PanelState.EXPANDED) {
                        mLayout.setPanelState(PanelState.EXPANDED);
                        selectButton(btn_comment);
                    }
                    else if (selected_button != btn_comment) {
                        selectButton(btn_comment);
                    }
                    else {
                        mLayout.setPanelState(PanelState.COLLAPSED);
                        deselectAllButtons();
                    }
                }

            }
        });
        btn_facts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != PanelState.EXPANDED) {
                        mLayout.setPanelState(PanelState.EXPANDED);
                        selectButton(btn_facts);
                    }
                    else if (selected_button != btn_facts) {
                        selectButton(btn_facts);
                    }
                    else {
                        mLayout.setPanelState(PanelState.COLLAPSED);
                        deselectAllButtons();
                    }
                }

            }
        });
        btn_notif_widg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != PanelState.EXPANDED) {
                        mLayout.setPanelState(PanelState.EXPANDED);
                        selectButton(btn_notif_widg);
                    }
                    else if (selected_button != btn_notif_widg) {
                        selectButton(btn_notif_widg);
                    }
                    else {
                        mLayout.setPanelState(PanelState.COLLAPSED);
                        deselectAllButtons();
                    }
                }

            }
        });
    }

    private  void updateScreen(int pos){
        currMakoEvent = mEvents.get(pos);
        tvDescription.setText(currMakoEvent.getDescription());

    }

    @Override
    public void onBackPressed() {
        if (mLayout != null) {
            if (mLayout.getPanelState() != PanelState.COLLAPSED){
                mLayout.setPanelState(PanelState.COLLAPSED);
                deselectAllButtons();
            } else finish();
        } else finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
