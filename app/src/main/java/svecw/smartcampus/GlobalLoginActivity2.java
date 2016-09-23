package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import internaldb.SmartCampusDB;
import model.User;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan on 12/22/15.
 */
public class GlobalLoginActivity2 extends AppCompatActivity {

    // views of activity
    EditText emailId, phone;
    Button goBtn, skipBtn;

    String str_emailId, str_phone;

    // sim number and role
    String currentObjectId, collegeId, phoneNumber, email;

    // internal db object
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    // get the data and store in internal db
    User user = new User();

    int status;
    JSONObject jsonResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity2);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // get the objectId
        currentObjectId = getIntent().getStringExtra(Constants.userObjectId);
        collegeId = getIntent().getStringExtra(Constants.collegeId);
        phoneNumber = getIntent().getStringExtra(Constants.phoneNumber);
        email = getIntent().getStringExtra(Constants.email);

        // get views from activity
        emailId = (EditText) findViewById(R.id.emailId); emailId.setTypeface(sansFont);
        phone = (EditText) findViewById(R.id.phone); phone.setTypeface(sansFont);
        goBtn = (Button) findViewById(R.id.goBtn); goBtn.setTypeface(sansFont);
        skipBtn = (Button) findViewById(R.id.skipBtn); skipBtn.setTypeface(sansFont);

        // pre fill the values
        emailId.setText(email);
        phone.setText(phoneNumber);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/sans.ttf");
        emailId.setTypeface(typeface);
        phone.setTypeface(typeface);
        goBtn.setTypeface(typeface);

        // check if user entered the required values
        // verify the secret code for the given email id
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                // get the strings entered
                str_emailId = emailId.getText().toString();
                str_phone = phone.getText().toString();
                // disable the go button as verification is in progress
                goBtn.setEnabled(false);

                // check if inputs are present,
                // if so, update the details
                // else, proceed
                if(str_emailId.length() >0 || str_phone.length() >0){

                    // update the details for given user
                    new UpdatePhoneNumberAndEmail().execute(Routes.update_phone_email, currentObjectId, str_phone, str_emailId);

                }
                else {

                    // show error msg
                    Toast.makeText(getApplicationContext(), "Enter required data", Toast.LENGTH_SHORT).show();
                }


            }
        });

        // do nothing
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent homeIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent launchIntent = new Intent(getApplicationContext(), AppLaunchScreen.class);
        startActivity(launchIntent);
        finish();
    }



    /**
     * Verify whether user has previous active login session
     *
     * if so, restrict the user for login
     * else
     *      Check if the collegeId and secretCode are matching
     *      if so, get the user details of collegeId from db and create a active login session
     */
    private class UpdatePhoneNumberAndEmail extends AsyncTask<String, Void, Void> {

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
                        + "&" + URLEncoder.encode(Constants.phoneNumber, "UTF-8") + "=" + (urls[2])
                        + "&" + URLEncoder.encode(Constants.email, "UTF-8") + "=" + (urls[3]);

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

                                // key mismatch
                                case -2:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // no data found
                                case -1:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // session exists
                                case 0:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // data founc
                                case 1:

                                    try {

                                        // update phoneNumber and email to internal db also
                                        // as it should reflect in the current session
                                        smartCampusDB.updateEmailAndPhone(str_emailId, str_phone, currentObjectId);

                                        // navigate to welcome screen
                                        Intent homeIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(homeIntent);
                                        finish();

                                        break;
                                    }
                                    catch(Exception e){

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
