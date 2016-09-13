package svecw.smartcampus;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import internaldb.SmartCampusDB;
import model.Privilege;
import utils.Constants;
import utils.Routes;
import utils.Snippets;

/**
 * Created by Pavan on 5/21/15.
 */
public class AdminPanel_SelectPrivileges extends AppCompatActivity {

    // privileges as checkboxes
    CheckBox collegeWallCheckBox, moderatorCheckBox, complaintOrFeedbackCheckBox, collegeDirectoryCheckBox, studentDirectoryCheckBox, cseDirectoryCheckBox,
            eceDirectoryCheckBox, eeeDirectoryCheckBox, itDirectoryCheckBox, mechDirectoryCheckBox, civilDirectoryCheckBox,
            mbaDirectoryCheckBox, mcaDirectoryCheckBox, basicScienceDirectoryCheckBox;

    //RelativeLayout collegeWallView, moderatorView, complaintOrFeedbackView, collegeDirectoryView, cseDirectoryView, eceDirectoryView, eeeDirectoryView, itDirectoryView, mechDirectoryView, civilDirectoryView, mbaDirectoryView, mcaDirectoryView, basicScienceDirectoryView;

    // finish button view
    TextView finishBtn;

    // selected privileges list
    ArrayList<String> listOfPrivileges = new ArrayList<String>();

    // status of privilege selection true/false
    Boolean collegeWall=false, moderator=false, complaintOrFeedback=false, collegeDirectory=false, studentDirectory=false, cseDirectory=false, eceDirectory=false,
            eeeDirectory=false, itDirectory=false,
            mechDirectory=false, civilDirectory=false, mbaDirectory=false, mcaDirectory=false, basicScienceDirectory=false;

    // object for storing previous privileges if any
    Privilege privilege = new Privilege();

    // progress dialog while fetching previous privileges
    ProgressDialog progressDialog;



    // select privilege list to assign to the layout while loading
//    ArrayList<SelectPrivilege> selectPrivilegeList = new ArrayList<SelectPrivilege>();

    // actual privileges present in the app
  //  ArrayList<String> privilegesList = new ArrayList<String>();

    // adapter for populating list view
    //AdminPanelSelectPrivilegeAdapter selectPrivilegeAdapter = new AdminPanelSelectPrivilegeAdapter(AdminPanel_SelectPrivileges.this);



    // list of existing privileges
    ArrayList<String> existingPrivileges, assigningPrivileges;

    // list of user privileges to be assigned
    ArrayList<String> userPrivileges;

    // selected userObjectId
    String userObjectId = "", username;
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

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.selectPrivilege));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);



/*
        // prepare the actual privileges present in the app
        privilegesList.add(Constants.collegeWall);
        privilegesList.add(Constants.moderating);
        privilegesList.add(Constants.complaintOrFeedback);
        privilegesList.add(Constants.collegeDirectory);
        privilegesList.add(Constants.cseDirectory);
        privilegesList.add(Constants.eceDirectory);
        privilegesList.add(Constants.eeeDirectory);
        privilegesList.add(Constants.itDirectory);
        privilegesList.add(Constants.mechDirectory);
        privilegesList.add(Constants.civilDirectory);
        privilegesList.add(Constants.mcaDirectory);
        privilegesList.add(Constants.mbaDirectory);
        privilegesList.add(Constants.basicScienceDirectory);


        // iterate through privilegesList and assign it to selectPrivilege object list
        for(int i=0; i<privilegesList.size(); i++) {

            // create object for select privilege to assign to layout
            SelectPrivilege selectPrivilege = new SelectPrivilege();
            selectPrivilege.setPrivilege(privilegesList.get(i));
            selectPrivilege.setSelected(false);

            // add to list
            selectPrivilegeList.add(selectPrivilege);

        }

        // update the adapter and notify
        selectPrivilegeAdapter.updateItems(selectPrivilegeList);
        selectPrivilegeAdapter.notifyDataSetChanged();




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
        basicScienceDirectoryView = (RelativeLayout) findViewById(R.id.basicScienceDirectoryView);*/


        // get views from activity
        collegeWallCheckBox = (CheckBox) findViewById(R.id.collegeWallCheckBox);
        moderatorCheckBox = (CheckBox) findViewById(R.id.moderatorCheckBox);
        complaintOrFeedbackCheckBox = (CheckBox) findViewById(R.id.complaintOrFeedbackCheckBox);
        collegeDirectoryCheckBox = (CheckBox) findViewById(R.id.collegeDirectoryCheckBox);
        studentDirectoryCheckBox = (CheckBox) findViewById(R.id.studentDirectoryCheckBox);
        cseDirectoryCheckBox = (CheckBox) findViewById(R.id.cseDirectoryCheckBox);
        eceDirectoryCheckBox = (CheckBox) findViewById(R.id.eceDirectoryCheckBox);
        eeeDirectoryCheckBox = (CheckBox) findViewById(R.id.eeeDirectoryCheckBox);
        itDirectoryCheckBox = (CheckBox) findViewById(R.id.itDirectoryCheckBox);
        mechDirectoryCheckBox = (CheckBox) findViewById(R.id.mechDirectoryCheckBox);
        civilDirectoryCheckBox = (CheckBox) findViewById(R.id.civilDirectoryCheckBox);
        mbaDirectoryCheckBox = (CheckBox) findViewById(R.id.mbaDirectoryCheckBox);
        mcaDirectoryCheckBox = (CheckBox) findViewById(R.id.mcaDirectoryCheckBox);
        basicScienceDirectoryCheckBox = (CheckBox) findViewById(R.id.basicScienceDirectoryCheckBox);


        finishBtn = (TextView) findViewById(R.id.finishBtn);

        existingPrivileges = new ArrayList<String>();
        assigningPrivileges = new ArrayList<String>();
        userPrivileges = new ArrayList<String>();

        // get selected user objectId
        userObjectId = getIntent().getStringExtra(Constants.objectId);
        username = getIntent().getStringExtra(Constants.userName);


        // fetch the previous privileges of the selected user if any are present
        new GetPrivilegesOfCurrentUser().execute(Routes.getPrivilegesOfUser, userObjectId);


        // manage the checkbox checked state on layout click event
        // toggle between the checked state manually
        collegeWallCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!collegeWallCheckBox.isChecked()){

                    collegeWall = false;

                } else {

                    collegeWall = true;
                }

            }
        });



        moderatorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!moderatorCheckBox.isChecked()){

                    moderator = false;

                } else {

                    moderator = true;
                }
            }
        });


        complaintOrFeedbackCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!complaintOrFeedbackCheckBox.isChecked()) {

                    complaintOrFeedback = false;

                } else {

                    complaintOrFeedback = true;
                }
            }
        });



        collegeDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!collegeDirectoryCheckBox.isChecked()){

                    collegeDirectory = false;

                } else {

                    collegeDirectory = true;
                }
            }
        });


        studentDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!studentDirectoryCheckBox.isChecked()){

                    studentDirectory = false;

                } else {

                    studentDirectory = true;
                }
            }
        });

        cseDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!cseDirectoryCheckBox.isChecked()){

                    cseDirectory = false;

                } else {

                    cseDirectory = true;
                }
            }
        });



        itDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!itDirectoryCheckBox.isChecked()){

                    itDirectory = false;

                } else {

                    itDirectory = true;
                }
            }
        });




        eceDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!eceDirectoryCheckBox.isChecked()){

                    eceDirectory = false;

                } else {

                    eceDirectory = true;
                }
            }
        });


        eeeDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!eeeDirectoryCheckBox.isChecked()){

                    eeeDirectory = false;

                } else {

                    eeeDirectory = true;
                }
            }
        });


        mechDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!mechDirectoryCheckBox.isChecked()){

                    mechDirectory = false;

                } else {

                    mechDirectory = true;
                }
            }
        });

        civilDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!civilDirectoryCheckBox.isChecked()){

                    civilDirectory = false;

                } else {

                    civilDirectory = true;
                }
            }
        });

        mbaDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!mbaDirectoryCheckBox.isChecked()){

                    mbaDirectory = false;

                } else {

                    mbaDirectory = true;
                }
            }
        });

        mcaDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!mcaDirectoryCheckBox.isChecked()){

                    mcaDirectory = false;

                } else {

                    mcaDirectory = true;
                }
            }
        });

        basicScienceDirectoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!basicScienceDirectoryCheckBox.isChecked()){

                    basicScienceDirectory = false;

                } else {

                    basicScienceDirectory = true;
                }
            }
        });

        //--------------------------------------------------------

        // on click event of finish btn
        // get the selected privileges and store it to db for the particular username
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                if (studentDirectory) {

                    listOfPrivileges.add(Constants.studentDirectory);
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



                // check if anyone of the checkbox is selected
                if(collegeWall || moderator || complaintOrFeedback || collegeDirectory || studentDirectory || cseDirectory || eceDirectory || eeeDirectory
                        || itDirectory || mechDirectory || civilDirectory || mbaDirectory || mcaDirectory || basicScienceDirectory){

                    // check with user whether he is sure of assigning these privileges
                    confirmationMessage("Are you sure to assign these privileges for");

                } else {

                    // check with user whether he is sure of assigning these privileges
                    confirmationMessage("Clearing the privileges for");
                }


            }
        });
    }



    /**
     * This method will confirm user from updating the details to server
     */
    public void confirmationMessage(String message) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked

                        // empty privilege string
                        String privileges = "";

                        // form privilege string with delimiters
                        for(int i=0; i<listOfPrivileges.size(); i++){

                            privileges = privileges + listOfPrivileges.get(i) + ",";
                        }
                        // prepare the privilege URL
                        //String privilegeURL = Routes.updateUserPrivileges + Constants.key + "/" + smartCampusDB.getUser().get(Constants.userObjectId) + "/" +
                          //      moderating + "/" + Snippets.escapeURIPathParam(privileges) + "/" + Snippets.getUniquePrivilegeId();

                        // update the privileges of given user
                        new UpdatePrivileges().execute(Routes.updateUserPrivileges, userObjectId, Snippets.escapeURIPathParam(privileges).toString(), Snippets.getUniquePrivilegeId().toString());
                        //Toast.makeText(EditProfileActivity.this, "Yes Clicked", Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked // do nothing
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message+ " " +username)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
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




    /**
     * This background task will fetch privileges of current user from server, if any
     */
    private class GetPrivilegesOfCurrentUser extends AsyncTask<String, Void, Void>{

        private String Content = "";
        private String Error = null;
        String data = "";

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(AdminPanel_SelectPrivileges.this);
            progressDialog.setMessage("Fetching previous privileges..please wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();

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
                        + "&" + URLEncoder.encode(Constants.userObjectId, "UTF-8") + "=" + (urls[1]);

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

                        Toast.makeText(AdminPanel_SelectPrivileges.this, "Please try again", Toast.LENGTH_SHORT).show();
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

                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // session exists
                                case 0:

                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "No previous privileges exists for selected user", Toast.LENGTH_SHORT).show();
                                    break;

                                // data found
                                case 1:

                                    progressDialog.dismiss();
                                    try {

                                        // get JSON Array of 'details'
                                        JSONArray jsonArray = jsonResponse.getJSONArray(Constants.details);

                                        if(jsonArray.length() > 0){
Log.v(Constants.appName, jsonArray.get(0).toString());
                                            // get the JSON object inside Array
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                                            // set the values of the wall post into object
                                            privilege.setPrivilegeId(jsonObject.getString(Constants.privilegeId));
                                            privilege.setUserObjectId(jsonObject.getString(Constants.userObjectId));
                                            privilege.setModerating(jsonObject.getInt(Constants.moderating));
                                            privilege.setDirectory(jsonObject.getString(Constants.directory));
                                            privilege.setCreatedAt(jsonObject.getString(Constants.createdAt));
                                            privilege.setUpdatedAt(jsonObject.getString(Constants.updatedAt));

                                            // as we have previous privileges for current user
                                            // populate the layout with them
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    if(privilege.getDirectory().contains(Constants.collegeWall)) {
                                                        collegeWallCheckBox.setChecked(true);
                                                        collegeWall = true;
                                                    }

                                                    if(privilege.getModerating() == 1) {
                                                        moderatorCheckBox.setChecked(true);
                                                        moderator = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.complaintOrFeedback)) {
                                                        complaintOrFeedbackCheckBox.setChecked(true);
                                                        complaintOrFeedback = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.collegeDirectory)) {
                                                        collegeDirectoryCheckBox.setChecked(true);
                                                        collegeDirectory = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.studentDirectory)) {
                                                        studentDirectoryCheckBox.setChecked(true);
                                                        studentDirectory = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.cseDirectory)) {
                                                        cseDirectoryCheckBox.setChecked(true);
                                                        cseDirectory = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.itDirectory)) {
                                                        itDirectoryCheckBox.setChecked(true);
                                                        itDirectory = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.eceDirectory)) {
                                                        eceDirectoryCheckBox.setChecked(true);
                                                        eceDirectory = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.eeeDirectory)) {
                                                        eeeDirectoryCheckBox.setChecked(true);
                                                        eeeDirectory = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.mechDirectory)) {
                                                        mechDirectoryCheckBox.setChecked(true);
                                                        mechDirectory = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.civilDirectory)) {
                                                        civilDirectoryCheckBox.setChecked(true);
                                                        civilDirectory = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.mbaDirectory)) {
                                                        mbaDirectoryCheckBox.setChecked(true);
                                                        mbaDirectory = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.mcaDirectory)) {
                                                        mcaDirectoryCheckBox.setChecked(true);
                                                        mcaDirectory = true;
                                                    }

                                                    if(privilege.getDirectory().contains(Constants.basicScienceDirectory)) {
                                                        basicScienceDirectoryCheckBox.setChecked(true);
                                                        basicScienceDirectory = true;
                                                    }
                                                }
                                            });


                                        }
                                        else{

                                            Toast.makeText(getApplicationContext(), "No previous privileges exists for selected user", Toast.LENGTH_SHORT).show();

                                        }



                                        break;
                                    }
                                    catch(Exception e){

                                        progressDialog.dismiss();
                                        Log.e(Constants.error, e.getMessage());
                                        Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();

                                    }


                            }
                        }
                    });





                } catch (JSONException e) {

                    progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();

                }

            }


        }

    }
}
