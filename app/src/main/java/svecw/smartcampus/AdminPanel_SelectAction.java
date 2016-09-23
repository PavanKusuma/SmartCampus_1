package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import utils.Constants;

/**
 * Created by Pavan on 5/20/15.
 */
public class AdminPanel_SelectAction extends AppCompatActivity {

    // toolbar for action bar
    Toolbar toolbar;

    // views
    TextView assignPrivilege, updateStudentBranch;

    // layout inflater
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminpanel_selectaction_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.adminPanel));
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
        assignPrivilege = (TextView) findViewById(R.id.assignPrivilege); assignPrivilege.setTypeface(sansFont);
        updateStudentBranch = (TextView) findViewById(R.id.updateStudentBranch); updateStudentBranch.setTypeface(sansFont);

        assignPrivilege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // navigate to user selection screen
                Intent assignPrivilegeIntent = new Intent(getApplicationContext(), AdminPanel_SelectUser.class);
                startActivity(assignPrivilegeIntent);
            }
        });

        updateStudentBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // navigate to user selection screen
                Intent udpateStudentBranchIntent = new Intent(getApplicationContext(), AdminPanel_UpdateBranchAction.class);
                startActivity(udpateStudentBranchIntent);
            }
        });


    }
}
