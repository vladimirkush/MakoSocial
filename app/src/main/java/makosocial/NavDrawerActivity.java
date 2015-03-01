package makosocial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.vj.makosocial.R;

import java.util.ArrayList;
import java.util.List;

import adapters.ListViewMakoAdapter;
import dbObjects.MakoEvent;

// Mike Penz lib

public class NavDrawerActivity extends ActionBarActivity {
    private final String NAME_COL = "Name";
    private final String DESCRIPTION_COL = "Description";
    private final String NUM_LIKES_COL = "NumLikes";
    private final String START_DATE_COL = "StartDate";
    private final String RATING_COL = "Rating";
    private final String NUM_RATED_COL = "numRated";
    private final String PICTURE_COL = "Picture";

    private final String PARSE_LOGCAT_TAG = "Parse";

    private Drawer.Result drawer_res;
    private Toolbar toolbar;
    private ShareActionProvider mShareActionProvider;
    private ListView lvMakoEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        // test parse auth
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        // init Parse session
        Parse.initialize(this, "vTOFv5b5IhCPhTrl0uqqCCXDiZSojjwt7FtzSMsU", "YAL4h7JMBz2gPClEnuQHXTyZv4R3YAnYV4Lt74JK");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


        /*/uploading some db rows for test*************************
        ParseObject makoEvent = new ParseObject("MakoEvent");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rahamim);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile("rahamim.png", image);
        // Upload the image into Parse Cloud
        file.saveInBackground();

        makoEvent.put("Picture", file);
        makoEvent.put("Name","Rahamim");

        Date myDate = new Date();

        makoEvent.put("StartDate",myDate);
        makoEvent.put("NumLikes",0);
        makoEvent.put("Description","He didn't know...");
        makoEvent.put("Rating",5);
        makoEvent.put("numRated",1);
        makoEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Toast.makeText(NavDrawerActivity.this, "Success", Toast.LENGTH_SHORT).show();
                else {
                    e.printStackTrace();
                    Toast.makeText(NavDrawerActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //end uploading rows****************************/


        // Handle Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Using Mike Penz material design library:
        * https://github.com/mikepenz/MaterialDrawer
        */

        drawer_res = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.nav_drawer_header)
                .addDrawerItems(
                        //"List view"
                        new PrimaryDrawerItem().withName(R.string.drawer_item_list_view)
                                .withIcon(FontAwesome.Icon.faw_list)
                                .withIdentifier(1),
                        //"Full view"
                        new PrimaryDrawerItem().withName(R.string.drawer_item_full_view)
                                .withIcon(FontAwesome.Icon.faw_square_o)
                                .withIdentifier(2),

                        //Divider line
                        new DividerDrawerItem(),

                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom1)
                                .withIcon(FontAwesome.Icon.faw_arrow_circle_right)
                                .withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom2)
                                .withIcon(FontAwesome.Icon.faw_arrow_circle_left)
                                .withIdentifier(4),
                        //new SectionDrawerItem()
                        //.withName(R.string.drawer_item_settings),

                        //Divider line
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_help)
                                .withIcon(FontAwesome.Icon.faw_question_circle)
                                .withIdentifier(4)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    // click handle
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            // Action on click
                            Toast.makeText(NavDrawerActivity.this, NavDrawerActivity.this.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).build();
        // download entities from Parse
        ArrayList<MakoEvent> mEventList = getMakoEvents();
        // list view
        lvMakoEvents = (ListView) findViewById(R.id.lvMakoEvents);
        lvMakoEvents.setAdapter(new ListViewMakoAdapter(mEventList, this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_nav_drawer, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem share = menu.findItem(R.id.menu_action_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(share);
        setShareIntent(getShareIntent());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


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

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent getShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        shareIntent.setType("text/plain");
        return shareIntent;
    }

    private ArrayList<MakoEvent> getMakoEvents(){
        // fetch objects from Mako side
        // and return a list of MakoEvent objects
        final ArrayList<MakoEvent> meList = new ArrayList<MakoEvent>();

        ProgressDialog progressDialog = ProgressDialog.show(NavDrawerActivity.this, "",
                "Fetching Mako data...", true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MakoEvent");
        try {
            List<ParseObject> list = query.find();
            // creating list of MakoEvents
            for (ParseObject i :list) {
                MakoEvent mEvent = new MakoEvent();

                mEvent.setId(i.getObjectId());
                mEvent.setDescription(i.getString(DESCRIPTION_COL));
                mEvent.setLikes(i.getInt(NUM_LIKES_COL));
                mEvent.setName(i.getString(NAME_COL));
                mEvent.setRating((float)i.getDouble(RATING_COL));
                mEvent.setStartDate(i.getDate(START_DATE_COL));

                //get picture
                ParseFile file = (ParseFile) i.get(PICTURE_COL);
                try {
                    byte[] picBytes = file.getData();
                    Bitmap bmp = BitmapFactory.decodeByteArray(picBytes, 0, picBytes.length);
                    mEvent.setPicture(bmp);
                    Log.d(PARSE_LOGCAT_TAG, "Success in downloading picture");
                } catch (ParseException e1) {
                    Log.d(PARSE_LOGCAT_TAG, "Error downloading picture");
                }

                meList.add(mEvent);
                Log.d(PARSE_LOGCAT_TAG, "added mEvent, id "+mEvent.getId());
            }
        } catch (ParseException e) {
            Log.d(PARSE_LOGCAT_TAG,"Fetching error");
            e.printStackTrace();
        }


        progressDialog.dismiss();
        return  meList;
    }

}
