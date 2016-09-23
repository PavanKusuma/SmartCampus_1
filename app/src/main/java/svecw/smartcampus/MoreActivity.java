package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Typeface;
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
public class MoreActivity extends AppCompatActivity{

    // views of activity
    RelativeLayout userProfileView, groupsView, adminPanelView, messagesView, learnView, academicsView, examsView, complaintOrFeedbackView,
    directoryView, studentDirectoryView, placementsView, collegeMapView, aboutUsView, aboutAppView;

    TextView messagesTextView;

    // object for internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.more));
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

        // get views from activity
        userProfileView = (RelativeLayout) findViewById(R.id.userProfileView);
        groupsView = (RelativeLayout) findViewById(R.id.groupsView);
        adminPanelView = (RelativeLayout) findViewById(R.id.adminPanelView);
        messagesView = (RelativeLayout) findViewById(R.id.messagesView);
        messagesTextView = (TextView) findViewById(R.id.messagesTextView); messagesTextView.setTypeface(sansFont);
        learnView = (RelativeLayout) findViewById(R.id.learnView);
        academicsView = (RelativeLayout) findViewById(R.id.acadamicsView);
        examsView = (RelativeLayout) findViewById(R.id.examsView);
        complaintOrFeedbackView = (RelativeLayout) findViewById(R.id.complaintOrFeedbackView);
        directoryView = (RelativeLayout) findViewById(R.id.directoryView);
        studentDirectoryView = (RelativeLayout) findViewById(R.id.studentDirectoryView);
        placementsView = (RelativeLayout) findViewById(R.id.placementsView);
        collegeMapView = (RelativeLayout) findViewById(R.id.collegeMapView);
        aboutAppView = (RelativeLayout) findViewById(R.id.aboutUsView);
        aboutUsView = (RelativeLayout) findViewById(R.id.aboutCollegeView);

        // based on user role show the views
        // admin panel is only visible if user is "admin"
        if(!smartCampusDB.getUserRole().contentEquals(Constants.admin)){
            adminPanelView.setVisibility(View.GONE);
        }

        if(!smartCampusDB.getUserRole().contentEquals(Constants.student)){
            messagesTextView.setText(R.string.messages);
        }

        // check if user is privileged to view college directory
        // if so display the college directory option
        // else hide it
        /*if(smartCampusDB.getUserRole().contentEquals(Constants.admin)){
            directoryView.setVisibility(View.VISIBLE);
        }
        else if(smartCampusDB.isUserPrivileged()) {

            if (smartCampusDB.getUserPrivileges().getDirectory().toLowerCase().contains(Constants.collegeDirectory)) {

                directoryView.setVisibility(View.VISIBLE);
            }
            else {

                directoryView.setVisibility(View.GONE);

            }
        }*/

        // as of now college directory is visible to everyone
        directoryView.setVisibility(View.VISIBLE);

        // check if user is privileged to view Student directory
        // if so display the Student directory option
        // else hide it

        if(smartCampusDB.getUserRole().contentEquals(Constants.admin)){
            studentDirectoryView.setVisibility(View.VISIBLE);
        }
        else if(smartCampusDB.isUserPrivileged()) {

            if (smartCampusDB.getUserPrivileges().getDirectory().toLowerCase().contains(Constants.studentDirectory.toLowerCase())) {

                studentDirectoryView.setVisibility(View.VISIBLE);
            }
            else {

                studentDirectoryView.setVisibility(View.GONE);

            }
        }


        userProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to admin panel
                Intent adminPanelIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(adminPanelIntent);

            }
        });

        groupsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to admin panel
                Intent adminPanelIntent = new Intent(getApplicationContext(), TemporaryGroup.class);
                startActivity(adminPanelIntent);

            }
        });

        adminPanelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to admin panel
                Intent adminPanelIntent = new Intent(getApplicationContext(), AdminPanel_SelectAction.class);
                startActivity(adminPanelIntent);

            }
        });

        messagesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to admin panel
                Intent messagesIntent = new Intent(getApplicationContext(), GlobalMessagesActivity.class);
                startActivity(messagesIntent);

            }
        });

        learnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to learn module
                Intent learnIntent = new Intent(getApplicationContext(), LearnActivity.class);
                startActivity(learnIntent);

            }
        });

        academicsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to academics module
                Intent academicsIntent = new Intent(getApplicationContext(), AcademicsActivity.class);
                startActivity(academicsIntent);
            }
        });

        examsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to exams module
                Intent examsIntent = new Intent(getApplicationContext(), ExamsListActivity.class);
                startActivity(examsIntent);

            }
        });

        complaintOrFeedbackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // navigate the user to complaint / feedback activity based on privilege
                // show complaints only when the user has complaintOrFeedback privilege
                // before that check if user is privileged
                if(smartCampusDB.isUserPrivileged()) {

                    if (smartCampusDB.getUserPrivileges().getDirectory().contains(Constants.complaintOrFeedback)) {

                        Intent adminComplaintFeedbackIntent = new Intent(getApplicationContext(), FeedbackActivity.class);
                        startActivity(adminComplaintFeedbackIntent);
                    }
                    // if user does not have complaintOrFeedback privilege, he can only post complaint, so navigate to NewComplaintOrFeedbackActivity
                    else {

                        Intent complaintFeedbackIntent = new Intent(getApplicationContext(), NewFeedbackActivity.class);
                        startActivity(complaintFeedbackIntent);

                    }
                }
                else {

                    // its a normal user flow as user has no privileges assigned
                    Intent complaintFeedbackIntent = new Intent(getApplicationContext(), NewFeedbackActivity.class);
                    startActivity(complaintFeedbackIntent);
                }
            }
        });

        directoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent directoryIntent = new Intent(getApplicationContext(), DirectoryActivity.class);
                startActivity(directoryIntent);

            }
        });

        studentDirectoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent directoryIntent = new Intent(getApplicationContext(), SearchStudent.class);
                startActivity(directoryIntent);

            }
        });

        placementsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementsIntent = new Intent(getApplicationContext(), PlacementsActivity.class);
                startActivity(placementsIntent);
            }
        });

        collegeMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent collegeMapIntent = new Intent(getApplicationContext(), CollegeMapActivity.class);
                startActivity(collegeMapIntent);
            }
        });

        aboutUsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent aboutUsIntent = new Intent(getApplicationContext(), GuestActivity.class);
                startActivity(aboutUsIntent);
            }
        });

        aboutAppView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent aboutAppIntent = new Intent(getApplicationContext(), AboutAppActivity.class);
                startActivity(aboutAppIntent);
            }
        });

        /*guestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent aboutAppIntent = new Intent(getApplicationContext(), GuestActivity.class);
                startActivity(aboutAppIntent);
            }
        });*/

    }
}
