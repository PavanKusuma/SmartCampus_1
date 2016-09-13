package svecw.smartcampus;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import internaldb.SmartCampusDB;
import model.User;
import utils.Constants;
import utils.EmailValidator;
import utils.Routes;

/**
 * Created by Pavan on 12/22/15.
 */
public class EditProfileActivity extends AppCompatActivity {

    // views of activity
    EditText emailId, phone;
    Button goBtn;

    String str_emailId, str_phone;

    // sim number and role
    String currentObjectId;

    // internal db object
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    // get the data and store in internal db
    User user = new User();
    Intent editIntent;

    JSONObject jsonResponse;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        // get the objectId
        editIntent = getIntent();
        currentObjectId = smartCampusDB.getUser().get(Constants.userObjectId).toString();
        str_emailId = getIntent().getStringExtra(Constants.email);
        str_phone = getIntent().getStringExtra(Constants.phoneNumber);

        // get views from activity
        emailId = (EditText) findViewById(R.id.emailId);
        phone = (EditText) findViewById(R.id.phone);
        goBtn = (Button) findViewById(R.id.goBtn);

        // set the current values
        if(!str_emailId.contentEquals("-"))
            emailId.setText(str_emailId);

        if(!str_emailId.contentEquals("-"))
            phone.setText(str_phone);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        emailId.setTypeface(typeface);
        phone.setTypeface(typeface);
        goBtn.setTypeface(typeface);

        // check if user entered the required values
        // verify the secret code for the given email id
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                // get input values
                str_emailId = emailId.getText().toString();
                str_phone = phone.getText().toString();

                // disable the go button as verification is in progress
                goBtn.setEnabled(false);

                // check if inputs are present,
                // if so, update the details
                // else, proceed
                if (str_emailId.length() > 0 || str_phone.length() > 0) {

                    // pattern for phone
                    Pattern pattern = Pattern.compile("\\d{10}");
                    Matcher matcher = pattern.matcher(str_phone);

                    // just checking if entered inputs are valid
                    EmailValidator emailValidator = new EmailValidator();
                    if(!emailValidator.validate(str_emailId)){

                        Toast.makeText(EditProfileActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                        goBtn.setEnabled(true);
                    }
                    else if(!matcher.matches()){

                        Toast.makeText(EditProfileActivity.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
                        goBtn.setEnabled(true);
                    }
                    else {

                        // before sending request to server for update
                        // confirm the update operation from user
                        confirmationMessage();
                    }



                } else {

                    // make the values equal to "-"
                    str_emailId = "-";
                    str_phone = "-";

                    confirmationMessage();

/*                    // enable the go button
                    goBtn.setEnabled(true);
                    // show error msg
                    Toast.makeText(getApplicationContext(), "Enter required data", Toast.LENGTH_SHORT).show();*/
                }

            }
        });

    }


    /**
     * This method will confirm user from updating the details to server
     */
    public void confirmationMessage() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        // update the details for given user
                        new UpdatePhoneNumberAndEmail().execute(Routes.update_phone_email, currentObjectId, str_phone, str_emailId);
                        //Toast.makeText(EditProfileActivity.this, "Yes Clicked", Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked // do nothing
                        // enable the go button
                        goBtn.setEnabled(true);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to update profile details ?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


            @Override
            public void onBackPressed() {
            super.onBackPressed();

            editIntent.putExtra(Constants.valueBack, Constants.error);
            setResult(RESULT_OK, editIntent);
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


                                            editIntent.putExtra(Constants.valueBack, Constants.success);
                                            editIntent.putExtra(Constants.email, str_emailId);
                                            editIntent.putExtra(Constants.phoneNumber, str_phone);
                                            setResult(RESULT_OK, editIntent);
                                            finish();

                                            break;
                                        }
                                        catch(Exception e){

                                            Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                            editIntent.putExtra(Constants.valueBack, Constants.error);
                                            setResult(RESULT_OK, editIntent);
                                            finish();
                                        }


                                }
                            }
                        });





                    } catch (JSONException e) {

                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();
                        editIntent.putExtra(Constants.valueBack, Constants.error);
                        setResult(RESULT_OK, editIntent);
                        finish();

                    }

                }
            }
        }
        }

