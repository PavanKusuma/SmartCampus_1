package svecw.smartcampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import internaldb.SmartCampusDB;
import utils.Constants;

/**
 * Created by Pavan on 6/12/15.
 */
public class LearnActivity extends AppCompatActivity {

    // views from activity
    RelativeLayout greView, cView, cPlusPlusView, javaView, htmlView, javaScriptView, androidView;

    // object for internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.learn));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // get views from activity
        greView = (RelativeLayout) findViewById(R.id.greView);
        cView = (RelativeLayout) findViewById(R.id.cView);
        cPlusPlusView = (RelativeLayout) findViewById(R.id.cPlusPlusView);
        javaView = (RelativeLayout) findViewById(R.id.javaView);
        htmlView = (RelativeLayout) findViewById(R.id.htmlView);
        javaScriptView = (RelativeLayout) findViewById(R.id.javaScriptView);
        androidView = (RelativeLayout) findViewById(R.id.androidView);

        // navigate to gre module
        greView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent greintent = new Intent(getApplicationContext(), Gre_MainActivity.class);
                startActivity(greintent);
            }
        });

        // navigate to the sub module activity
        // and show the content based on the selected topic
        cView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cintent = new Intent(getApplicationContext(), LearnSubModuleActivity.class);
                cintent.putExtra(Constants.topic, Constants.C);
                startActivity(cintent);
            }
        });

        cPlusPlusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cPlusPlusintent = new Intent(getApplicationContext(), LearnSubModuleActivity.class);
                cPlusPlusintent.putExtra(Constants.topic, Constants.CPlusPlus);
                startActivity(cPlusPlusintent);
            }
        });

        javaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent javaintent = new Intent(getApplicationContext(), LearnSubModuleActivity.class);
                javaintent.putExtra(Constants.topic, Constants.Java);
                startActivity(javaintent);
            }
        });

        htmlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent javaintent = new Intent(getApplicationContext(), LearnSubModuleActivity.class);
                javaintent.putExtra(Constants.topic, Constants.HTML);
                startActivity(javaintent);
            }
        });

        javaScriptView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent javaintent = new Intent(getApplicationContext(), LearnSubModuleActivity.class);
                javaintent.putExtra(Constants.topic, Constants.JavaScript);
                startActivity(javaintent);
            }
        });

        androidView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent javaintent = new Intent(getApplicationContext(), LearnSubModuleActivity.class);
                javaintent.putExtra(Constants.topic, Constants.Android);
                startActivity(javaintent);
            }
        });
    }
}
