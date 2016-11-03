package svecw.smartcampus;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

import model.Drive;
import model.GlobalInfo;
import utils.ConnectionDetector;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan_Kusuma on 9/17/2016.
 */
public class DriveActivity extends AppCompatActivity {

    ViewPagerAdapter adapter;
    public static ArrayList<Drive> drivePosts;
    ConnectionDetector connectionDetector;
    ProgressBar driveProgress;

    ViewPager pager;
    TabLayout tabLayout;

    JSONObject jsonResponse;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive_home_activity);

        // get the tool bar and set the support ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);

        // object for globalPosts
        drivePosts = new ArrayList<Drive>();
        pager = (ViewPager) findViewById(R.id.driveViewPager);
        driveProgress = (ProgressBar) findViewById(R.id.driveProgress);
        //setupViewPager(pager);

        tabLayout = (TabLayout) findViewById(R.id.driveTabs);

        // object for ConnectionDetector
        connectionDetector = new ConnectionDetector(getApplicationContext());

        // check if internet is working
        // else show no network toast
        if(connectionDetector.isInternetWorking()) {

            // get the posts on load of the activity
            //new GetGlobalInfo().execute(Routes.getGlobalInfo, String.valueOf(0));

            callDriveInfo(0);

        } else {

            // disable progress and show connection status
            driveProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
        }

    }

    // call GlobalInfo
    public void callDriveInfo(int skip){

        new GetDriveInfo().execute(Routes.getGlobalInfo, String.valueOf(skip));
    }

    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // create the bundle and put in the arguments to passed to a fragment
        // create an object of the fragment and set the arguments
        Bundle bundle = new Bundle();
        bundle.putString(Constants.category, Constants.uploads);
        bundle.putParcelableArrayList(Constants.driveInfo, drivePosts);
        DriveWallFragment uploadsFragment = new DriveWallFragment();
        uploadsFragment.setArguments(bundle);

            adapter.addFragment(uploadsFragment, Constants.uploads);


        Bundle bundle2 = new Bundle();
        bundle2.putString(Constants.category, Constants.shared);
        bundle2.putParcelableArrayList(Constants.driveInfo, drivePosts);
        DriveWallFragment sharedFragment = new DriveWallFragment();
        sharedFragment.setArguments(bundle2);

            adapter.addFragment(sharedFragment, Constants.shared);


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




    /**
     * Verify whether user has previous active login session
     *
     * if so, restrict the user for login
     * else
     *      Check if the collegeId and secretCode are matching
     *      if so, get the user details of collegeId from db and create a active login session
     */
    private class GetDriveInfo extends AsyncTask<String, Void, Void> {

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
                callDriveInfo(0);

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
                            driveProgress.setVisibility(View.GONE);
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
                                    driveProgress.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // key mismatch
                                case -2:

                                    // kill the progress and show error
                                    driveProgress.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // Reached end, no more to display
                                case 0:

                                    // kill the progress and show error
                                    driveProgress.setVisibility(View.GONE);
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
                                            Drive drivePost = new Drive();


                                            // date format for displaying created date
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm");

                                            // set the values of the wall post into object
                                            drivePost.setTitle(jsonObject.getString(Constants.title));
                                            drivePost.setDescription(jsonObject.getString(Constants.description));
                                            drivePost.setUserObjectId(jsonObject.getString(Constants.userObjectId));
                                            /*knowledgeWallPost.setCreatedAt(jsonObject.getString(Constants.createdAt));
                                            knowledgeWallPost.setUpdatedAt(jsonObject.getString(Constants.updatedAt));
*/

                                            //Log.v(Constants.appName, "Adding media"+knowledgeWallPost.getMediaCount() + " media" + knowledgeWallPost.getMedia());

                                            // add the object to list
                                            drivePosts.add(drivePost);
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
                    driveProgress.setVisibility(View.GONE);



                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();

                }

            }
        }
    }

}
