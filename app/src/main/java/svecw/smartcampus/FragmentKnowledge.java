package svecw.smartcampus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import adapters.KnowledgeWallAdapter;
import model.GlobalInfo;
import utils.ConnectionDetector;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan_Kusuma on 12/13/2016.
 */

public class FragmentKnowledge extends Fragment implements Global_Activity.FragmentLifecycle {

    ProgressBar knowledgeWallProgressBar;
    ListView knowledgeList;
    ArrayList<GlobalInfo> knowledgeWallPosts = new ArrayList<GlobalInfo>();;
    String category;

    int status, skipCounter = 0;
    JSONObject jsonResponse;
    // instance for ConnectionDetector
    ConnectionDetector connectionDetector;

    // adapter
    KnowledgeWallAdapter knowledgeWallAdapter;

    public FragmentKnowledge() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("Connection", "Knowledge View");
        // inflate the layout
        RelativeLayout itemView = (RelativeLayout) inflater.inflate(R.layout.knowledge_single_fragment, container, false);

        // progress bar
        knowledgeWallProgressBar = (ProgressBar) itemView.findViewById(R.id.knowledgeWallProgressBar);
        knowledgeList = (ListView) itemView.findViewById(R.id.knowledgeList);

        if(getActivity()!=null)
        knowledgeWallAdapter = new KnowledgeWallAdapter(getActivity().getApplicationContext());
        knowledgeList.setAdapter(knowledgeWallAdapter);
//        knowledgeWallAdapter.notifyDataSetChanged();

        Log.i("Connection", "Knowledge");

        // object for ConnectionDetector
        connectionDetector = new ConnectionDetector(getContext());

        // check if internet is working
        // else show no network toast
        if(connectionDetector.isInternetWorking()) {

            // get the posts on load of the activity
            //new GetGlobalInfo().execute(Routes.getGlobalInfo, String.valueOf(0));

            // check if the list items are present.
            // if so, do not call the service
            // else call the service to fetch results
            if(knowledgeWallPosts.size() == 0)
                callGlobalInfo();
            else
                knowledgeWallProgressBar.setVisibility(View.GONE);

        } else {

            // disable progress and show connection status
            knowledgeWallProgressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No internet", Toast.LENGTH_SHORT).show();
        }
        // close the progress bar
        //knowledgeWallProgressBar.setVisibility(View.GONE);

        return itemView;
    }

    @Override
    public void onResume() {
        knowledgeWallAdapter.updateItems(knowledgeWallPosts);
        knowledgeWallAdapter.notifyDataSetChanged();

        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResumeFragment() {
        knowledgeWallAdapter.updateItems(knowledgeWallPosts);
        knowledgeWallAdapter.notifyDataSetChanged();

    }

    // call GlobalInfo
    public void callGlobalInfo(){

        new GetGlobalInfo().execute(Routes.getGlobalInfo, String.valueOf(skipCounter));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

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

                wall.setMediaCount(0);
                wall.setMedia(Constants.null_indicator);
                wall.setHits(0);
                wall.setSeen(0);

                Log.v(Constants.appName, data.getStringExtra(Constants.infoId));
                Log.v(Constants.appName, data.getStringExtra(Constants.title));
                Log.v(Constants.appName, data.getStringExtra(Constants.postDescription));
                Log.v(Constants.appName, data.getStringExtra(Constants.category));
                Log.v(Constants.appName, data.getStringExtra(Constants.createdAt));

                // add the object to list at top and notify adapter
                knowledgeWallPosts.add(0, wall);

                knowledgeWallAdapter.notifyDataSetChanged();
                //tabLayout.setupWithViewPager(pager);
                //tabLayout.setTabTextColors(getResources().getColor(R.color.title_color), getResources().getColor(R.color.title_color));

            }
            // check if the post is not created
            else{

            }
        }
    }


    private class GetGlobalInfo extends AsyncTask<String, Void, Void> {

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
                Log.i("Connection", Content);
                // close the reader
                //reader.close();


            } catch (SocketTimeoutException e){

                e.printStackTrace();
                Error = e.getMessage();


                // call service again after time out
                callGlobalInfo();

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
                            knowledgeWallProgressBar.setVisibility(View.GONE);
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

                                    // kill the progress and show error
                                    knowledgeWallProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // key mismatch
                                case -2:

                                    // kill the progress and show error
                                    knowledgeWallProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // Reached end, no more to display
                                case 0:

                                    // kill the progress and show error
                                    knowledgeWallProgressBar.setVisibility(View.GONE);
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
                                            knowledgeWallPost.setHits(jsonObject.getInt(Constants.hits));
                                            knowledgeWallPost.setSeen(jsonObject.getInt(Constants.seen));

                                            //Log.v(Constants.appName, "Adding media"+knowledgeWallPost.getMediaCount() + " media" + knowledgeWallPost.getMedia());

                                            // add the object to list
                                            knowledgeWallPosts.add(knowledgeWallPost);
                                        }


                                        Log.i("Connection", "List filled");
                                        knowledgeWallAdapter.updateItems(knowledgeWallPosts);
                                        knowledgeWallAdapter.notifyDataSetChanged();
/*

                                        // update the items
                                        setupViewPager(pager);
                                        tabLayout.setupWithViewPager(pager);
                                        tabLayout.setTabTextColors(getResources().getColor(R.color.title_color), getResources().getColor(R.color.title_color));

*/
                                        knowledgeWallProgressBar.setVisibility(View.GONE);

                                        // detect the end of scrolling
                                        knowledgeList.setOnScrollListener(new AbsListView.OnScrollListener() {
                                            @Override
                                            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                                                int threshold = 1;
                                                int count = knowledgeList.getCount();

                                                if (scrollState == SCROLL_STATE_IDLE) {
                                                    if (knowledgeList.getLastVisiblePosition() >= count
                                                            - threshold) {

                                                        // show loading
                                                        knowledgeWallProgressBar.setVisibility(View.VISIBLE);

                                                        // increment the skip counter
                                                        skipCounter = skipCounter + 10;
                                                        // Fetch additional posts skipping existing posts
                                                        //new GetWallPosts().execute(Routes.getWallPosts, Constants.COLLEGE, String.valueOf(skipCounter));
                                                        callGlobalInfo();
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


                    // hide the progress
                    knowledgeWallProgressBar.setVisibility(View.GONE);

                    Log.i("Connection", "Done");


                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();

                }

            }
        }
    }
}
