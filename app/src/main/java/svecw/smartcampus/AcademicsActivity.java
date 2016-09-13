package svecw.smartcampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
 * Created by Pavan on 6/24/15.
 */
public class AcademicsActivity extends AppCompatActivity {

    // views of activity
    RelativeLayout timeTableView, syllabusView, classNotesView;
    FloatingActionButton createAcademics;

    SmartCampusDB smartDB = new SmartCampusDB(this);

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.academics_activity);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.academics));

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
        timeTableView = (RelativeLayout) findViewById(R.id.timeTableView);
        syllabusView = (RelativeLayout) findViewById(R.id.syllabusView);
        classNotesView = (RelativeLayout) findViewById(R.id.classNotesView);
        createAcademics = (FloatingActionButton) findViewById(R.id.createAcademics);

        // based on user role
        // user is allowed to create a message

        if(smartDB.getUserRole().contentEquals(Constants.student)){

            createAcademics.setVisibility(View.GONE);
        }
        else {

            createAcademics.setVisibility(View.VISIBLE);
        }


        timeTableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent timeTableIntent = new Intent(getApplicationContext(), AcademicsModuleActivity.class);
                timeTableIntent.putExtra(Constants.module, Constants.timetable);
                startActivity(timeTableIntent);
            }
        });

        syllabusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent timeTableIntent = new Intent(getApplicationContext(), AcademicsModuleActivity.class);
                timeTableIntent.putExtra(Constants.module, Constants.syllabus);
                startActivity(timeTableIntent);
            }
        });

        classNotesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent timeTableIntent = new Intent(getApplicationContext(), AcademicsModuleActivity.class);
                timeTableIntent.putExtra(Constants.module, Constants.classnotes);
                startActivity(timeTableIntent);
            }
        });

        createAcademics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // other users except student can send a new message to students
                Intent newAcademicsIntent = new Intent(getApplicationContext(), AcademicsNewActivity.class);
                startActivity(newAcademicsIntent);
            }
        });
    }
}
