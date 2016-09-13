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

import utils.Constants;

/**
 * Created by Pavan on 7/3/15.
 */
public class PlacementsActivity extends AppCompatActivity {

    RelativeLayout recruitersView, p2016View, p2015View, p2014View, p2013View, p2012View, p2011View, p2010View, p2009View, p2008View, p2007View;

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placements_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.placements));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);


        recruitersView = (RelativeLayout) findViewById(R.id.recruitersView);
        p2016View = (RelativeLayout) findViewById(R.id.p2016View);
        p2015View = (RelativeLayout) findViewById(R.id.p2015View);
        p2014View = (RelativeLayout) findViewById(R.id.p2014View);
        p2013View = (RelativeLayout) findViewById(R.id.p2013View);
        p2012View = (RelativeLayout) findViewById(R.id.p2012View);
        p2011View = (RelativeLayout) findViewById(R.id.p2011View);
        p2010View = (RelativeLayout) findViewById(R.id.p2010View);
        p2009View = (RelativeLayout) findViewById(R.id.p2009View);
        p2008View = (RelativeLayout) findViewById(R.id.p2008View);
        p2007View = (RelativeLayout) findViewById(R.id.p2007View);


        recruitersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementIntent = new Intent(getApplicationContext(), RecruitersLIstActivity.class);
                placementIntent.putExtra(Constants.year, "list");
                startActivity(placementIntent);
            }
        });

        p2016View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementIntent = new Intent(getApplicationContext(), PlacementResultActivity.class);
                placementIntent.putExtra(Constants.year, "2016");
                startActivity(placementIntent);
            }
        });

        p2015View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementIntent = new Intent(getApplicationContext(), PlacementResultActivity.class);
                placementIntent.putExtra(Constants.year, "2015");
                startActivity(placementIntent);
            }
        });

        p2014View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementIntent = new Intent(getApplicationContext(), PlacementResultActivity.class);
                placementIntent.putExtra(Constants.year, "2014");
                startActivity(placementIntent);
            }
        });

        p2013View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementIntent = new Intent(getApplicationContext(), PlacementResultActivity.class);
                placementIntent.putExtra(Constants.year, "2013");
                startActivity(placementIntent);
            }
        });

        p2012View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementIntent = new Intent(getApplicationContext(), PlacementResultActivity.class);
                placementIntent.putExtra(Constants.year, "2012");
                startActivity(placementIntent);
            }
        });

        p2011View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementIntent = new Intent(getApplicationContext(), PlacementResultActivity.class);
                placementIntent.putExtra(Constants.year, "2011");
                startActivity(placementIntent);
            }
        });

        p2010View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementIntent = new Intent(getApplicationContext(), PlacementResultActivity.class);
                placementIntent.putExtra(Constants.year, "2010");
                startActivity(placementIntent);
            }
        });

        p2009View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementIntent = new Intent(getApplicationContext(), PlacementResultActivity.class);
                placementIntent.putExtra(Constants.year, "2009");
                startActivity(placementIntent);
            }
        });

        p2008View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementIntent = new Intent(getApplicationContext(), PlacementResultActivity.class);
                placementIntent.putExtra(Constants.year, "2008");
                startActivity(placementIntent);
            }
        });

        p2007View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementIntent = new Intent(getApplicationContext(), PlacementResultActivity.class);
                placementIntent.putExtra(Constants.year, "2007");
                startActivity(placementIntent);
            }
        });

    }


}
