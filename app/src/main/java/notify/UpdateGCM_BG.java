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
 * Created by Pavan_Kusuma on 8/29/2016.
 */
public class UpdateGCM_BG extends AsyncTask<String, Void, Void> {


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

    public UpdateGCM_BG(Context context) {
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




        return null;
    }

}
