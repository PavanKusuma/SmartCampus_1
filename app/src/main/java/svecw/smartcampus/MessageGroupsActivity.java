package svecw.smartcampus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import adapters.GroupMessagesAdapter;
import internaldb.SmartCampusDB;
import model.MsgGroup;
import utils.ConnectionDetector;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 4/17/2015.
 *
 * This activity will get the message groups that are associated with the current user
 * based on the collegeId
 *
 * On click of the group, navigate the user to messageGroupMsgs activity.
 * From their user will be able to
 *
 *      1. view messages of the group
 *      2. view groupDetails and send messages to individual users
 *      3. send messages to all users of the group
 *
 */
public class MessageGroupsActivity extends AppCompatActivity {

    // adapter to populate list view
    GroupMessagesAdapter adapter;

    // list of college wall posts
    ArrayList<MsgGroup> messagesList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_groups_layout);

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

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // get views from layout
        listView = (ListView) findViewById(R.id.messagesListView);
        //createMessage = (FloatingActionButton) findViewById(R.id.createMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMessages);
        emptyElementMessages = (TextView) findViewById(R.id.emptyElementMessages);

        // adapter for the college wall posts
        adapter = new GroupMessagesAdapter(MessageGroupsActivity.this);
        //setListAdapter(adapter);
        listView.setAdapter(adapter);

        // set emptyTextView for listView
        listView.setEmptyView(emptyElementMessages);


        // list for storing messages
        messagesList = new ArrayList<MsgGroup>();

        year = smartDB.getUser().get(Constants.year).toString();
        branch = smartDB.getUser().get(Constants.branch).toString();
        semester = smartDB.getUser().get(Constants.semester).toString();

        // object for ConnectionDetector
        connectionDetector = new ConnectionDetector(getApplicationContext());

        // check if internet is working
        if(connectionDetector.isInternetWorking()) {

            // get the wall posts from database
            new GetGroupMessages().execute();

        } else {

            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
        }


    }



    /**
     * Get Messages for the given filter parameters
     */
    class GetGroupMessages extends AsyncTask<String, Float, String>{

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // create parse object for the "Messages"
                ParseQuery<ParseObject> issuesQuery = ParseQuery.getQuery(Constants.MessageGroupsUserMapping);

                // get the collegeId of the current user to fetch the groups associated with him
                issuesQuery.whereEqualTo(Constants.collegeId, smartDB.getUser().get(Constants.collegeId));

                // get the list of shouts
                issuesQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {

                        if (e == null) {

                            if (parseObjects.size() > 0) {

                                try{

                                    // navigate through the list of objects
                                    for (int i = 0; i < parseObjects.size(); i++) {

                                        // create object for collegeWallPost class
                                        final MsgGroup msgGroup = new MsgGroup();

                                        // date format for displaying created date
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm");

                                        // set the values of the wall post into object
                                        msgGroup.setGroupId(parseObjects.get(i).getString(Constants.groupId));
                                        msgGroup.setGroupName(parseObjects.get(i).getString(Constants.groupName));
                                        msgGroup.setCollegeId(parseObjects.get(i).getString(Constants.collegeId));
                                        msgGroup.setRole(parseObjects.get(i).getString(Constants.role));
                                        msgGroup.setPhoneNumber(parseObjects.get(i).getString(Constants.phoneNumber));
                                        msgGroup.setUsername(parseObjects.get(i).getString(Constants.userName));
                                        msgGroup.setUpdatedAt(simpleDateFormat.format(parseObjects.get(i).getUpdatedAt()));
                                        messagesList.add(msgGroup);

                                        adapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }catch (Exception ex){
                                    Log.e(Constants.appName, ex.getMessage());
                                }
                            }

                            else {

                                progressBar.setVisibility(View.GONE);
                                emptyElementMessages.setText(R.string.emptyMessages);

                            }
                        }
                    }
                });
            }
            // parse user or other data not found exceptions
            catch (Exception e){
                finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            runOnUiThread(new Runnable() {
                public void run() {

                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    adapter.updateItems(messagesList);
                    // notify the adapter
                    adapter.notifyDataSetChanged();

                }
            });
        }
    }
}
