package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import adapters.KnowledgeWallAdapter;
import internaldb.SmartCampusDB;
import model.GlobalInfo;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 12/13/2016.
 */

public class FragmentMore extends Fragment implements Global_Activity.FragmentLifecycle {

    // views of activity
    RelativeLayout userProfileView, groupsView, adminPanelView, messagesView, driveView, learnView, academicsView, examsView, complaintOrFeedbackView,
            directoryView, studentDirectoryView, placementsView, collegeMapView, aboutUsView, bugView, aboutAppView;

    TextView messagesTextView;

    // object for internal db
    SmartCampusDB smartCampusDB;
    LayoutInflater layoutInflater;

    public FragmentMore() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the layout
        ScrollView itemView = (ScrollView) inflater.inflate(R.layout.more_activity, container, false);

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(getContext());

        smartCampusDB = new SmartCampusDB(getContext());

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // get views from activity
        userProfileView = (RelativeLayout) itemView.findViewById(R.id.userProfileView);
        groupsView = (RelativeLayout) itemView.findViewById(R.id.groupsView);
        adminPanelView = (RelativeLayout) itemView.findViewById(R.id.adminPanelView);
        messagesView = (RelativeLayout) itemView.findViewById(R.id.messagesView);
        driveView = (RelativeLayout) itemView.findViewById(R.id.driveView);
        messagesTextView = (TextView) itemView.findViewById(R.id.messagesTextView); messagesTextView.setTypeface(sansFont);
        learnView = (RelativeLayout) itemView.findViewById(R.id.learnView);
        academicsView = (RelativeLayout) itemView.findViewById(R.id.acadamicsView);
        examsView = (RelativeLayout) itemView.findViewById(R.id.examsView);
        complaintOrFeedbackView = (RelativeLayout) itemView.findViewById(R.id.complaintOrFeedbackView);
        directoryView = (RelativeLayout) itemView.findViewById(R.id.directoryView);
        studentDirectoryView = (RelativeLayout) itemView.findViewById(R.id.studentDirectoryView);
        placementsView = (RelativeLayout) itemView.findViewById(R.id.placementsView);
        collegeMapView = (RelativeLayout) itemView.findViewById(R.id.collegeMapView);
        aboutAppView = (RelativeLayout) itemView.findViewById(R.id.aboutUsView);
        bugView = (RelativeLayout) itemView.findViewById(R.id.bugView);
        aboutUsView = (RelativeLayout) itemView.findViewById(R.id.aboutCollegeView);

        // based on user role show the views
        // admin panel is only visible if user is "admin"
        if(!smartCampusDB.getUserRole().equalsIgnoreCase(Constants.admin)){
            adminPanelView.setVisibility(View.GONE);
        }

        if(!smartCampusDB.getUserRole().equalsIgnoreCase(Constants.student)){
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

        if(smartCampusDB.getUserRole().equalsIgnoreCase(Constants.admin)){
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
                Intent adminPanelIntent = new Intent(getContext(), ProfileActivity.class);
                startActivity(adminPanelIntent);

            }
        });

        groupsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to admin panel
                Intent adminPanelIntent = new Intent(getContext(), TemporaryGroup.class);
                startActivity(adminPanelIntent);

            }
        });

        adminPanelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to admin panel
                Intent adminPanelIntent = new Intent(getContext(), AdminPanel_SelectAction.class);
                startActivity(adminPanelIntent);

            }
        });

        messagesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to admin panel
                Intent messagesIntent = new Intent(getContext(), GlobalMessagesActivity.class);
                startActivity(messagesIntent);

            }
        });

        driveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to drive to upload and share content
                Intent driveIntent = new Intent(getContext(), DriveActivity.class);
                startActivity(driveIntent);
            }
        });

        learnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to learn module
                Intent learnIntent = new Intent(getContext(), LearnActivity.class);
                startActivity(learnIntent);

            }
        });

        academicsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to academics module
                Intent academicsIntent = new Intent(getContext(), AcademicsActivity.class);
                startActivity(academicsIntent);
            }
        });

        examsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // user navigates to exams module
                Intent examsIntent = new Intent(getContext(), ExamsListActivity.class);
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

                        Intent adminComplaintFeedbackIntent = new Intent(getContext(), FeedbackActivity.class);
                        startActivity(adminComplaintFeedbackIntent);
                    }
                    // if user does not have complaintOrFeedback privilege, he can only post complaint, so navigate to NewComplaintOrFeedbackActivity
                    else {

                        Intent complaintFeedbackIntent = new Intent(getContext(), NewFeedbackActivity.class);
                        startActivity(complaintFeedbackIntent);

                    }
                }
                else if(smartCampusDB.getUserRole().equalsIgnoreCase(Constants.admin)){

                    // its a normal user flow as user has no privileges assigned
                    Intent complaintFeedbackIntent = new Intent(getContext(), FeedbackActivity.class);
                    startActivity(complaintFeedbackIntent);
                }
                else {

                    Intent complaintFeedbackIntent = new Intent(getContext(), NewFeedbackActivity.class);
                    startActivity(complaintFeedbackIntent);
                }
            }
        });

        directoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent directoryIntent = new Intent(getContext(), DirectoryActivity.class);
                startActivity(directoryIntent);

            }
        });

        studentDirectoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent directoryIntent = new Intent(getContext(), SearchStudent.class);
                startActivity(directoryIntent);

            }
        });

        placementsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementsIntent = new Intent(getContext(), PlacementsActivity.class);
                startActivity(placementsIntent);
            }
        });

        collegeMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent collegeMapIntent = new Intent(getContext(), CollegeMapActivity.class);
                startActivity(collegeMapIntent);
            }
        });

        aboutUsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent aboutUsIntent = new Intent(getContext(), GuestActivity.class);
                startActivity(aboutUsIntent);
            }
        });

        bugView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent aboutUsIntent = new Intent(getContext(), BugReportAndSuggestions.class);
                startActivity(aboutUsIntent);
            }
        });

        aboutAppView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent aboutAppIntent = new Intent(getContext(), AboutAppActivity.class);
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


        return itemView;
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onResumeFragment() {

    }
}
