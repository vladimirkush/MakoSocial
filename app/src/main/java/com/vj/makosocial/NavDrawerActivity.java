package com.vj.makosocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.parse.ParseUser;

import java.util.ArrayList;

import adapters.ListViewMakoAdapter;
import async.AsyncGetMakoEvents;
import async.AsyncGetMakoEventsFacts;
import dbObjects.MakoEvent;

public class NavDrawerActivity extends ActionBarActivity {

    private static String POS_TAG  =    "position";
    private Drawer.Result       drawer_res;
    private Toolbar             toolbar;
    private ShareActionProvider mShareActionProvider;
    private ListView            lvMakoEvents;
    ArrayList<MakoEvent>        mEventList;
    ListViewMakoAdapter         adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);


        // Handle Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer_res = getDrawerResult();

        //list view
        lvMakoEvents = (ListView) findViewById(R.id.lvMakoEvents);

        //set adapter
        adapter = new ListViewMakoAdapter(this);
        lvMakoEvents.setAdapter(adapter);

        // download entities from Parse (asyncronic)
        mEventList = new ArrayList<MakoEvent>();
        AsyncGetMakoEvents asyncGetEvents = new AsyncGetMakoEvents(NavDrawerActivity.this,mEventList,adapter);
        asyncGetEvents.execute();

        AsyncGetMakoEventsFacts asyncGetFacts = new AsyncGetMakoEventsFacts(NavDrawerActivity.this, mEventList, adapter);
        asyncGetFacts.execute();

        lvMakoEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NavDrawerActivity.this, DetailedViewActivity.class);
                intent.putExtra(POS_TAG, position);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_nav_drawer, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem share = menu.findItem(R.id.menu_action_share);

        // Fetch and store ShareActionProvider
        //mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(share);
        //setShareIntent(getShareIntent());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //if (id == R.id.action_settings) {
        // return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer_res.isDrawerOpen()) {
            drawer_res.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }




    private Drawer.Result getDrawerResult(){

        Drawer.Result dr = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.nav_drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Our Shows")
                                .withIcon(FontAwesome.Icon.faw_list)
                                .withIdentifier(1),
                        new PrimaryDrawerItem().withName("TV News (in dev)")
                                .withIcon(FontAwesome.Icon.faw_square_o)
                                .withIdentifier(2),
                        new DividerDrawerItem(),

                        new PrimaryDrawerItem().withName("Tell Friends (in dev)")
                                .withIcon(FontAwesome.Icon.faw_user_plus)
                                .withIdentifier(3),
                        new PrimaryDrawerItem().withName("My Schedule (in dev)")
                                .withIcon(FontAwesome.Icon.faw_calendar)
                                .withIdentifier(4),
                        //Divider line
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIcon(FontAwesome.Icon.faw_sign_out)
                                .withName("Logout")
                                .withIdentifier(0)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    // click handle
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem == null) { // clicked on the header picture
                            Log.d("Parse", "Drawer NULL ");
                            return;
                        }
                        if (drawerItem.getIdentifier() == 0) {  //clicked "Logout"
                            ParseUser.logOut();
                            Intent i = new Intent(NavDrawerActivity.this, Dispatcher.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        } else {
                            Log.d("Parse", "Drawer id " + drawerItem.getIdentifier());
                        }
                    }
                }).build();
        return dr;
    }
}
