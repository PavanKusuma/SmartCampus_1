package svecw.smartcampus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import internaldb.SmartCampusDB;
import model.Privilege;
import model.User;
import notify.GCMRegistrationIntentService;
import utils.Constants;
import utils.Routes;
import utils.Snippets;

/**
 * Created by Pavan on 5/20/15.
 */
public class GlobalLoginActivity extends AppCompatActivity {

    // views of activity
    EditText collegeId, secretCode;
    Button nextButton;
    String str_collegeId, str_secretCode;
    TextView errorText;
    ProgressBar progressBar;

    // request status
    int status = -3;
    JSONObject jsonResponse;

    // internal db object
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    //Creating a broadcast receiver for gcm registration
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    // token
    String token = Constants.null_indicator;
    String gcm_error = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // fetch views from layout
        getViews();


            // This particular code will register this device to the gcm server for sending push notifications
            {
                //Checking play service is available or not
                int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

                //if play service is not available
                if (ConnectionResult.SUCCESS != resultCode) {
                    //If play service is supported but not installed
                    if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                        //Displaying message that play service is not installed
                        //Toast.makeText(getApplicationContext(), "To receive notifications install/enabled Google Play Service in this device!", Toast.LENGTH_LONG).show();
                        GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                        gcm_error = "To receive notifications install/enable Google Play Services in this device from Play Store!";
                        //If play service is not supported
                        //Displaying an error message
                    } else {
                        gcm_error = "This device does not support for Google Play Services!";
                        //Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
                    }

                    //If play service is available
                } else {
                    //Starting intent to register device

                    Intent intent = new Intent(this, GCMRegistrationIntentService.class);
                    startService(intent);


                }
            }


            // this below code will register the service

            //Initializing our broadcast receiver
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {

                //When the broadcast received
                //We are sending the broadcast from GCMRegistrationIntentService

                @Override
                public void onReceive(Context context, Intent intent) {
                    //If the broadcast has received with success
                    //that means device is registered successfully
                    if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                        //Getting the registration token from the intent
                        token = intent.getStringExtra("token");
                        //Displaying the token as toast
                        //Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                        //tokenText.setText(token);
                        // save the GCM Reg Id
                        //new CollectGCM_BG(getApplicationContext()).execute(Routes.updateGCM, collegeId);


                        gcm_error = Constants.null_indicator;

                        //if the intent is not with success then displaying error messages
                    } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                        //Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();

                        token = Constants.null_indicator;
                    } else {
                        //Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();

                        token = Constants.null_indicator;
                    }
                }
            };

        // check if user entered the required values
        // verify the secret code for the given email id
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                // assign values
                str_collegeId = collegeId.getText().toString();
                str_secretCode = secretCode.getText().toString();

                // disable the go button as verification is in progress
                nextButton.setEnabled(false);

                // disable the errorText view
                errorText.setVisibility(View.GONE);

                // check if required inputs are present
                if (str_collegeId.length() > 0 && str_secretCode.length() > 0) {

                    if(str_secretCode.toLowerCase().contentEquals("svecw")) {

                        progressBar.setVisibility(View.VISIBLE);
                        // check if the current user have active session
                        // if so, avoid login
                        // else, create new session and fetch the user details

                        // check if gcm token is fetched
                        if(gcm_error.length()==0){
                            progressBar.setVisibility(View.GONE);
                            // disable the errorText view
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText(R.string.loginTryAgain);
                            nextButton.setEnabled(true);
                        }
                        else if(gcm_error.contentEquals(Constants.null_indicator)) {
                            new UserLogin().execute(Routes.login, str_collegeId);
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            // disable the errorText view
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText(gcm_error);
                            nextButton.setEnabled(true);
                        }


                       /* // Inorder to check if the user is using the app in multiple devices
                        // create a session based on collegeId
                        ParseQuery<ParseObject> sessionQ = ParseQuery.getQuery(Constants.Session);

                        sessionQ.whereContains(Constants.collegeId, collegeId.getText().toString());

                        // get the count
                        sessionQ.countInBackground(new CountCallback() {
                            @Override
                            public void done(int i, ParseException e) {

                                if (e == null) {

                                    // count should be '0'
                                    // it means that user didn't login on any device yet
                                    if (i == 0) {

                                        // proceed with the login logic
                                        ParseQuery<ParseObject> loginQ = ParseQuery.getQuery(Constants.users);

                                        loginQ.whereContains(Constants.collegeId, collegeId.getText().toString());

                                        // check if the user is faculty or admin, if so then match the secretCode with "SVECW"
                                        //if(secretCode.getText().toString().toLowerCase().contentEquals("svecw"))
                                        //loginQ.whereContains(Constants.objectId, secretCode.getText().toString());

                                        loginQ.getFirstInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {

                                                try {

                                                    if (e == null) {

                                                        if (parseObject != null) {
                                                            // get the data and store in internal db
                                                            User user = new User();

                                                            user.setObjectId(parseObject.getObjectId()); // secretCode.getText().toString()
                                                            user.setUserName(parseObject.getString(Constants.userName));
                                                            user.setEmail(collegeId.getText().toString());
                                                            user.setPhoneNumber(parseObject.getString(Constants.phoneNumber));
                                                            user.setCollegeId(parseObject.getString(Constants.collegeId));
                                                            user.setBranch(parseObject.getString(Constants.branch));
                                                            user.setSemester(parseObject.getInt(Constants.semester));
                                                            user.setYear(parseObject.getInt(Constants.year));
                                                            user.setRole(parseObject.getString(Constants.role));

                                                            user.setMessages(parseObject.getInt(Constants.messages));
                                                            user.setKnowledgeWall(parseObject.getInt(Constants.knowledgeWall));
                                                            user.setCollegeWall(parseObject.getInt(Constants.collegeWall));
                                                            user.setStudentWall(parseObject.getInt(Constants.studentWall));

                                                            user.setModerating(parseObject.getInt(Constants.moderating));
                                                            user.setCollegeDirectory(parseObject.getInt(Constants.collegeDirectory));
                                                            user.setCseDirectory(parseObject.getInt(Constants.cseDirectory));
                                                            user.setEceDirectory(parseObject.getInt(Constants.eceDirectory));
                                                            user.setEeeDirectory(parseObject.getInt(Constants.eeeDirectory));
                                                            user.setItDirectory(parseObject.getInt(Constants.itDirectory));
                                                            user.setMechDirectory(parseObject.getInt(Constants.mechDirectory));
                                                            user.setCivilDirectory(parseObject.getInt(Constants.civilDirectory));
                                                            user.setMbaDirectory(parseObject.getInt(Constants.mbaDirectory));
                                                            user.setMcaDirectory(parseObject.getInt(Constants.mcaDirectory));
                                                            user.setBasicScienceDirectory(parseObject.getInt(Constants.basicScienceDirectory));
                                                            user.setComplaintOrFeedback(parseObject.getInt(Constants.complaintOrFeedback));

                                                            // get privileges
                                                            //user.setPrivileges((ArrayList<String>)parseObject.get(Constants.privilege));


                                                            // check if the role is student, then secret code should be equal to objectId
                                                            //if((parseObject.getString(Constants.role).contentEquals(Constants.student) && parseObject.getObjectId().contentEquals(secretCode.getText().toString())) ||
                                                            //      (parseObject.getString(Constants.role).contentEquals(Constants.faculty) && secretCode.getText().toString().toLowerCase().contentEquals("svecw"))) {


                                                            // navigate to home activity by saving user details in internal db
                                                            smartCampusDB.insertUser(user);

                                                            // create a user session in the external db before proceeding into the app
                                                            ParseObject sessionObject = new ParseObject(Constants.Session);
                                                            sessionObject.put(Constants.collegeId, collegeId.getText().toString());
                                                            sessionObject.put(Constants.count, 1);
                                                            sessionObject.saveInBackground();

                                                            // navigate to home activity
                                                            Intent homeIntent = new Intent(getApplicationContext(), GlobalLoginActivity2.class);
                                                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            homeIntent.putExtra(Constants.objectId, secretCode.getText().toString());
                                                            startActivity(homeIntent);
                                                            finish();

                                                        } else {

                                                            // enable go button to re submit the login credentials
                                                            nextButton.setEnabled(true);

                                                            //Toast.makeText(getApplicationContext(), "Invalid email Id", Toast.LENGTH_SHORT).show();

                                                            Snackbar snackbar = Snackbar.make(v, "Invalid credentials", Snackbar.LENGTH_SHORT);
                                                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.errorRed));
                                                            snackbar.show();
                                                        }
                                                    } else {

                                                        // enable go button to re submit the login credentials
                                                        nextButton.setEnabled(true);
                                                        Log.e(Constants.appName, e.getMessage());
                                                        //Toast.makeText(getApplicationContext(), "Invalid Secret code, Try again", Toast.LENGTH_SHORT).show();

                                                        Snackbar snackbar = Snackbar.make(v, "Invalid credentials", Snackbar.LENGTH_SHORT);
                                                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.errorRed));
                                                        snackbar.show();
                                                    }

                                                } catch (Exception ex) {

                                                    Log.e(Constants.appName, ex.getMessage());
                                                    Snackbar snackbar = Snackbar.make(v, "Try again later", Snackbar.LENGTH_SHORT);
                                                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.errorRed));
                                                    snackbar.show();
                                                }


                                            }
                                        });

                                    } else {

                                        // show the message and navigate user back to LaunchScreen
                                        Toast.makeText(getApplicationContext(), "You have already logged in on other device", Toast.LENGTH_SHORT).show();

                                        Intent loginIntent = new Intent(getApplicationContext(), AppLaunchScreen.class);
                                        startActivity(loginIntent);
                                        finish();
                                    }
                                }
                            }
                        });*/

                    }
                    else{

                        // enable go button to re submit the login credentials
                        nextButton.setEnabled(true);

                        // show toast to enter required data
                        //Toast.makeText(getApplicationContext(), "Enter required data", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();

                        // show the error message
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setText(R.string.incorrectPassword);

                    }



                } else {

                    // enable go button to re submit the login credentials
                    nextButton.setEnabled(true);

                    // show toast to enter required data
                    //Toast.makeText(getApplicationContext(), "Enter required data", Toast.LENGTH_SHORT).show();

                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.requiredData);
                }

            }
        });

    }

    // this method will fetch views from layout
    public void getViews(){

        // get views from activity
        collegeId = (EditText) findViewById(R.id.collegeId);
        secretCode = (EditText) findViewById(R.id.secretCode);
        nextButton = (Button) findViewById(R.id.nextButton);
        errorText = (TextView) findViewById(R.id.errorText);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        collegeId.setTypeface(typeface);
        secretCode.setTypeface(typeface);
        nextButton.setTypeface(typeface);
        errorText.setTypeface(typeface);

    }

    @Override
    public void onBackPressed() {

        Intent launchIntent = new Intent(getApplicationContext(), AppLaunchScreen.class);
        startActivity(launchIntent);
        finish();
    }

    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }
    //Unregister receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    /**
     * Verify whether user has previous active login session
     *
     * if so, restrict the user for login
     * else
     *      Check if the collegeId and secretCode are matching
     *      if so, get the user details of collegeId from db and create a active login session
     */
    private class UserLogin extends AsyncTask<String, Void, Void>{

        private String Content = "";
        private String Error = null;
        String data = "";
        String collegeId = "";

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

                collegeId = urls[1];

                // Set Request parameter
                data += "?&" + URLEncoder.encode(Constants.KEY, "UTF-8") + "=" + Constants.key
                        + "&" + URLEncoder.encode(Constants.sessionId, "UTF-8") + "=" + (new Snippets().getUniqueSessionId())
                        + "&" + URLEncoder.encode(Constants.collegeId, "UTF-8") + "=" + (collegeId)
                        + "&" + URLEncoder.encode(Constants.gcm_regId, "UTF-8") + "=" + (token);

                //Log.v(Constants.appName, urls[0]+data);

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

                        //Toast.makeText(GlobalLoginActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setText(R.string.loginServerError);
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

                                    //Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    errorText.setVisibility(View.VISIBLE);
                                    errorText.setText(R.string.errorMsg);
                                    break;

                                // key mismatch
                                case -2:
                                    progressBar.setVisibility(View.GONE);
                                    //Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    errorText.setVisibility(View.VISIBLE);
                                    errorText.setText(R.string.errorMsg);
                                    break;

                                // no data found
                                case -1:
                                    progressBar.setVisibility(View.GONE);
                                    //Toast.makeText(getApplicationContext(), "No account found! Verify college id", Toast.LENGTH_SHORT).show();
                                    errorText.setVisibility(View.VISIBLE);
                                    errorText.setText(R.string.noAccount);
                                    break;

                                // session exists
                                case 0:
                                    progressBar.setVisibility(View.GONE);
                                    //Toast.makeText(getApplicationContext(), R.string.duplicateLogin, Toast.LENGTH_SHORT).show();
                                    errorText.setVisibility(View.VISIBLE);
                                    errorText.setText(R.string.duplicateLogin);
                                    break;

                                // data found
                                case 1:

                                    try {

                                        //new UpdateGCM_BG(getApplicationContext()).execute(Routes.updateGCM, collegeId);

                                        // get JSON Array of 'details'
                                        JSONArray jsonArray = jsonResponse.getJSONArray(Constants.details);
                                        // get the JSON object inside Array
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                                        // get the data and store in internal db
                                        User user = new User();

                                        user.setObjectId(jsonObject.getString(Constants.userObjectId)); // secretCode.getText().toString()
                                        user.setCampusId(jsonObject.getString(Constants.campusId));
                                        user.setUserName(jsonObject.getString(Constants.userName));
                                        user.setEmail(jsonObject.getString(Constants.email));
                                        user.setPhoneNumber(jsonObject.getString(Constants.phoneNumber));
                                        user.setCollegeId(jsonObject.getString(Constants.collegeId));
                                        user.setBranch(jsonObject.getString(Constants.branch));
                                        user.setSemester(jsonObject.getInt(Constants.semester));
                                        user.setYear(jsonObject.getInt(Constants.year));
                                        user.setRole(jsonObject.getString(Constants.role));

                                        user.setMediaCount(jsonObject.getInt(Constants.mediaCount));
                                        user.setMedia(jsonObject.getString(Constants.media));

                                        // insert user details into internal db
                                        smartCampusDB.insertUser(user);

                                        // get JSON Array of 'privileges'
                                        JSONArray privilegeArray = jsonResponse.getJSONArray(Constants.privileges);

                                        // check if there are privileges for user
                                        if(privilegeArray.length() > 0) {
                                            // get the JSON object inside Array
                                            JSONObject privilegeObject = (JSONObject) privilegeArray.get(0);

                                            // get the privilege data
                                            Privilege privilege = new Privilege();

                                            privilege.setPrivilegeId(privilegeObject.getString(Constants.privilegeId));
                                            privilege.setUserObjectId(privilegeObject.getString(Constants.userObjectId));
                                            privilege.setModerating(privilegeObject.getInt(Constants.moderating));
                                            privilege.setDirectory(privilegeObject.getString(Constants.directory));
                                            privilege.setCreatedAt((String) (privilegeObject.get(Constants.createdAt)));
                                            privilege.setUpdatedAt((String) privilegeObject.get(Constants.updatedAt));

                                            // save privilege data to internal db
                                            smartCampusDB.insertPrivilege(privilege);


                                        }else {

                                            // do not update privilege for the user

                                        }


                                        // navigate to home activity
                                        Intent homeIntent = new Intent(getApplicationContext(), GlobalLoginActivity2.class);
                                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        homeIntent.putExtra(Constants.userObjectId, user.getObjectId());
                                        homeIntent.putExtra(Constants.collegeId, user.getCollegeId());
                                        homeIntent.putExtra(Constants.phoneNumber, user.getPhoneNumber());
                                        homeIntent.putExtra(Constants.email, user.getEmail());
                                        startActivity(homeIntent);
                                        finish();

                                        break;
                                    }
                                    catch(Exception e){

                                        Log.e(Constants.error, e.getMessage());
                                        //Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();

                                        progressBar.setVisibility(View.GONE);
                                        errorText.setVisibility(View.VISIBLE);
                                        errorText.setText(R.string.errorMsg);
                                    }


                            }
                        }
                    });





                } catch (JSONException e) {

                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.GONE);
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.errorMsg);
                }

            }

            nextButton.setEnabled(true);

        }

    }
}
