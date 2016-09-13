package svecw.smartcampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import utils.Constants;

/**
 * Created by Pavan on 1/22/16.
 */
public class TemporaryGroup extends AppCompatActivity {

    RelativeLayout goKartingView, hodsView, deanView, hodView, raggingView, counselorsView, placementCoordinatorsView,
    ecapView, classView, hostelView, clubView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_group);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.groups));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title


        //goKartingView = (RelativeLayout) findViewById(R.id.goKartingView);
        hodsView = (RelativeLayout) findViewById(R.id.hodsView);
        deanView = (RelativeLayout) findViewById(R.id.deanView);
        raggingView = (RelativeLayout) findViewById(R.id.raggingView);
        counselorsView = (RelativeLayout) findViewById(R.id.counselorsView);
        placementCoordinatorsView = (RelativeLayout) findViewById(R.id.placementCoordinatorsView);
        ecapView = (RelativeLayout) findViewById(R.id.ecapView);
        classView = (RelativeLayout) findViewById(R.id.classView);
        hostelView = (RelativeLayout) findViewById(R.id.hostelView);
        clubView = (RelativeLayout) findViewById(R.id.clubsView);

        hodsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent tempIntent = new Intent(getApplicationContext(), TemporaryGroup_Details.class);
                tempIntent.putExtra(Constants.groupName, "HOD");
                startActivity(tempIntent);
            }
        });

        deanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent tempIntent = new Intent(getApplicationContext(), TemporaryGroup_Details.class);
                tempIntent.putExtra(Constants.groupName, "Dean");
                startActivity(tempIntent);
            }
        });

        raggingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent tempIntent = new Intent(getApplicationContext(), TemporaryGroup_Details.class);
                tempIntent.putExtra(Constants.groupName, "Antiragging");
                startActivity(tempIntent);
            }
        });

        counselorsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent tempIntent = new Intent(getApplicationContext(), TemporaryGroup_Details.class);
                tempIntent.putExtra(Constants.groupName, "Counselors");
                startActivity(tempIntent);
            }
        });

        placementCoordinatorsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent tempIntent = new Intent(getApplicationContext(), TemporaryGroup_Details.class);
                tempIntent.putExtra(Constants.groupName, "Placement");
                startActivity(tempIntent);
            }
        });

        ecapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent tempIntent = new Intent(getApplicationContext(), TemporaryGroup_Details.class);
                tempIntent.putExtra(Constants.groupName, "ECAP");
                startActivity(tempIntent);
            }
        });

        classView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent tempIntent = new Intent(getApplicationContext(), TemporaryGroup_Details.class);
                tempIntent.putExtra(Constants.groupName, "CRs");
                startActivity(tempIntent);
            }
        });


        hostelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent tempIntent = new Intent(getApplicationContext(), TemporaryGroup_Details.class);
                tempIntent.putExtra(Constants.groupName, "HRs");
                startActivity(tempIntent);
            }
        });

        clubView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent tempIntent = new Intent(getApplicationContext(), TemporaryGroup_Details.class);
                tempIntent.putExtra(Constants.groupName, "Student clubs");
                startActivity(tempIntent);
            }
        });

    }
}
