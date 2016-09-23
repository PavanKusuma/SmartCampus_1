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
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import internaldb.SmartCampusDB;
import utils.Constants;
import utils.Routes;
import utils.Snippets;

/**
 * Created by Pavan on 5/21/15.
 */
public class AdminPanel_SelectPrivilege extends AppCompatActivity {

    // privileges as checkboxes
    CheckBox collegeWallCheckBox, moderatorCheckBox, complaintOrFeedbackCheckBox, collegeDirectoryCheckBox, cseDirectoryCheckBox,
            eceDirectoryCheckBox, eeeDirectoryCheckBox, itDirectoryCheckBox, mechDirectoryCheckBox, civilDirectoryCheckBox,
            mbaDirectoryCheckBox, mcaDirectoryCheckBox, basicScienceDirectoryCheckBox;

    RelativeLayout collegeWallView, moderatorView, complaintOrFeedbackView, collegeDirectoryView, cseDirectoryView, eceDirectoryView,
    eeeDirectoryView, itDirectoryView, mechDirectoryView, civilDirectoryView, mbaDirectoryView, mcaDirectoryView, basicScienceDirectoryView;

    // finish button view
    TextView finishBtn;

    // selected privileges list
    ArrayList<String> listOfPrivileges = new ArrayList<String>();

    // status of privilege selection true/false
    Boolean collegeWall=false, moderator=false, complaintOrFeedback=false, collegeDirectory=false, cseDirectory=false, eceDirectory=false,
            eeeDirectory=false, itDirectory=false,
    mechDirectory=false, civilDirectory=false, mbaDirectory=false, mcaDirectory=false, basicScienceDirectory=false;

    // list of existing privileges
    ArrayList<String> existingPrivileges, assigningPrivileges;

    // list of user privileges to be assigned
    ArrayList<String> userPrivileges;

    // selected userObjectId
    String userObjectId = "";
    int moderating = 0;

    // layout inflater
    LayoutInflater layoutInflater;

    JSONObject jsonResponse;
    int status;

    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminpanel_selectprivilege_activity);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.selectPrivilege));
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
        collegeWallCheckBox = (CheckBox) findViewById(R.id.collegeWallCheckBox);
        moderatorCheckBox = (CheckBox) findViewById(R.id.moderatorCheckBox);
        complaintOrFeedbackCheckBox = (CheckBox) findViewById(R.id.complaintOrFeedbackCheckBox);
        collegeDirectoryCheckBox = (CheckBox) findViewById(R.id.collegeDirectoryCheckBox);
        cseDirectoryCheckBox = (CheckBox) findViewById(R.id.cseDirectoryCheckBox);
        eceDirectoryCheckBox = (CheckBox) findViewById(R.id.eceDirectoryCheckBox);
        eeeDirectoryCheckBox = (CheckBox) findViewById(R.id.eeeDirectoryCheckBox);
        itDirectoryCheckBox = (CheckBox) findViewById(R.id.itDirectoryCheckBox);
        mechDirectoryCheckBox = (CheckBox) findViewById(R.id.mechDirectoryCheckBox);
        civilDirectoryCheckBox = (CheckBox) findViewById(R.id.civilDirectoryCheckBox);
        mbaDirectoryCheckBox = (CheckBox) findViewById(R.id.mbaDirectoryCheckBox);
        mcaDirectoryCheckBox = (CheckBox) findViewById(R.id.mcaDirectoryCheckBox);
        basicScienceDirectoryCheckBox = (CheckBox) findViewById(R.id.basicScienceDirectoryCheckBox);

        collegeWallView = (RelativeLayout) findViewById(R.id.collegeWallView);
        moderatorView = (RelativeLayout) findViewById(R.id.moderatorView);
        complaintOrFeedbackView = (RelativeLayout) findViewById(R.id.complaintOrFeedbackView);
        collegeDirectoryView = (RelativeLayout) findViewById(R.id.collegeDirectoryView);
        cseDirectoryView = (RelativeLayout) findViewById(R.id.cseDirectoryView);
        itDirectoryView = (RelativeLayout) findViewById(R.id.itDirectoryView);
        eceDirectoryView = (RelativeLayout) findViewById(R.id.eceDirectoryView);
        eeeDirectoryView = (RelativeLayout) findViewById(R.id.eeeDirectoryView);
        mechDirectoryView = (RelativeLayout) findViewById(R.id.mechDirectoryView);
        civilDirectoryView = (RelativeLayout) findViewById(R.id.civilDirectoryView);
        mbaDirectoryView = (RelativeLayout) findViewById(R.id.mbaDirectoryView);
        mcaDirectoryView = (RelativeLayout) findViewById(R.id.mcaDirectoryView);
        basicScienceDirectoryView = (RelativeLayout) findViewById(R.id.basicScienceDirectoryView);

        finishBtn = (TextView) findViewById(R.id.finishBtn); finishBtn.setTypeface(sansFont);

        existingPrivileges = new ArrayList<String>();
        assigningPrivileges = new ArrayList<String>();
        userPrivileges = new ArrayList<String>();

        // get selected user objectId
        userObjectId = getIntent().getStringExtra(Constants.objectId);

        // manage the checkbox checked state on layout click event
        // toggle between the checked state manually
        collegeWallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(collegeWallCheckBox.isChecked()){

                    collegeWall = false;
                    collegeWallCheckBox.setChecked(false);

                } else {

                    collegeWall = true;
                    collegeWallCheckBox.setChecked(true);
                }

            }
        });

  /*      collegeWallCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(collegeWallCheckBox.isChecked()){

                    collegeWall = false;
                    collegeWallCheckBox.setChecked(false);

                } else {

                    collegeWall = true;
                    collegeWallCheckBox.setChecked(true);
                }
            }
        });*/







        moderatorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(moderatorCheckBox.isChecked()){

                    moderator = false;
                    moderatorCheckBox.setChecked(false);

                } else {

                    moderator = true;
                    moderatorCheckBox.setChecked(true);
                }
            }
        });

    /*    moderatorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(moderatorCheckBox.isChecked()){

                    moderator = false;
                    moderatorCheckBox.setChecked(false);

                } else {

                    moderator = true;
                    moderatorCheckBox.setChecked(true);
                }
            }
        });


*/


        complaintOrFeedbackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(complaintOrFeedbackCheckBox.isChecked()) {

                    complaintOrFeedback = false;
                    complaintOrFeedbackCheckBox.setChecked(false);

                } else {

                    complaintOrFeedback = true;
                    complaintOrFeedbackCheckBox.setChecked(true);
                }
            }
        });

   /*     complaintOrFeedbackCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(complaintOrFeedbackCheckBox.isChecked()) {

                    complaintOrFeedback = false;
                    complaintOrFeedbackCheckBox.setChecked(false);

                } else {

                    complaintOrFeedback = true;
                    complaintOrFeedbackCheckBox.setChecked(true);
                }
            }
        });
*/





        collegeDirectoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(collegeDirectoryCheckBox.isChecked()){

                    collegeDirectory = false;
                    collegeDirectoryCheckBox.setChecked(false);

                } else {

                    collegeDirectory = true;
                    collegeDirectoryCheckBox.setChecked(true);
                }
            }
        });

        /*collegeDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(collegeDirectoryCheckBox.isChecked()){

                    collegeDirectory = false;
                    collegeDirectoryCheckBox.setChecked(false);

                } else {

                    collegeDirectory = true;
                    collegeDirectoryCheckBox.setChecked(true);
                }
            }
        });

*/



        cseDirectoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cseDirectoryCheckBox.isChecked()){

                    cseDirectory = false;
                    cseDirectoryCheckBox.setChecked(false);

                } else {

                    cseDirectory = true;
                    cseDirectoryCheckBox.setChecked(true);
                }
            }
        });

       /* cseDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(cseDirectoryCheckBox.isChecked()){

                    cseDirectory = false;
                    cseDirectoryCheckBox.setChecked(false);

                } else {

                    cseDirectory = true;
                    cseDirectoryCheckBox.setChecked(true);
                }
            }
        });
*/




        itDirectoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(itDirectoryCheckBox.isChecked()){

                    itDirectory = false;
                    itDirectoryCheckBox.setChecked(false);

                } else {

                    itDirectory = true;
                    itDirectoryCheckBox.setChecked(true);
                }
            }
        });

        /*itDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(itDirectoryCheckBox.isChecked()){

                    itDirectory = false;
                    itDirectoryCheckBox.setChecked(false);

                } else {

                    itDirectory = true;
                    itDirectoryCheckBox.setChecked(true);
                }
            }
        });
*/




        eceDirectoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(eceDirectoryCheckBox.isChecked()){

                    eceDirectory = false;
                    eceDirectoryCheckBox.setChecked(false);

                } else {

                    eceDirectory = true;
                    eceDirectoryCheckBox.setChecked(true);
                }
            }
        });

        /*eceDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(eceDirectoryCheckBox.isChecked()){

                    eceDirectory = false;
                    eceDirectoryCheckBox.setChecked(false);

                } else {

                    eceDirectory = true;
                    eceDirectoryCheckBox.setChecked(true);
                }
            }
        });
*/




        eeeDirectoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(eeeDirectoryCheckBox.isChecked()){

                    eeeDirectory = false;
                    eeeDirectoryCheckBox.setChecked(false);

                } else {

                    eeeDirectory = true;
                    eeeDirectoryCheckBox.setChecked(true);
                }
            }
        });

        /*eeeDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(eeeDirectoryCheckBox.isChecked()){

                    eeeDirectory = false;
                    eeeDirectoryCheckBox.setChecked(false);

                } else {

                    eeeDirectory = true;
                    eeeDirectoryCheckBox.setChecked(true);
                }
            }
        });

*/





        mechDirectoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mechDirectoryCheckBox.isChecked()){

                    mechDirectory = false;
                    mechDirectoryCheckBox.setChecked(false);

                } else {

                    mechDirectory = true;
                    mechDirectoryCheckBox.setChecked(true);
                }
            }
        });

        /*mechDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(mechDirectoryCheckBox.isChecked()){

                    mechDirectory = false;
                    mechDirectoryCheckBox.setChecked(false);

                } else {

                    mechDirectory = true;
                    mechDirectoryCheckBox.setChecked(true);
                }
            }
        });
*/




        //--------------------------------------------------------

        // on click event of finish btn
        // get the selected privileges and store it to db for the particular username
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if anyone of the checkbox is selected
                if(collegeWall || moderator || complaintOrFeedback || collegeDirectory || cseDirectory || eceDirectory || eeeDirectory
                        || itDirectory || mechDirectory || civilDirectory || mbaDirectory || mcaDirectory || basicScienceDirectory){


                        // prepare the directory and moderating values
                                if (collegeWall) {

                                    listOfPrivileges.add(Constants.collegeWall);

                                }
                                if (moderator) {

                                    moderating = 1;
                                }
                                if(complaintOrFeedback) {

                                    listOfPrivileges.add(Constants.complaintOrFeedback);
                                }
                                if (collegeDirectory) {

                                    listOfPrivileges.add(Constants.collegeDirectory);
                                }
                                if (cseDirectory) {

                                    listOfPrivileges.add(Constants.cseDirectory);
                                }
                                if (eceDirectory) {

                                    listOfPrivileges.add(Constants.eceDirectory);
                                }
                                if (eeeDirectory) {

                                    listOfPrivileges.add(Constants.eeeDirectory);
                                }
                                if (itDirectory) {

                                    listOfPrivileges.add(Constants.itDirectory);
                                }
                                if (mechDirectory) {

                                    listOfPrivileges.add(Constants.mechDirectory);
                                }
                                if (civilDirectory) {

                                    listOfPrivileges.add(Constants.civilDirectory);
                                }
                                if (mbaDirectory) {

                                    listOfPrivileges.add(Constants.mbaDirectory);
                                }
                                if (mcaDirectory) {

                                    listOfPrivileges.add(Constants.mcaDirectory);
                                }
                                if (basicScienceDirectory) {

                                    listOfPrivileges.add(Constants.basicScienceDirectory);
                                }


                            // empty privilege string
                            String privileges = "";

                            // form privilege string with delimiters
                            for(int i=0; i<listOfPrivileges.size(); i++){

                                privileges = privileges + listOfPrivileges.get(i) + ",";
                            }


                        // prepare the privilege URL
                        //String privilegeURL = Routes.updateUserPrivileges + Constants.key + "/" + smartCampusDB.getUser().get(Constants.userObjectId) + "/" +
                                //moderating + "/" + Snippets.escapeURIPathParam(privileges) + "/" + Snippets.getUniquePrivilegeId();

                        // update the privileges of given user
                        new UpdatePrivileges().execute(Routes.updateUserPrivileges, smartCampusDB.getUser().get(Constants.userObjectId).toString(), Snippets.escapeURIPathParam(privileges).toString(), Snippets.getUniquePrivilegeId().toString());
                        // save the privileges object to the db
                        //privilegesObject.saveInBackground();

                } else {

                    Toast.makeText(getApplicationContext(), "No Privilege selected", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    // handles mutliple click events
    public void onClick(View v){


    }

    /**
     * Update user privileges
     */
    private class UpdatePrivileges extends AsyncTask<String, Void, Void> {

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
                        + "&" + URLEncoder.encode(Constants.moderating, "UTF-8") + "=" + moderating
                        + "&" + URLEncoder.encode(Constants.directory, "UTF-8") + "=" + (urls[2])
                        + "&" + URLEncoder.encode(Constants.privilegeId, "UTF-8") + "=" + (urls[3]);


                Log.v(Constants.appName, urls[0]);

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
                                    Toast.makeText(getApplicationContext(), "Privileges updated", Toast.LENGTH_SHORT).show();

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
