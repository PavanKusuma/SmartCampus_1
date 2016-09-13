package svecw.smartcampus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan on 6/4/15.
 */
public class AdminPanel_UpdateBranchAction extends AppCompatActivity {

    // views from activity
    Spinner fromYear, fromBranch, fromSemseter, toYear, toBranch, toSemester;
    TextView finishBtn;

    // layout inflater
    LayoutInflater layoutInflater;

    JSONObject jsonResponse;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminpanel_updatebranchaction_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.updateStudentBranch));

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
        fromYear = (Spinner) findViewById(R.id.fromYear);
        fromBranch = (Spinner) findViewById(R.id.fromBranch);
        fromSemseter = (Spinner) findViewById(R.id.fromSemester);
        toYear = (Spinner) findViewById(R.id.toYear);
        toBranch = (Spinner) findViewById(R.id.toBranch);
        toSemester = (Spinner) findViewById(R.id.toSemester);
        finishBtn = (TextView) findViewById(R.id.branchUpdateFinish);

        toBranch.setEnabled(false);

        fromBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                toBranch.setSelection(position);
                toBranch.getSelectedView().setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if there is change in semester from and to values
                if(!fromSemseter.getSelectedItem().toString().contentEquals(toSemester.getSelectedItem().toString())){


                    // prepare the url
                    String branchURL = Routes.updateUserBranchDetails + Constants.key + "/" + fromBranch.getSelectedItem().toString() + "/" +
                            Integer.valueOf(fromYear.getSelectedItem().toString()) + "/" + Integer.valueOf(fromSemseter.getSelectedItem().toString()) + "/" + toBranch.getSelectedItem().toString() + "/" +
                            Integer.valueOf(toYear.getSelectedItem().toString()) + "/" + Integer.valueOf(toSemester.getSelectedItem().toString());


                    // update user branch details
                    new UpdateBranchDetails().execute(Routes.updateUserBranchDetails, fromBranch.getSelectedItem().toString(), fromYear.getSelectedItem().toString(), fromSemseter.getSelectedItem().toString(), toBranch.getSelectedItem().toString(), toYear.getSelectedItem().toString(), toSemester.getSelectedItem().toString());
                    /*
                    // query to fetch currently selected students data
                    ParseQuery<ParseObject> branchUpdateQuery = ParseQuery.getQuery(Constants.users);

                    branchUpdateQuery.whereEqualTo(Constants.branch, fromBranch.getSelectedItem().toString());
                    branchUpdateQuery.whereEqualTo(Constants.year, fromYear.getSelectedItem().toString());
                    branchUpdateQuery.whereEqualTo(Constants.semester, fromSemseter.getSelectedItem().toString());

                    branchUpdateQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {

                            // check if there is no exception
                            if(e==null){

                                // check if data is present
                                if(list.size()>0){

                                    for(int i=0; i<list.size(); i++){

                                        // get the selected object and update the data
                                        ParseObject updateBranchObject = list.get(i);

                                        updateBranchObject.put(Constants.branch, toBranch.getSelectedItem().toString());
                                        updateBranchObject.put(Constants.year, toYear.getSelectedItem().toString());
                                        updateBranchObject.put(Constants.semester, toSemester.getSelectedItem().toString());

                                        updateBranchObject.saveInBackground();
                                    }
                                }
                            }

                        }
                    });*/

                } else {

                    // show the snackBar as that is wrong udpated
                    Toast.makeText(getApplicationContext(), "Invalid update", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }




    /**
     * Update user privileges
     */
    private class UpdateBranchDetails extends AsyncTask<String, Void, Void> {

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
                        + "&" + URLEncoder.encode(Constants.fromBranch, "UTF-8") + "=" + (urls[1])
                        + "&" + URLEncoder.encode(Constants.fromYear, "UTF-8") + "=" + Integer.valueOf(urls[2])
                        + "&" + URLEncoder.encode(Constants.fromSemester, "UTF-8") + "=" + Integer.valueOf(urls[3])
                        + "&" + URLEncoder.encode(Constants.toBranch, "UTF-8") + "=" + (urls[4])
                        + "&" + URLEncoder.encode(Constants.toYear, "UTF-8") + "=" + Integer.valueOf(urls[5])
                        + "&" + URLEncoder.encode(Constants.toSemester, "UTF-8") + "=" + Integer.valueOf(urls[6]);


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

                                // session exists
                                case 0:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // data founc
                                case 1:

                                    // show success message
                                    Toast.makeText(getApplicationContext(), "Details updated", Toast.LENGTH_SHORT).show();

                                    // close the screen
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
