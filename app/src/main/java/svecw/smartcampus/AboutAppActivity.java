package svecw.smartcampus;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    TextView appName, appVersion;

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.aboutApp));

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

        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.FONTNAME);
        appName.setTypeface(typeface);
        appVersion.setTypeface(typeface);

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
}
