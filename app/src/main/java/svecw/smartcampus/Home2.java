package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.KnowledgeWallAdapter;
import internaldb.SmartCampusDB;
import model.KnowledgeInfo;
import utils.Constants;


/**
 * Created by Pavan on 6/8/15.
 */
public class Home2 extends AppCompatActivity {

    // get views from activity
    LinearLayout cseKnowledgeView, eceKnowledgeView, eeeKnowledgeView, mechKnowledgeView, civilKnowledgeView,
            basicScienceKnowledgeView, mbaKnowledgeView, mcaKnowledgeView;

    // progress bar
    ProgressBar knowledgeWallProgressBar;

    // bottom navigation views
    LinearLayout collegeWallView, studentWallView, alumniWallView, moreView;

    // internal db object
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    // list for storing global wall posts
    public static ArrayList<KnowledgeInfo> knowledgeWallPosts;
    public static KnowledgeWallAdapter knowledgeWallAdapter;

    // date format for displaying created date
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");

    // layout inflator
    LayoutInflater layoutInflater;
    RelativeLayout l;

    ViewPager pager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.home_activity);
        setContentView(R.layout.global_home_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        //TextView title = (TextView) toolbar.findViewById(R.id.appName);
        toolbar.setTitle(Constants.appName);
        toolbar.setTitleTextColor(getResources().getColor(R.color.title_color));// setText(getResources().getString(R.string.app_name));

        // set the toolbar to the actionBar
        //setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(pager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabTextColors(getResources().getColor(R.color.title_color), getResources().getColor(R.color.title_color));

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // bottom bar views
        collegeWallView = (LinearLayout) findViewById(R.id.collegeWallView);
        studentWallView = (LinearLayout) findViewById(R.id.studentWallView);
        alumniWallView = (LinearLayout) findViewById(R.id.alumniWallView);
        moreView = (LinearLayout) findViewById(R.id.moreView);

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
                Intent studentWallIntent = new Intent(getApplicationContext(), StudentWallActivity.class);
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

        // get the posts
        //new GetGlobalPosts().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.createWallPost) {

            // navigate to selectNewPostActivity to show posting options based on privileges
            Intent newPostIntent = new Intent(getApplicationContext(), SelectNewPostActivity.class);
            startActivity(newPostIntent);

            return true;
        }/*
        else if (id == R.id.userProfile) {

            // navigate to selectNewPostActivity to show posting options based on privileges
            Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(profileIntent);

            return true;
        }*/


        return super.onOptionsItemSelected(item);
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString(Constants.category, Constants.technology);
        Fragment computersFragment = new ComputersFragment(); computersFragment.setArguments(bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putString(Constants.category, Constants.electronics);
        Fragment electronicsFragment = new ElectronicsFragment(); electronicsFragment.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putString(Constants.category, Constants.business);
        Fragment businessFragment = new BusinessFragment(); businessFragment.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putString(Constants.category, Constants.startups);
        Fragment startupsFragment = new KnowledgeWallFragment(); startupsFragment.setArguments(bundle3);

        Bundle bundle4 = new Bundle();
        bundle4.putString(Constants.category, Constants.news);
        Fragment newsFragment = new KnowledgeWallFragment(); newsFragment.setArguments(bundle4);

        Bundle bundle5 = new Bundle();
        bundle5.putString(Constants.category, Constants.events);
        Fragment eventsFragment = new KnowledgeWallFragment(); eventsFragment.setArguments(bundle5);


        adapter.addFragment(computersFragment, Constants.technology);
        adapter.addFragment(electronicsFragment, Constants.electronics);
        adapter.addFragment(businessFragment, Constants.business);
        adapter.addFragment(startupsFragment, Constants.startups);
        adapter.addFragment(newsFragment, Constants.news);
        adapter.addFragment(eventsFragment, Constants.events);

        viewPager.setAdapter(adapter);
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









    public static class KnowledgeWallFragments extends Fragment {

        ProgressBar knowledgeWallProgressBar;
        ListView knowledgeList;

        public KnowledgeWallFragments() {

        }



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // inflate the layout
            RelativeLayout itemView = (RelativeLayout) inflater.inflate(R.layout.knowledge_single_fragment, container, false);

            // progress bar
            knowledgeWallProgressBar = (ProgressBar) itemView.findViewById(R.id.knowledgeWallProgressBar);
            knowledgeList = (ListView) itemView.findViewById(R.id.knowledgeList);

            knowledgeWallAdapter = new KnowledgeWallAdapter(getActivity().getApplicationContext());

            knowledgeList.setAdapter(knowledgeWallAdapter);

            // get the posts
            //new GetGlobalPosts().execute();


            return itemView;
        }



    }



    /**
     * This AsyncTask class will fetch globalInfo wall posts from
     * database and assign it to adapter for populating listview
     */
    class GetGlobalPosts extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            knowledgeWallPosts = new ArrayList<KnowledgeInfo>();
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
                                final KnowledgeInfo knowledgeWallPost = new KnowledgeInfo();

                                // set the values of the wall post into object
                                knowledgeWallPost.setTitle(parseObjects.get(i).getString(Constants.title));
                                knowledgeWallPost.setDescription(parseObjects.get(i).getString(Constants.description));
                                knowledgeWallPost.setBranch(parseObjects.get(i).getString(Constants.category));
                                knowledgeWallPost.setRole(parseObjects.get(i).getString(Constants.role));
                                knowledgeWallPost.setCreatedAt(parseObjects.get(i).getCreatedAt());
                                knowledgeWallPost.setUpdatedAt(parseObjects.get(i).getUpdatedAt());
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
                                                knowledgeWallPosts.add(knowledgeWallPost);

                                                //knowledgeWallAdapter.updateItems(knowledgeWallPosts);
                                                knowledgeWallAdapter.notifyDataSetChanged();
                                            } else {

                                                knowledgeWallPost.setMediaFile(Base64.encodeToString(bytes, Base64.DEFAULT).toString());
                                                knowledgeWallPosts.add(knowledgeWallPost);


                                                //knowledgeWallAdapter.updateItems(knowledgeWallPosts);
                                                knowledgeWallAdapter.notifyDataSetChanged();
                                            }

                                        }

                                    }
                                });


                                knowledgeWallAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            /**
             * Updating parsed JSON data into ListView
             * */
            Log.i(Constants.appName, "Finished");
            // notify the adapter
            knowledgeWallAdapter.notifyDataSetChanged();

            // save the data to shared preferences


        }
    }
}
