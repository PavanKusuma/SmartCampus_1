package svecw.smartcampus;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import adapters.ComplaintsFeedbacksAdapter;
import model.ComplaintAndFeedback;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan on 6/12/15.
 */
public class FeedbackActivity extends AppCompatActivity {

    // views of activity
    ListView complaintsOrFeedbacksListView;

    // adapter for complaints
    ComplaintsFeedbacksAdapter complaintsFeedbacksAdapter;

    // list of complaints and feedback
    ArrayList<ComplaintAndFeedback> complaintsFeedbacksList;

    // progress bar
    ProgressBar progressBar;

    // emptyTextView for list view
    TextView emptyElementView;

    LayoutInflater layoutInflater;
    JSONObject jsonResponse;
    int status;

    // skip counter
    int skipCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.complaintOrFeedback));
        title.setTypeface(sansFont);

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // get views from activity
        complaintsOrFeedbacksListView = (ListView) findViewById(R.id.complaintsORFeebacksListView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        emptyElementView = (TextView) findViewById(R.id.emptyElement); emptyElementView.setTypeface(sansFont);

        // list of complaints and feedback
        complaintsFeedbacksList = new ArrayList<ComplaintAndFeedback>();

        // object for adapter
        complaintsFeedbacksAdapter = new ComplaintsFeedbacksAdapter(FeedbackActivity.this);
        complaintsOrFeedbacksListView.setAdapter(complaintsFeedbacksAdapter);

        // empty text view for list view
        //complaintsOrFeedbacksListView.setEmptyView(emptyElementView);

        // get complaints and feedback from db
        new GetFeedback().execute(Routes.getFeedback, String.valueOf(skipCounter));

    }
/*

    class GetFeedback extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            // date format for displaying created date
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, mm:ss");
            simpleDateFormat.format(new Date());

            try {
                // create parse object for the "CollegeWall"
                ParseQuery<ParseObject> studentPostsQuery = ParseQuery.getQuery(Constants.Complaint_FeedbackTable);

                studentPostsQuery.addDescendingOrder(Constants.createdAt);
                // get mapping for the list of objectIds
                //issuesQuery.whereEqualTo(Constants.TYPE, Constants.OFFICIAL);

                // get the list of shouts
                studentPostsQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {

                        if (e == null) {

                            if (parseObjects.size() > 0) {

                                // navigate through the list of objects
                                for (int i = 0; i < parseObjects.size(); i++) {

                                    try {

                                        // create object for collegeWallPost class
                                        final ComplaintAndFeedback complaintAndFeedback = new ComplaintAndFeedback();

                                        // set the values of the wall post into object
                                        complaintAndFeedback.setDescription(parseObjects.get(i).getString(Constants.description));
                                        complaintAndFeedback.setCreatedAt(parseObjects.get(i).getCreatedAt());
                                        complaintAndFeedback.setType(parseObjects.get(i).getString(Constants.type));
                                        complaintAndFeedback.setUserName(parseObjects.get(i).getString(Constants.userName));

                                        complaintsFeedbacksList.add(complaintAndFeedback);

                                        complaintsFeedbacksAdapter.updateItems(complaintsFeedbacksList);
                                        complaintsFeedbacksAdapter.notifyDataSetChanged();

                                        progressBar.setVisibility(View.GONE);


                                    } catch (Exception ex) {

                                        Log.v(Constants.appName, ex.getMessage());
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }

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

                    */
/**
                     * Updating parsed JSON data into ListView
                     * *//*


                    complaintsFeedbacksAdapter.updateItems(complaintsFeedbacksList);
                    // notify the adapter
                    complaintsFeedbacksAdapter.notifyDataSetChanged();

                    // save the data to shared preferences
                    //sharedPreferences.saveWallPosts(studentWallPostsList);

                }
            });
        }
    }
*/

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

        complaintsFeedbacksAdapter.updateItems(complaintsFeedbacksList);
        complaintsFeedbacksAdapter.notifyDataSetChanged();
    }


    /**
     * This background task will fetch feedback and display
     */
    private class GetFeedback extends AsyncTask<String, Void, Void>{

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

            } catch (Exception ex) {

                ex.printStackTrace();
                Error = ex.getMessage();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(FeedbackActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                });


            } finally {

                try {

                    reader.close();

                } catch (Exception ex) {
                    Error = ex.getMessage();
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // check the status and proceed with the logic
                            switch (status){

                                // exception occurred
                                case -3:

                                    // hide loading
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // exception occurred
                                case -2:

                                    // hide loading
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // session exists
                                case 0:

                                    // hide loading
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), R.string.noData, Toast.LENGTH_SHORT).show();
                                    break;

                                // data found
                                case 1:

                                    try {

                                        // get JSON Array of 'details'
                                        JSONArray jsonArray = jsonResponse.getJSONArray(Constants.details);

                                        for(int i=0; i<jsonArray.length(); i++) {

                                            // get the JSON object inside Array
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                            ComplaintAndFeedback complaintAndFeedback = new ComplaintAndFeedback();

                                            // set the values of the wall post into object
                                            complaintAndFeedback.setFeedbackId(jsonObject.getString(Constants.feedbackId));
                                            complaintAndFeedback.setDescription(jsonObject.getString(Constants.description));
                                            complaintAndFeedback.setCreatedAt(jsonObject.getString(Constants.createdAt));
                                            complaintAndFeedback.setUpdatedAt(jsonObject.getString(Constants.updatedAt));
                                            complaintAndFeedback.setUserObjectId(jsonObject.getString(Constants.userObjectId));
                                            complaintAndFeedback.setUserName(jsonObject.getString(Constants.userName));
                                            complaintAndFeedback.setCollegeId(jsonObject.getString(Constants.collegeId));
                                            complaintAndFeedback.setStatus(jsonObject.getInt(Constants.status));
                                            complaintAndFeedback.setUserImage(jsonObject.getString(Constants.media));

                                            complaintsFeedbacksList.add(complaintAndFeedback);

                                        }

                                        complaintsFeedbacksAdapter.updateItems(complaintsFeedbacksList);
                                        complaintsFeedbacksAdapter.notifyDataSetChanged();

                                        // hide loading
                                        progressBar.setVisibility(View.GONE);

                                        // detect the end of scrolling
                                        complaintsOrFeedbacksListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                            @Override
                                            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                                                int threshold = 1;
                                                int count = complaintsOrFeedbacksListView.getCount();

                                                if (scrollState == SCROLL_STATE_IDLE) {
                                                    if (complaintsOrFeedbacksListView.getLastVisiblePosition() >= count
                                                            - threshold) {

                                                        // show loading
                                                        progressBar.setVisibility(View.VISIBLE);

                                                        // increment the skip counter
                                                        skipCounter = skipCounter + 10;

                                                        // Fetch additional posts skipping existing posts
                                                        new GetFeedback().execute(Routes.getFeedback, String.valueOf(skipCounter));
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                                            }
                                        });


                                        break;
                                    }
                                    catch(Exception e){

                                        Log.e(Constants.error, e.getMessage());
                                        Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();

                                    }


                            }
                        }
                    });





                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();

                }

            }


        }

    }
}
