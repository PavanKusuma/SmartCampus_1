package svecw.smartcampus;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;

import utils.Constants;

/**
 * Created by Pavan_Kusuma on 4/18/2015.
 */
public class AboutAppActivity extends AppCompatActivity {

    ActionBar actionBar;

    // toolbar for actionbar
    Toolbar toolbar;

    TextView appInfo, appName, appVersion, applink1, applink2;
    Spanned text;

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.aboutApp));
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

        appName = (TextView) findViewById(R.id.appName);
        appVersion = (TextView) findViewById(R.id.appVersion);
        appInfo = (TextView) findViewById(R.id.appInfo);
        applink1 = (TextView) findViewById(R.id.applink1);
        applink2 = (TextView) findViewById(R.id.applink2);


        appName.setTypeface(sansFont);
        appVersion.setTypeface(sansFont);
        appInfo.setTypeface(sansFont);
        applink1.setTypeface(sansFont);
        applink2.setTypeface(sansFont);

        text = Html.fromHtml("<a href='http://www.infinitynext.com//'>www.infinitynext.com</a>");

        applink1.setMovementMethod(LinkMovementMethod.getInstance());
        applink1.setText(text);

        text = Html.fromHtml("<a href='http://www.smartcampusapp.com//'>www.smartcampusapp.com</a>");

        applink2.setMovementMethod(LinkMovementMethod.getInstance());
        applink2.setText(text);
    }

        // get the Action bar
       /* actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        View customView = layoutInflater.inflate(R.layout.action_bar_layout, null);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f9f9f9")));
        actionBar.setCustomView(customView);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setElevation(0.7f);*/

}
