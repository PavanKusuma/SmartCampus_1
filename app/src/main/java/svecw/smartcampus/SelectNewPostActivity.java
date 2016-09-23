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
 * Created by Pavan on 6/11/15.
 */
public class SelectNewPostActivity extends AppCompatActivity {

    // layouts from activity
    RelativeLayout knowledgeWallPostView, collegeWallPostView, studentWallPostView, alumniWallPostView;

    // object for internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_newpost_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.selectToPost));
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


        // get layouts from activity
        knowledgeWallPostView = (RelativeLayout) findViewById(R.id.knowledgeWallPostView);
        collegeWallPostView = (RelativeLayout) findViewById(R.id.collegeWallPostView);
        studentWallPostView = (RelativeLayout) findViewById(R.id.studentWallPostView);
        alumniWallPostView = (RelativeLayout) findViewById(R.id.alumniWallPostView);

        // based on user privilege
        // show the layouts that user can access
        // user is allowed to create a wall post
        if(smartCampusDB.getUser().get(Constants.collegeWall)==0){
            collegeWallPostView.setVisibility(View.GONE);
        }
        if(smartCampusDB.getUser().get(Constants.studentWall)==0){
            studentWallPostView.setVisibility(View.GONE);
        }
        if(smartCampusDB.getUser().get(Constants.knowledgeWall)==0){
            knowledgeWallPostView.setVisibility(View.GONE);
        }
        // alumni wall is by default visible to everyone
        // everyone can write to alumni wall

        // navigate the user accordingly to create selected wall post
        knowledgeWallPostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent knowledgeWallNewPostIntent = new Intent(getApplicationContext(), KnowledgeWallNewPostActivity.class);
                startActivity(knowledgeWallNewPostIntent);
                finish();
            }
        });

        collegeWallPostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent collegeWallNewPostIntent = new Intent(getApplicationContext(), CollegeWallNewPostActivity.class);
                startActivity(collegeWallNewPostIntent);
                finish();
            }
        });

        studentWallPostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent studentWallNewPostIntent = new Intent(getApplicationContext(), StudentWallNewPostActivity.class);
                studentWallNewPostIntent.putExtra(Constants.alumniPost, false);
                startActivity(studentWallNewPostIntent);
                finish();
            }
        });

        alumniWallPostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent alumniWallNewPostIntent = new Intent(getApplicationContext(), StudentWallNewPostActivity.class);
                alumniWallNewPostIntent.putExtra(Constants.alumniPost, true);
                startActivity(alumniWallNewPostIntent);
                finish();
            }
        });


    }
}
