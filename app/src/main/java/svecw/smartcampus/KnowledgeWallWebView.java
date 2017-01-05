package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import utils.Constants;
import utils.Routes;
import utils.Snippets;

/**
 * Created by Pavan_Kusuma on 5/6/2015.
 */
public class KnowledgeWallWebView extends AppCompatActivity {

    // global web view
    RelativeLayout webViewLayout;
    WebView globalWebView;
    TextView globalTextView;

    // toolbar for actionbar
    Toolbar toolbar;

    // layout inflater
    LayoutInflater layoutInflater;

    String infoId, url, description;

    // bundle for fetching the intent values
    Bundle bundle;

    // progress bar
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.globalpost_webview);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getIntent().getStringExtra(Constants.title));
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

        // get view from activity
        webViewLayout = (RelativeLayout) findViewById(R.id.webViewLayout);
        globalWebView = (WebView) findViewById(R.id.globalWebView);
        globalTextView = (TextView) findViewById(R.id.globalTextView);
        globalTextView.setTypeface(sansFont);
        progressBar = (ProgressBar) findViewById(R.id.progressBarGlobal);

        // set web view client
        globalWebView.setWebViewClient(new MyBrowser());

        // get url from bundle
        //bundle = getIntent().getExtras();

        infoId = getIntent().getStringExtra(Constants.infoId);
        url = getIntent().getStringExtra(Constants.link);
        description = getIntent().getStringExtra(Constants.description);
        Log.v(Constants.appName, url);
        // check if link is present
        // if so display the webView
        if (!url.contentEquals(Constants.null_indicator)) {

            globalWebView.getSettings().setLoadsImagesAutomatically(true);
            globalWebView.getSettings().setJavaScriptEnabled(true);
            globalWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            globalWebView.loadUrl(url);

            // show wait message
            Toast.makeText(KnowledgeWallWebView.this, "Please wait while loading..", Toast.LENGTH_SHORT).show();

            // update hit globalInfo
            new UpdateHitGlobalInfo().execute(Routes.updateSeenGlobalInfo, infoId);

        }
        // as there is no link
        // display the description
        else {

            // enable text view to show description
            webViewLayout.setVisibility(View.GONE);
            globalTextView.setVisibility(View.VISIBLE);
            globalTextView.setText(description);
        }


    }

    private class MyBrowser extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            //super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    /*private class MyBrowser extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    public void setValue(int progress) {
        progressBar.setProgress(progress);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.knowledge_webview_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.webViewShare) {

            // share only content of the post
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, description + " \n" + url);
            startActivity(Intent.createChooser(intent2, "Share via"));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private class UpdateHitGlobalInfo extends AsyncTask<String, Void, Void> {

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
                        + "&" + URLEncoder.encode(Constants.infoId, "UTF-8") + "=" + (urls[1]);


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
    }

}
