package svecw.smartcampus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

import adapters.CollegeWallAdapter;
import adapters.KnowledgeWallAdapter;
import internaldb.SmartCampusDB;
import internaldb.SmartSessionManager;
import model.Wall;
import notify.Notification_BGService;
import utils.ConnectionDetector;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan_Kusuma on 12/13/2016.
 */

public class FragmentCollege extends Fragment implements Global_Activity.FragmentLifecycle {


    // adapter to populate list view
    CollegeWallAdapter adapter;

    // list of college wall posts
    List<Wall> collegeWallPostsList;

    // local db object
    SmartCampusDB smartDB = new SmartCampusDB(getContext());

    // list view
    ListView listView;
    TextView noData;
    LinearLayout fullImageLayout;

    // toolbar
    Toolbar toolbar;

    //SmartDB smartDB = new SmartDB(this);
    byte[] b = Constants.null_indicator.getBytes();

    // instance for ConnectionDetector
    ConnectionDetector connectionDetector;

    // progress bar
    ProgressBar progressBar;

    // object for internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(getContext());

    // skip counter
    int skipCounter = 0;

    // footerView for the list
    Button btnMore;

    JSONObject jsonResponse;
    int status;

    // url for get wall posts
    String url = Routes.getWallPosts + Constants.key + "/" + Constants.COLLEGE + "/";

    SmartSessionManager session;

    public FragmentCollege() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("Connection", "CollegeView");
        // inflate the layout
        RelativeLayout itemView = (RelativeLayout) inflater.inflate(R.layout.college_wall_activity, container, false);

        // get views from layout
        listView = (ListView) itemView.findViewById(R.id.collegeWallListView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBarCollegeWall);
        fullImageLayout = (LinearLayout) itemView.findViewById(R.id.fullImageLayout);


        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // set emptyTextView for listView
        noData = (TextView) itemView.findViewById(R.id.emptyElement); noData.setTypeface(sansFont);
//listView.addFooterView(noData);
        // clear the notification
        clearNotifications();

        // adapter for the college wall posts
        adapter = new CollegeWallAdapter(getContext());
        //setListAdapter(adapter);
        listView.setAdapter(adapter);

        // session
        session = new SmartSessionManager(getContext());

        // list for storing college wall posts
        collegeWallPostsList = new ArrayList<Wall>();

        // object for ConnectionDetector
        connectionDetector = new ConnectionDetector(getContext());



        // check if internet is working
        // else show no network toast
        if(connectionDetector.isInternetWorking()) {

            // get the wall posts from database
            //new GetWallPosts().execute(Routes.getWallPosts, Constants.COLLEGE, String.valueOf(skipCounter));
            // check if the list items are present.
            // if so, do not call the service
            // else call the service to fetch results
            if(collegeWallPostsList.size() == 0)
                callGetPosts();
            else
                progressBar.setVisibility(View.GONE);
            //new GetCollegeWallPosts().execute(String.valueOf(skipCounter));


        } else {

            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No internet", Toast.LENGTH_SHORT).show();
        }



        return itemView;
    }

    @Override
    public void onResume() {

        adapter.updateItems(collegeWallPostsList);
        adapter.notifyDataSetChanged();

        super.onResume();
    }
    @Override
    public void onResumeFragment() {
        Log.i("Connection", "College");

        // check if the list items are present.
        // if so, do not call the service
        // else call the service to fetch results
        /*if(collegeWallPostsList.size() == 0){
            callGetPosts();
        }*/
        adapter.updateItems(collegeWallPostsList);
        adapter.notifyDataSetChanged();

    }

    // clear the notifications
    public void clearNotifications(){

        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.appName+"_notify", Context.MODE_PRIVATE);
        if(prefs.contains("notificationNumber")) {
            int notificationNumber = prefs.getInt("notificationNumber", 0);

            //notificationManager.notify(notificationNumber , noBuilder.build());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("notificationNumber", 0);
            editor.commit();
        }

    }

    // call GlobalInfo
    public void callGetPosts(){

        new GetWallPosts().execute(Routes.getWallPosts, Constants.COLLEGE, String.valueOf(skipCounter));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Log.v(Constants.appName, "Checking"+1);
        // if the result is capturing Image
        if (requestCode == 100) {

            Log.v(Constants.appName, "Checking"+2);
            if (resultCode == -1) {

                Log.v(Constants.appName, "Checking"+3);
                // fetch the comment count and position
                int commentCount = data.getIntExtra(Constants.commentCount, 0);
                int position = data.getIntExtra(Constants.position, 0);

                Log.v(Constants.appName, "Checking"+4);
                // check the comment count and notify the adapter
                if(commentCount > 0) {

                    Log.v(Constants.appName, "Checking"+5);
                    Log.v(Constants.appName, "Checking"+commentCount);
                    Log.v(Constants.appName, "Checking"+position);
                    // add the comment count to current comment count of given wall post
                    //collegeWallPostsList.get(position).setComments(collegeWallPostsList.get(position).getComments()+commentCount);
                    collegeWallPostsList.get(position).setComments(commentCount);

                    adapter.notifyDataSetChanged();

                } else {

                    // if no new comment appeared, do nothing
                }
            }
        }
        // check if result is from new post activity
        else if(requestCode == 200){

            // check if the post is created
            if(resultCode == 1){


                // update the backIntent to display new post on wall
                Wall wall = new Wall();

                // fetch the details from intent and assign it to object
                wall.setWallId(data.getStringExtra(Constants.wallId));
                wall.setWallType(data.getStringExtra(Constants.wallType));
                wall.setUserObjectId(data.getStringExtra(Constants.userObjectId));
                wall.setPostDescription(data.getStringExtra(Constants.postDescription));
                wall.setCreatedAt(data.getStringExtra(Constants.createdAt));
                wall.setUpdatedAt(data.getStringExtra(Constants.updatedAt));
                wall.setLikes(data.getIntExtra(Constants.likes, 0));
                wall.setComments(data.getIntExtra(Constants.comments, 0));
                wall.setMediaCount(data.getIntExtra(Constants.mediaCount, 0));
                wall.setMedia(data.getStringExtra(Constants.media));
                wall.setIsActive(data.getIntExtra(Constants.isActive, 0));
                wall.setUserName(data.getStringExtra(Constants.userName));
                wall.setMedia(data.getStringExtra(Constants.media));
                wall.setLinkUrl(data.getStringExtra(Constants.linkUrl));
                wall.setLinkTitle(data.getStringExtra(Constants.linkTitle));
                wall.setLinkCaption(data.getStringExtra(Constants.linkCaption));
                wall.setLocation(data.getStringExtra(Constants.location));
                wall.setFeeling(data.getStringExtra(Constants.feeling));
                wall.setUserImage(session.getProfilePhoto());

                // add the object to list at top and notify adapter
                collegeWallPostsList.add(0, wall);
                adapter.notifyDataSetChanged();

                // send notification
                new Notification_BGService().execute(Routes.createWallPost_Notify, wall.getUserObjectId());


            }
            // check if the post is not created
            else{

                //Toast.makeText(this, "Try posting", Toast.LENGTH_SHORT).show();
            }
        }
        else {

            adapter.notifyDataSetChanged();
        }
    }

    private class GetWallPosts extends AsyncTask<String, Void, Void> {

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
                data += "?&" + URLEncoder.encode(Constants.KEY, Constants.charset) + "=" + Constants.key
                        + "&" + URLEncoder.encode(Constants.wallType, Constants.charset) + "=" + (urls[1])
                        + "&" + URLEncoder.encode(Constants.limit, Constants.charset) + "=" + (urls[2]);

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
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), Constants.charset));
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
                callGetPosts();
            }
            catch (Exception ex) {

                ex.printStackTrace();
                Error = ex.getMessage();


            } finally {

                try {

                    reader.close();

                } catch (Exception ex) {
                    Error = ex.getMessage();

                    if(getActivity()!=null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // hide the progress
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Server unreachable! Please try again", Toast.LENGTH_SHORT).show();
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

                //Log.i("Connection", Content);
                /****************** Start Parse Response JSON Data *************/


                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);


                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    status = jsonResponse.getInt(Constants.status);

                    if(getActivity()!=null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // check the status and proceed with the logic
                            switch (status){

                                // exception occurred
                                case -3:

                                    // hide loading
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // key mismatch
                                case -2:

                                    // hide loading
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // Reached end, no more to display
                                case 0:

                                    // no more data to display
                                    // hide loading
                                    progressBar.setVisibility(View.GONE);
                                    //Toast.makeText(getContext(), R.string.noData, Toast.LENGTH_SHORT).show();
                                    noData.setVisibility(View.VISIBLE);
                                    //listView.addFooterView(noData);

                                    adapter.notifyDataSetChanged();
                                    break;

                                // data found
                                case 1:

                                    try {

                                        // get JSON Array of 'details'
                                        JSONArray jsonArray = jsonResponse.getJSONArray(Constants.details);

                                        for(int i=0; i<jsonArray.length(); i++){

                                            // get the JSON object inside Array
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                            // get the data and store in internal db
                                            Wall wall = new Wall();

                                            wall.setWallId(jsonObject.getString(Constants.wallId));
                                            wall.setWallType(jsonObject.getString(Constants.wallType)); // secretCode.getText().toString()
                                            wall.setUserObjectId(jsonObject.getString(Constants.userObjectId));
                                            wall.setPostDescription(jsonObject.getString(Constants.postDescription));
                                            wall.setCreatedAt(jsonObject.getString(Constants.createdAt));
                                            wall.setUpdatedAt(jsonObject.getString(Constants.updatedAt));
                                            wall.setLikes(jsonObject.getInt(Constants.likes));
                                            wall.setDislikes(jsonObject.getInt(Constants.dislikes));
                                            wall.setComments(jsonObject.getInt(Constants.comments));
                                            wall.setMediaCount(jsonObject.getInt(Constants.mediaCount));
                                            wall.setMedia(jsonObject.getString(Constants.media));
                                            wall.setIsActive(jsonObject.getInt(Constants.isActive));
                                            wall.setUserName(jsonObject.getString(Constants.userName));
                                            wall.setLinkUrl(jsonObject.getString(Constants.linkUrl));
                                            wall.setLinkTitle(jsonObject.getString(Constants.linkTitle));
                                            wall.setLinkCaption(jsonObject.getString(Constants.linkCaption));
                                            wall.setLocation(jsonObject.getString(Constants.location));
                                            wall.setFeeling(jsonObject.getString(Constants.feeling));


                                            //Log.v(Constants.appName, "Media:" +jsonObject.getString(Constants.media));

                                            // get the user image
                                            JSONObject userObject = (JSONObject) jsonObject.get(Constants.user);
                                            int mediaCount = userObject.getInt(Constants.mediaCount);
                                            if(mediaCount > 0){
                                                Log.v(Constants.appName, "userImage : "+userObject.getString(Constants.media));
                                                wall.setUserImage(userObject.getString(Constants.media));
                                            }
                                            else{
                                                Log.v(Constants.appName, "userImage : "+userObject.getString(Constants.media));
                                                wall.setUserImage(Constants.null_indicator);
                                            }

                                            // get the collegeId of user
                                            wall.setCollegeId(userObject.getString(Constants.collegeId));

                                            // add the object to list
                                            collegeWallPostsList.add(wall);
                                        }

                                        // update the items
                                        adapter.updateItems(collegeWallPostsList);
                                        adapter.notifyDataSetChanged();

                                        // hide loading
                                        progressBar.setVisibility(View.GONE);

                                        // detect the end of scrolling
                                        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                            @Override
                                            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                                                int threshold = 1;
                                                int count = listView.getCount();

                                                if (scrollState == SCROLL_STATE_IDLE) {
                                                    if (listView.getLastVisiblePosition() >= count
                                                            - threshold) {

                                                        // show loading
                                                        progressBar.setVisibility(View.VISIBLE);

                                                        // increment the skip counter
                                                        skipCounter = skipCounter + 4;
                                                        // Fetch additional posts skipping existing posts
                                                        //new GetWallPosts().execute(Routes.getWallPosts, Constants.COLLEGE, String.valueOf(skipCounter));
                                                        callGetPosts();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                                            }
                                        });


                                    }
                                    catch(Exception e){

                                        Log.e(Constants.error, e.getMessage());
                                        Toast.makeText(getContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();

                                    }

                                    break;

                            }
                        }
                    });





                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();

                }

            }
        }
    }

}
