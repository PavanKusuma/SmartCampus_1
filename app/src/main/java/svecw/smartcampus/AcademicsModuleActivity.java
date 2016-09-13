package svecw.smartcampus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import adapters.AcademicsAdapter;
import internaldb.SmartCampusDB;
import model.Academics;
import utils.ConnectionDetector;
import utils.Constants;

/**
 * Created by Pavan on 4/16/15.
 */
public class AcademicsModuleActivity extends AppCompatActivity {

    // adapter to populate list view
    AcademicsAdapter adapter;

    // list of acadamics wall posts
    List<Academics> academicsPostsList;

    // local db object
    SmartCampusDB smartDB = new SmartCampusDB(this);

    // list view
    ListView listView;

    // toolbar
    Toolbar toolbar;

    //SmartDB smartDB = new SmartDB(this);
    byte[] b = Constants.null_indicator.getBytes();

    // instance for ConnectionDetector
    ConnectionDetector connectionDetector;

    // progress bar
    ProgressBar progressBar;

    // object for internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    // skip counter
    int skipCounter = 0;

    // layout inflater
    LayoutInflater layoutInflater;

    // list of mediaFiles
    List<byte[]> mediaFilesList;

    // selected module
    String selectedModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.academics_module_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.academics));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // get views from layout
        listView = (ListView) findViewById(R.id.academicPostListView);
        progressBar = (ProgressBar) findViewById(R.id.progressBarAcademicsPosts);
        mediaFilesList = new ArrayList<byte[]>();

        // get the selected module
        selectedModule = getIntent().getStringExtra(Constants.module);

        // create 'more' button for footerView
        Button btnMore = new Button(this);
        btnMore.setText("More...");
        btnMore.setBackgroundResource(R.drawable.card_background);

        // adding footerView to listView
        listView.addFooterView(btnMore);

        // adapter for the college wall posts
        adapter = new AcademicsAdapter(AcademicsModuleActivity.this);
        //setListAdapter(adapter);
        listView.setAdapter(adapter);


        // set emptyTextView for listView
        listView.setEmptyView(findViewById(R.id.emptyElement));

        // list for storing college wall posts
        academicsPostsList = new ArrayList<Academics>();

        // object for ConnectionDetector
        connectionDetector = new ConnectionDetector(getApplicationContext());


        // check if internet is working
        // else show no network toast
        if(connectionDetector.isInternetWorking()) {

            Log.v(Constants.appName, "from external db");

            // get the wall posts from database
            new GetAcademicPosts().execute(String.valueOf(skipCounter));


        } else {

            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
        }

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // increment the skip counter
                skipCounter = skipCounter + 10;
                new GetAcademicPosts().execute(String.valueOf(skipCounter));
            }
        });
    }

    class GetAcademicPosts extends AsyncTask<String, Float, String>{
        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // create parse object for the "CollegeWall"
                ParseQuery<ParseObject> collegePostsQuery = ParseQuery.getQuery(Constants.Academics);

                // order by createdAt
                collegePostsQuery.addDescendingOrder(Constants.createdAt);
                collegePostsQuery.whereEqualTo(Constants.module, selectedModule);
                collegePostsQuery.whereEqualTo(Constants.year, smartCampusDB.getUser().get(Constants.year));
                collegePostsQuery.whereEqualTo(Constants.branch, smartCampusDB.getUser().get(Constants.branch));
                collegePostsQuery.whereEqualTo(Constants.semester, smartCampusDB.getUser().get(Constants.semester));

                // check if skipCounter is not '0'
                // this indicates, request is received from 'load more' button click
                // to fetch more posts from db
                if(Integer.valueOf(params[0]) != 0){

                    collegePostsQuery.setSkip(skipCounter);
                }

                // limit results to 10
                collegePostsQuery.setLimit(10);

                // get the list of shouts
                collegePostsQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> parseObjects, ParseException e) {

                        if (e == null) {

                            if (parseObjects.size() > 0) {

                                // navigate through the list of objects
                                for (int i = 0; i < parseObjects.size(); i++) {

                                    try {

                                        // create object for collegeWallPost class
                                        final Academics academicPost = new Academics();

                                        final ParseObject parseObject = parseObjects.get(i);

                                        // set the values of the wall post into object
                                        academicPost.setDescription(parseObjects.get(i).getString(Constants.description));
                                        academicPost.setCreatedAt(parseObjects.get(i).getCreatedAt());
                                        academicPost.setObjectId(parseObjects.get(i).getObjectId());
                                        academicPost.setUserName(parseObjects.get(i).getString(Constants.userName));
                                        academicPost.setUserObjectId(parseObjects.get(i).getString(Constants.userObjectId));
                                        academicPost.setMediaId(parseObjects.get(i).getString(Constants.mediaId));

                                        // get the number of medias that are present for the given post
                                        // fetch user data using pointer
                                        ParseQuery<ParseObject> userDataObject = ParseQuery.getQuery(Constants.mediaTable);

                                        // check if the objectId exists as 'relativeId' in mediaTable table
                                        userDataObject.whereEqualTo(Constants.relativeId, parseObjects.get(i).getString(Constants.mediaId));

                                        userDataObject.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> list, ParseException e) {

                                                // check if there is no exception
                                                if (e == null) {

                                                    // check if given list is > 0
                                                    if (list.size() > 0) {

                                                        //
                                                        for(int i=0; i<list.size(); i++) {

                                                            // get the mediaTable file
                                                            ParseFile mediaFiles = (ParseFile) list.get(i).get(Constants.mediaFile);

                                                            // check if the data is available
                                                            if (mediaFiles.isDataAvailable()) {
                                                                mediaFiles.getDataInBackground(new GetDataCallback() {
                                                                    @Override
                                                                    public void done(byte[] bytes, ParseException e) {

                                                                        // check if the image is available
                                                                        if (e == null) {

                                                                            if (bytes == Constants.null_indicator.getBytes()) {

                                                                                // set the user image file string to null indicator
                                                                                academicPost.setMediaFile(Constants.null_indicator.getBytes());
                                                                                academicsPostsList.add(academicPost);
                                                                                adapter.notifyDataSetChanged();
                                                                            } else {

                                                                                // set the user image file string to image base 64 string
                                                                                academicPost.setMediaFile(bytes);
                                                                                academicsPostsList.add(academicPost);
                                                                                adapter.notifyDataSetChanged();
                                                                            }


                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                // set the user image file string to null indicator
                                                                academicPost.setMediaFile(Constants.null_indicator.getBytes());
                                                                academicsPostsList.add(academicPost);
                                                                adapter.notifyDataSetChanged();
                                                            }

                                                        }


                                                    }




                                                }
                                            }
                                        });


                                        adapter.updateItems(academicsPostsList);
                                        adapter.notifyDataSetChanged();

                                        progressBar.setVisibility(View.GONE);


                                    } catch (Exception ex) {

                                        Log.v(Constants.appName, ex.getMessage());
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }

                            }

                            // if there are no objects to display
                            else {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "No more to display", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

            }

            // all parse data not found exceptions are caught
            catch(Exception e){
                finish();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            runOnUiThread(new Runnable() {
                public void run() {

                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    adapter.updateItems(academicsPostsList);
                    // notify the adapter
                    adapter.notifyDataSetChanged();

                    // save the data to shared preferences

                }
            });
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

        adapter.updateItems(academicsPostsList);
        adapter.notifyDataSetChanged();
    }


}
