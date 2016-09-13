package svecw.smartcampus;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import adapters.MsgGroupDetailsAdapter;
import internaldb.SmartCampusDB;
import model.Messages;
import model.MsgGroup;
import utils.ConnectionDetector;
import utils.Constants;

/**
 * Created by Pavan on 1/20/16.
 *
 * This activity will display the details of users of current group
 * which are fetched in previous activity
 *
 */
public class MessageGroupDetailsActivity extends AppCompatActivity {

    // adapter to populate list view
    MsgGroupDetailsAdapter adapter;

    // list of college wall posts
    ArrayList<Messages> messagesList;
    ArrayList<MsgGroup> grpDetailsList;

    // local db object
    SmartCampusDB smartDB = new SmartCampusDB(this);

    // list view
    ListView listView;
    TextView emptyElementMessages;

    // filter parameters
    String branch, year, semester;

    // toolbar for action bar
    Toolbar toolbar;

    // progress bar
    ProgressBar progressBar;

    // instance for connection detector
    ConnectionDetector connectionDetector;

    // layout inflater
    LayoutInflater layoutInflater;

    // create message menu
    Menu newMessage;
    // create message floating button
    //FloatingActionButton createMessage;

    // get the selected groupId
    ArrayList<MsgGroup> groupDetails = new ArrayList<>();

    // phoneNumbers to which message to be sent
    String phoneNumbers="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_groups_info_layout);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.groupMessagesDetails));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title



        // get the groupDetails from previous activity
        groupDetails = getIntent().getExtras().getParcelableArrayList(Constants.groupDetails);



        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // get views from layout
        listView = (ListView) findViewById(R.id.groupUsersListView);
        //createMessage = (FloatingActionButton) findViewById(R.id.createMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMessages);
        emptyElementMessages = (TextView) findViewById(R.id.emptyElementMessages);

        // adapter for the college wall posts
        adapter = new MsgGroupDetailsAdapter(MessageGroupDetailsActivity.this);
        //setListAdapter(adapter);
        listView.setAdapter(adapter);

        // set emptyTextView for listView
        listView.setEmptyView(emptyElementMessages);


        // list for storing messages
        messagesList = new ArrayList<Messages>();
        grpDetailsList = new ArrayList<MsgGroup>();

        year = smartDB.getUser().get(Constants.year).toString();
        branch = smartDB.getUser().get(Constants.branch).toString();
        semester = smartDB.getUser().get(Constants.semester).toString();

        // object for ConnectionDetector
        connectionDetector = new ConnectionDetector(getApplicationContext());

        // check if internet is working
        if(connectionDetector.isInternetWorking()) {

            // assign the group details
            adapter.updateItems(groupDetails);
            // notify the adapter
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

        } else {

            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
        }


    }



}

