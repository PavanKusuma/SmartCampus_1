package svecw.smartcampus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import adapters.MessagesAdapter;
import internaldb.SmartCampusDB;
import model.Messages;
import model.MsgGroup;
import utils.ConnectionDetector;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 4/17/2015.
 *
 * This activity will help user to view messages of the current group
 * and also view group details as well as send messages individual and all users
 *
 */
public class MessageGroupMsgsActivity extends AppCompatActivity {

    // adapter to populate list view
    MessagesAdapter adapter;

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
    String groupId;

    // phoneNumbers to which message to be sent
    String phoneNumbers="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_groups_layout);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.groupMessages));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // get the selected GroupId
        groupId = getIntent().getStringExtra(Constants.groupId);

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // get views from layout
        listView = (ListView) findViewById(R.id.messagesListView);
        //createMessage = (FloatingActionButton) findViewById(R.id.createMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMessages);
        emptyElementMessages = (TextView) findViewById(R.id.emptyElementMessages);

        // adapter for the college wall posts
        adapter = new MessagesAdapter(MessageGroupMsgsActivity.this);
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

            // get the group messages and group details from database
            new GetGroupDetails().execute();
            new GetGroupMessages().execute();

        } else {

            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
        }


    }



    // as of now, bulk sms is disabled in the app
    // only calling feature for the respective group member is available
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // so create msg icons is hided

            menu.findItem(R.id.createNewGroupMsg).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //this.newMessage = menu;

        getMenuInflater().inflate(R.menu.grp_msg_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.createNewGroupMsg){

            if(grpDetailsList.size() > 0) {

                if(grpDetailsList.size() == 1){

                    phoneNumbers = grpDetailsList.get(0).getPhoneNumber();
                }
                else {
                    // get the phone numbers
                    for (int i = 0; i < grpDetailsList.size(); i++) {

                        phoneNumbers = grpDetailsList.get(i).getPhoneNumber() + "," + phoneNumbers;

                    }

                    // get the "," out of the complete string
                    phoneNumbers = phoneNumbers.substring(0, (phoneNumbers.length()-2));
                }
                // navigate to msg groups activity
                Intent msgGroupsIntent = new Intent(getApplicationContext(), SendMessageActivity.class);
                msgGroupsIntent.putExtra(Constants.phoneNumber, phoneNumbers);
                msgGroupsIntent.putExtra(Constants.groupId, groupId);
                // add msgType
                msgGroupsIntent.putExtra(Constants.msgType, Constants.msgTypeGateway);
                startActivity(msgGroupsIntent);
            }
            else {

                Toast.makeText(MessageGroupMsgsActivity.this, "Please wait..fetching group details..", Toast.LENGTH_SHORT).show();
            }
        }

        // check if it is groupDetails
        else if(id == R.id.createNewGroupInfo){

            if(grpDetailsList.size() > 0) {

                // pass the group details to the next activity
                Intent groupDetailsIntent = new Intent(getApplicationContext(), MessageGroupDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.groupDetails, grpDetailsList);
                groupDetailsIntent.putExtras(bundle);
                startActivity(groupDetailsIntent);
            }
            else{

                Toast.makeText(MessageGroupMsgsActivity.this, "Fetching group details..try after a moment", Toast.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get Messages of the current group for the given filter parameters
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
                ParseQuery<ParseObject> issuesQuery = ParseQuery.getQuery(Constants.messagesTable);

                // get the collegeId of the current user to fetch the groups associated with him
                issuesQuery.whereEqualTo(Constants.groupId, groupId);

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
                                        final Messages message = new Messages();

/*
                                        // set the values of the wall post into object
                                        message.setCreatedAt(parseObjects.get(i).getCreatedAt());
                                        message.setBranch(parseObjects.get(i).get(Constants.branch).toString());
                                        message.setYear(parseObjects.get(i).get(Constants.year).toString());
                                        message.setSemester(parseObjects.get(i).get(Constants.semester).toString());
                                        message.setMessage(parseObjects.get(i).get(Constants.message).toString());
                                        message.setName(parseObjects.get(i).get(Constants.name).toString());
                                        message.setUserObjectId(parseObjects.get(i).get(Constants.userObjectId).toString());
                                        message.setTo(parseObjects.get(i).get(Constants.to).toString());
                                        message.setIsGroup(parseObjects.get(i).getBoolean(Constants.isGroup));
                                        message.setGroupId(parseObjects.get(i).getString(Constants.groupId));
                                        messagesList.add(message);
*/

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





    /**
     * Get Group details of the current group
     */
    class GetGroupDetails extends AsyncTask<String, Float, String>{

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // create parse object for the "Messages"
                ParseQuery<ParseObject> issuesQuery = ParseQuery.getQuery(Constants.MessageGroupsUserMapping);

                // get the collegeId of the current user to fetch the groups associated with him
                issuesQuery.whereEqualTo(Constants.groupId, groupId);

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
                                        grpDetailsList.add(msgGroup);

                                    }
                                }catch (Exception ex){
                                    Log.e(Constants.appName, ex.getMessage());
                                }
                            }

                            else {

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
        }
    }
}
