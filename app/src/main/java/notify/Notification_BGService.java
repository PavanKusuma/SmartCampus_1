package notify;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import utils.Constants;

/**
 * Created by Pavan_Kusuma on 8/23/2016.
 */
public class Notification_BGService extends AsyncTask<String, Void, Void>{

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
                    + "&" + URLEncoder.encode(Constants.userObjectId, "UTF-8") + "=" + (urls[1]);


            Log.v(Constants.appName, urls[0]+data);

            // Defined URL  where to send data
            URL url = new URL(urls[0]+data);

            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

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

        // clear the dialog
        //progressDialog.dismiss();

        if (Error != null) {

            Log.i("Connection", Error);

        } else {

        }
    }
}

