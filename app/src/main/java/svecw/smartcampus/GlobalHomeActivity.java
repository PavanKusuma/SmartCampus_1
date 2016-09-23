package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import adapters.KnowledgeWallAdapter;
import internaldb.SmartCampusDB;
import model.GlobalInfo;
import utils.ConnectionDetector;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan on 12/29/15.
 */
public class GlobalHomeActivity extends AppCompatActivity {

    // bottom navigation views
    LinearLayout collegeWallView, studentWallView, alumniWallView, moreView;
    TextView collegeWallViewText, studentWallViewText, alumniWallViewText, moreViewText;

    // internal db object
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    // list for storing global wall posts
    public static ArrayList<GlobalInfo> knowledgeWallPosts;
    public static KnowledgeWallAdapter knowledgeWallAdapter;

    // date format for displaying created date
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");

    // layout inflator
    LayoutInflater layoutInflater;
    RelativeLayout l;
    ProgressBar knowledgeWallProgress;

    ViewPager pager;
    TabLayout tabLayout;

    JSONObject jsonResponse;
    int status;
    ViewPagerAdapter adapter;

    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_home_activity);


        // get the tool bar and set the support ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        //toolbar.setTitle(R.string.knowledgeWall);
        toolbar.setTitleTextColor(getResources().getColor(R.color.title_color));



        pager = (ViewPager) findViewById(R.id.viewPager);
        knowledgeWallProgress = (ProgressBar) findViewById(R.id.knowledgeWallProgress);
        //setupViewPager(pager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(pager);
        //tabLayout.setTabTextColors(getResources().getColor(R.color.title_color), getResources().getColor(R.color.title_color));

        // object for globalPosts
        knowledgeWallPosts = new ArrayList<GlobalInfo>();

        // we are displaying top top 10 views, so the skipCounter is set to 0
        String globalInfoURL = Routes.getGlobalInfo + Constants.key + "/" + 0;

        // object for ConnectionDetector
        connectionDetector = new ConnectionDetector(getApplicationContext());

        // check if internet is working
        // else show no network toast
        if(connectionDetector.isInternetWorking()) {

            // get the posts on load of the activity
            //new GetGlobalInfo().execute(Routes.getGlobalInfo, String.valueOf(0));

            callGlobalInfo(0);

        } else {

            // disable progress and show connection status
            knowledgeWallProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
        }

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // bottom bar views
        collegeWallView = (LinearLayout) findViewById(R.id.collegeWallView);
        studentWallView = (LinearLayout) findViewById(R.id.studentWallView);
        alumniWallView = (LinearLayout) findViewById(R.id.alumniWallView);
        moreView = (LinearLayout) findViewById(R.id.moreView);

        collegeWallViewText = (TextView) findViewById(R.id.collegeWallViewText); collegeWallViewText.setTypeface(sansFont);
        studentWallViewText = (TextView) findViewById(R.id.studentWallViewText); studentWallViewText.setTypeface(sansFont);
        alumniWallViewText = (TextView) findViewById(R.id.alumniWallViewText); alumniWallViewText.setTypeface(sansFont);
        moreViewText = (TextView) findViewById(R.id.moreViewText); moreViewText.setTypeface(sansFont);

        //collegeWallViewText.setTypeface(typefaceOpenSans);
        //studentWallViewText.setTypeface(typefaceOpenSans);

        // show activities on click of corresponding views from home activity
        collegeWallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // navigate to college wall activity
                Intent collegeWallIntent = new Intent(getApplicationContext(), CollegeWallActivity.class);
                startActivity(collegeWallIntent);

            }
        });


        studentWallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // navigate to student wall activity
                Intent studentWallIntent = new Intent(getApplicationContext(), StudentActivity.class);
                startActivity(studentWallIntent);
            }
        });

        alumniWallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if the user is student, then he will be see messages from faculty
                Intent alumniWallIntent = new Intent(getApplicationContext(), AlumniWallActivity.class);
                startActivity(alumniWallIntent);

            }
        });

        moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent moreIntent = new Intent(getApplicationContext(), MoreActivity.class);
                startActivity(moreIntent);

            }
        });


    }


    // call GlobalInfo
    public void callGlobalInfo(int skip){

        new GetGlobalInfo().execute(Routes.getGlobalInfo, String.valueOf(skip));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);


        if(!smartCampusDB.getUserRole().contentEquals(Constants.admin)) {
            menu.findItem(R.id.createWallPost).setVisible(false);
        }


        //menu.findItem(R.id.userProfile).setVisible(false);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.createWallPost) {

            // navigate to selectNewPostActivity to show posting options based on privileges
            Intent newPostIntent = new Intent(getApplicationContext(), KnowledgeWallNewPostActivity.class);
            //startActivity(newPostIntent);
            startActivityForResult(newPostIntent, 200);

            return true;
        }
        /*else if (id == R.id.userProfile) {

            // navigate to selectNewPostActivity to show posting options based on privileges
            Intent profileIntent = new Intent(getApplicationContext(), Global_Notifications_Activity.class);
            startActivity(profileIntent);

            return true;
        }*/


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the result is capturing Image
        if(requestCode == 200){

            // check if the post is created
            if(resultCode == 1){

                // update the backIntent to display new post on wall
                GlobalInfo wall = new GlobalInfo();

                // fetch the details from intent and assign it to object
                wall.setInfoId(data.getStringExtra(Constants.infoId));
                wall.setTitle(data.getStringExtra(Constants.title));
                wall.setDescription(data.getStringExtra(Constants.postDescription));
                wall.setLink(data.getStringExtra(Constants.link));
                wall.setCategory(data.getStringExtra(Constants.category));
                wall.setCreatedAt(data.getStringExtra(Constants.createdAt));

                Log.v(Constants.appName, data.getStringExtra(Constants.infoId));
                Log.v(Constants.appName, data.getStringExtra(Constants.title));
                Log.v(Constants.appName, data.getStringExtra(Constants.postDescription));
                Log.v(Constants.appName, data.getStringExtra(Constants.category));
                Log.v(Constants.appName, data.getStringExtra(Constants.createdAt));

                // add the object to list at top and notify adapter
                knowledgeWallPosts.add(0, wall);
                setupViewPager(pager);

                Bundle bundle = new Bundle();
                bundle.putString(Constants.category, Constants.technology);
                bundle.putParcelableArrayList(Constants.globalInfoTable, knowledgeWallPosts);

                // check if adapter has any items
                if(adapter.getCount() > 0)
                    adapter.getItem(0).setArguments(bundle);
                adapter.notifyDataSetChanged();
                //tabLayout.setupWithViewPager(pager);
                //tabLayout.setTabTextColors(getResources().getColor(R.color.title_color), getResources().getColor(R.color.title_color));

            }
            // check if the post is not created
            else{

            }
        }
    }



    /*
     * This method will resume the activity after returning from other activity
     * hence, adapter to the listview is set here
     *
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();


    }

    // get decoded sample bitmap
    public static Bitmap decodeSampledBitmapFromResource(byte[] bytes, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    // get calculated sample size
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }



    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // create the bundle and put in the arguments to passed to a fragment
        // create an object of the fragment and set the arguments

        Bundle bundle = new Bundle();
        bundle.putString(Constants.category, Constants.technology);
        bundle.putParcelableArrayList(Constants.globalInfoTable, knowledgeWallPosts);
        KnowledgeWallFragment computersFragment = new KnowledgeWallFragment();
        computersFragment.setArguments(bundle);

            adapter.addFragment(computersFragment, Constants.technology);

/*        Bundle bundle1 = new Bundle();
        bundle1.putString(Constants.category, Constants.electronics);
        bundle1.putParcelableArrayList(Constants.globalInfoTable, knowledgeWallPosts);
        KnowledgeWallFragment electronicsFragment = new KnowledgeWallFragment();
        electronicsFragment.setArguments(bundle1);

            adapter.addFragment(electronicsFragment, Constants.electronics);*/

        Bundle bundle2 = new Bundle();
        bundle2.putString(Constants.category, Constants.business);
        bundle2.putParcelableArrayList(Constants.globalInfoTable, knowledgeWallPosts);
        KnowledgeWallFragment businessFragment = new KnowledgeWallFragment();
        businessFragment.setArguments(bundle2);

            adapter.addFragment(businessFragment, Constants.business);

        Bundle bundle3 = new Bundle();
        bundle3.putString(Constants.category, Constants.startups);
        bundle3.putParcelableArrayList(Constants.globalInfoTable, knowledgeWallPosts);
        KnowledgeWallFragment startupsFragment = new KnowledgeWallFragment();
        startupsFragment.setArguments(bundle3);

            adapter.addFragment(startupsFragment, Constants.startups);

        Bundle bundle4 = new Bundle();
        bundle4.putString(Constants.category, Constants.newsandevents);
        bundle4.putParcelableArrayList(Constants.globalInfoTable, knowledgeWallPosts);
        KnowledgeWallFragment newsFragment = new KnowledgeWallFragment();
        newsFragment.setArguments(bundle4);

            adapter.addFragment(newsFragment, Constants.newsandevents);

/*        Bundle bundle5 = new Bundle();
        bundle5.putString(Constants.category, Constants.events);
        bundle5.putParcelableArrayList(Constants.globalInfoTable, knowledgeWallPosts);
        KnowledgeWallFragment eventsFragment = new KnowledgeWallFragment();
        eventsFragment.setArguments(bundle5);

            adapter.addFragment(eventsFragment, Constants.events);*/

        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {


            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }












/*

    */
/**
     * This AsyncTask class will fetch globalInfo wall posts from
     * database and assign it to adapter for populating listview
     *//*

    class GetGlobalPosts extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            knowledgeWallPosts = new ArrayList<GlobalInfo>();
        }

        @Override
        protected String doInBackground(String... strings) {

            // create parse object for the "GlobalInfo"
            ParseQuery<ParseObject> globalInfoQuery = ParseQuery.getQuery(Constants.globalInfoTable);

            // get mapping for the list of objectIds
            globalInfoQuery.addDescendingOrder(Constants.createdAt);

            // get the list of shouts
            globalInfoQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {

                    if (e == null) {

                        if (parseObjects.size() > 0) {

                            // navigate through the list of objects
                            for (int i = 0; i < parseObjects.size(); i++) {

                                // create object for collegeWallPost class
                                final GlobalInfo knowledgeWallPost = new GlobalInfo();


                                // date format for displaying created date
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm");

                                // set the values of the wall post into object
                                knowledgeWallPost.setTitle(parseObjects.get(i).getString(Constants.title));
                                knowledgeWallPost.setDescription(parseObjects.get(i).getString(Constants.description));
                                knowledgeWallPost.setBranch(parseObjects.get(i).getString(Constants.branch));
                                knowledgeWallPost.setRole(parseObjects.get(i).getString(Constants.role));
                                knowledgeWallPost.setCreatedAt(simpleDateFormat.format(parseObjects.get(i).getCreatedAt()));
                                knowledgeWallPost.setUpdatedAt(simpleDateFormat.format(parseObjects.get(i).getUpdatedAt()));
                                knowledgeWallPost.setObjectId(parseObjects.get(i).getObjectId());
                                knowledgeWallPost.setCategory(parseObjects.get(i).getString(Constants.category));

                                // get the mediaFile of the wall post
                                ParseFile mediaFile = (ParseFile) parseObjects.get(i).get(Constants.mediaFile);

                                mediaFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] bytes, ParseException e) {

                                        // check if the image is available
                                        if (e == null) {

                                            // check if the mediaTable file bytes are null indicator bytes
                                            // if so assign the mediaTable file string to null indicator
                                            // else fetch the mediaTable file bytes and assign
                                            if (Arrays.equals(bytes, Constants.null_indicator.getBytes())) {

                                                knowledgeWallPost.setMediaFile(Constants.null_indicator);
                                                //knowledgeWallPosts.add(knowledgeWallPost);

                                            } else {

                                                knowledgeWallPost.setMediaFile(Base64.encodeToString(bytes, Base64.DEFAULT).toString());
                                                //knowledgeWallPosts.add(knowledgeWallPost);

                                            }

                                        }
                                        else {

                                            // do nothing
                                        }

                                    }
                                });

                                knowledgeWallPosts.add(knowledgeWallPost);

                            }
                            setupViewPager(pager);
                            tabLayout.setupWithViewPager(pager);
                            tabLayout.setTabTextColors(getResources().getColor(R.color.title_color), getResources().getColor(R.color.title_color));


                        }
                    }
                }
            });

            return null;
        }

    }

*/






    /**
     * Verify whether user has previous active login session
     *
     * if so, restrict the user for login
     * else
     *      Check if the collegeId and secretCode are matching
     *      if so, get the user details of collegeId from db and create a active login session
     */
    private class GetGlobalInfo extends AsyncTask<String, Void, Void>{

        private String Content = "";
        private String Error = null;
        String data = "";

        @Override
        protected void onPreExecute() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }

        @Override
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader = null;

            // Send data
            try {

                // Set Request parameter
                data += "?&" + URLEncoder.encode(Constants.KEY, "UTF-8") + "=" + Constants.key
                        + "&" + URLEncoder.encode(Constants.limit, "UTF-8") + "=" + (urls[1]);

                Log.v(Constants.appName, urls[0]+data);

                // Defined URL  where to send data
                URL url = new URL(urls[0]+data);

                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                //conn.setDoInput(true);
                //OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                //wr.write(data);
                //wr.flush();

                // setting timeout
                conn.setConnectTimeout(15000);

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();

                // close the reader
                //reader.close();


            } catch (SocketTimeoutException e){

                e.printStackTrace();
                Error = e.getMessage();


                // call service again after time out
                callGlobalInfo(0);

            }

            catch (Exception ex) {

                ex.printStackTrace();
                Error = ex.getMessage();


            } finally {

                try {

                    reader.close();


                } catch (Exception ex) {
                    Error = ex.getMessage();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // hide the progress
                            knowledgeWallProgress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Server unreachable! Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }



            return null;
        }

        protected void onPostExecute(Void unused) {

            if (Error != null) {

                Log.i("Connection", Error);

            } else {

                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);


                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    status = jsonResponse.getInt(Constants.status);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // check the status and proceed with the logic
                            switch (status){

                                // exception occurred
                                case -3:

                                    // kill the progress and show error
                                    knowledgeWallProgress.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // key mismatch
                                case -2:

                                    // kill the progress and show error
                                    knowledgeWallProgress.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // Reached end, no more to display
                                case 0:

                                    // kill the progress and show error
                                    knowledgeWallProgress.setVisibility(View.GONE);
                                    break;

                                // data found
                                case 1:

                                    try {

                                        // get JSON Array of 'details'
                                        JSONArray jsonArray = jsonResponse.getJSONArray(Constants.details);

                                        for(int i=0; i<jsonArray.length(); i++){

                                            // get the JSON object inside Array
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);


                                            // create object for collegeWallPost class
                                            GlobalInfo knowledgeWallPost = new GlobalInfo();


                                            // date format for displaying created date
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm");

                                            // set the values of the wall post into object
                                            knowledgeWallPost.setInfoId(jsonObject.getString(Constants.infoId));
                                            knowledgeWallPost.setCategory(jsonObject.getString(Constants.category));
                                            knowledgeWallPost.setTitle(jsonObject.getString(Constants.title));
                                            knowledgeWallPost.setDescription(jsonObject.getString(Constants.description));
                                            knowledgeWallPost.setLink(jsonObject.getString(Constants.link));
                                            knowledgeWallPost.setUserObjectId(jsonObject.getString(Constants.userObjectId));
                                            knowledgeWallPost.setCreatedAt(jsonObject.getString(Constants.createdAt));
                                            knowledgeWallPost.setUpdatedAt(jsonObject.getString(Constants.updatedAt));

                                            knowledgeWallPost.setMediaCount(jsonObject.getInt(Constants.mediaCount));
                                            knowledgeWallPost.setMedia(jsonObject.getString(Constants.media));

                                            //Log.v(Constants.appName, "Adding media"+knowledgeWallPost.getMediaCount() + " media" + knowledgeWallPost.getMedia());

                                            // add the object to list
                                            knowledgeWallPosts.add(knowledgeWallPost);
                                        }

                                        // update the items
                                        setupViewPager(pager);
                                        tabLayout.setupWithViewPager(pager);
                                        tabLayout.setTabTextColors(getResources().getColor(R.color.title_color), getResources().getColor(R.color.title_color));


                                    }
                                    catch(Exception e){

                                        Log.e(Constants.error, e.getMessage());
                                        Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();

                                    }

                                    break;

                            }
                        }
                    });


                    // hide the progress
                    knowledgeWallProgress.setVisibility(View.GONE);



                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();

                }

            }
        }
    }
}
