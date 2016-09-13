package notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import utils.Constants;

/**
 * Created by Pavan_Kusuma on 8/28/2016.
 */
public class CollectGCM_BG extends AsyncTask<String, Void, Void> {

    private Context mContext;
    //Creating a broadcast receiver for gcm registration
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    // token
    String token = Constants.null_indicator;
    private String Content = "";
    private String Error = null;
    String data = "";
    String collegeId = "";
    String urls = "";

    public CollectGCM_BG(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(String... params) {

        urls = params[0];
        collegeId = params[1];
        // This particular code will register this device to the gcm server for sending push notifications
        {
            //Checking play service is available or not
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);

            //if play service is not available
            if (ConnectionResult.SUCCESS != resultCode) {
                //If play service is supported but not installed
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    //Displaying message that play service is not installed
                    Toast.makeText(mContext, "To receive notifications install/enabled Google Play Service in this device!", Toast.LENGTH_LONG).show();
                    GooglePlayServicesUtil.showErrorNotification(resultCode, mContext);

                    //If play service is not supported
                    //Displaying an error message
                } else {
                    Toast.makeText(mContext, "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
                }

                //If play service is available
            } else {
                //Starting intent to register device
                Intent intent = new Intent(mContext, GCMRegistrationIntentService.class);
                mContext.startService(intent);
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
                    Toast.makeText(mContext, "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //tokenText.setText(token);



                    /************ Make Post Call To Web Server ***********/
                    BufferedReader reader = null;

                    // Send data
                    try {

                        // Set Request parameter
                        data += "?&" + URLEncoder.encode(Constants.KEY, "UTF-8") + "=" + Constants.key
                                + "&" + URLEncoder.encode(Constants.collegeId, "UTF-8") + "=" + (collegeId)
                                + "&" + URLEncoder.encode(Constants.gcm_regId, "UTF-8") + "=" + token;
                        //+ "&" + URLEncoder.encode(Constants.gcm_regId, "UTF-8") + "=" + (token);

                        //Log.v(Constants.appName, urls[0]+data);

                        // Defined URL  where to send data
                        URL url = new URL(urls+data);

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
                        //Error = ex.getMessage();


                    } finally {

                        try {

                            reader.close();

                        } catch (Exception ex) {
                            Error = ex.getMessage();
                        }
                    }



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



        return null;
    }
}
