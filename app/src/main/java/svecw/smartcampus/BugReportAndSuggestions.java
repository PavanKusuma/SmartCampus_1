package svecw.smartcampus;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import internaldb.SmartCampusDB;
import utils.Constants;
import utils.Routes;
import utils.Snippets;

/**
 * Created by Pavan_Kusuma on 9/26/2016.
 */

public class BugReportAndSuggestions extends AppCompatActivity {

    // views of activity
    TextInputEditText description;
    TextView descriptionHint;

    // default Type
    String selectionType = Constants.Complaint;
    String descriptionText;

    // object for internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    JSONObject jsonResponse;
    int status;

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bugreport_suggestions);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.bugReport));
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
        description = (TextInputEditText) findViewById(R.id.description); description.setTypeface(sansFont);
        descriptionHint = (TextView) findViewById(R.id.descriptionhHint); descriptionHint.setTypeface(sansFont);

        /*complaintType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // change the color of name search factor to blue
                // to show it active
                complaintType.setTextColor(Color.WHITE);
                complaintType.setBackgroundColor(Color.parseColor("#4683ea"));

                feedbackType.setTextColor(Color.parseColor("#595959"));
                feedbackType.setBackgroundColor(Color.WHITE);

                // get the active search factor
                selectionType = Constants.Complaint;
            }
        });

        feedbackType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // change the color of name search factor to blue
                // to show it active
                feedbackType.setTextColor(Color.WHITE);
                feedbackType.setBackgroundColor(Color.parseColor("#4683ea"));

                complaintType.setTextColor(Color.parseColor("#595959"));
                complaintType.setBackgroundColor(Color.WHITE);

                // get the active search factor
                selectionType = Constants.Feedback;
            }
        });
*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.post) {

            // create new post
            createNewBugReport();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    // this method will fetch the user inputs and call out request
    public void createNewBugReport() {

        // check if text is provided in given edit text
        if(description.getText().length()>0){

            // get the description
            descriptionText = description.getText().toString();

            // get url
            String feedbackURL = Routes.createBugReport + Constants.key + "/" + smartCampusDB.getUser().get(Constants.userObjectId) + "/" +
                    Snippets.getUniqueFeedbackId() + "/" + Snippets.escapeURIPathParam(descriptionText);

            // create feedback
            new CreateFeedback().execute(Routes.createBugReport, smartCampusDB.getUser().get(Constants.userObjectId).toString(), Snippets.getUniqueFeedbackId(), Snippets.escapeURIPathParam(descriptionText));


        } else {

            Toast.makeText(getApplicationContext(), "Write something", Toast.LENGTH_SHORT).show();

        }

    }
    /**
     * This background task will create feedback provided by the respective user
     */
    private class CreateFeedback extends AsyncTask<String, Void, Void> {

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
                        + "&" + URLEncoder.encode(Constants.userObjectId, "UTF-8") + "=" + (urls[1])
                        + "&" + URLEncoder.encode(Constants.feedbackId, "UTF-8") + "=" + (urls[2])
                        + "&" + URLEncoder.encode(Constants.description, "UTF-8") + "=" + (urls[3]);

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

                        Toast.makeText(BugReportAndSuggestions.this, "Please try again", Toast.LENGTH_SHORT).show();
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

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // key mismatch
                                case -2:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // data found
                                case 1:

                                    Toast.makeText(getApplicationContext(), "Thanks for writing to us!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;


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

