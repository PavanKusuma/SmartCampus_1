package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import adapters.MessagesAdapter;
import internaldb.SmartCampusDB;
import model.Messages;
import notify.Notification_BGService;
import utils.ConnectionDetector;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan_Kusuma on 4/17/2015.
 */
public class GlobalMessagesActivity extends AppCompatActivity {

    // adapter to populate list view
    MessagesAdapter adapter;

    // list of college wall posts
    ArrayList<Messages> messagesList;

    // local db object
    SmartCampusDB smartDB = new SmartCampusDB(this);

    // list view
    ListView listView;
    TextView emptyElementMessages;

    // filter parameters
    String branch, year, semester;
    JSONObject jsonResponse;
    int status;

    // skip counter
    int skipCounter = 0;

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
        setContentView(R.layout.messages_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.messages));
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

        // get views from layout
        listView = (ListView) findViewById(R.id.messagesListView);
        //createMessage = (FloatingActionButton) findViewById(R.id.createMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMessages);
        emptyElementMessages = (TextView) findViewById(R.id.emptyElementMessages); emptyElementMessages.setTypeface(sansFont);

        // based on user role
        // user is allowed to create a message
        // only 'faculty' user type are able to send messages
        /*if(smartDB.getUserRole().contentEquals(Constants.student)){

            newMessage.setGroupVisible(1, false);
            //createMessage.setVisibility(View.GONE); // student is not allowed to send message
        }
        else {

            newMessage.setGroupVisible(1, true);
            //createMessage.setVisibility(View.VISIBLE); // other than students, everyone is allowed to send message
        }*/

        /*createMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // other users except student can send a new message to students
                Intent newMessageIntent = new Intent(getApplicationContext(), NewMessageActivity.class);
                startActivity(newMessageIntent);

            }
        });*/

        // adapter for the college wall posts
        adapter = new MessagesAdapter(GlobalMessagesActivity.this);
        //setListAdapter(adapter);
        listView.setAdapter(adapter);

        // set emptyTextView for listView
        listView.setEmptyView(emptyElementMessages);


        // list for storing messages
        messagesList = new ArrayList<Messages>();

        year = smartDB.getUser().get(Constants.year).toString();
        branch = smartDB.getUser().get(Constants.branch).toString();
        semester = smartDB.getUser().get(Constants.semester).toString();

        // object for ConnectionDetector
        connectionDetector = new ConnectionDetector(getApplicationContext());

        // check if internet is working
        if(connectionDetector.isInternetWorking()) {

            // fetch the messages which matches the current users branch, year and semester
            // based on the fromObjectId, the messages are tagged as received/sent while displaying
            // for user - 'Student' as of now, all are received messages as they cannot send messages

            // get the url
            String messageURL = Routes.getBroadcastMessages + Constants.key + "/" + smartDB.getUser().get(Constants.userObjectId) + "/" + branch + "/" + year + "/" + semester + "/" + skipCounter;

            // get broadcast messages
            new GetBroadcastMessages().execute(Routes.getBroadcastMessages, smartDB.getUser().get(Constants.userObjectId).toString(), String.valueOf(skipCounter));

        } else {

            emptyElementMessages.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //menu.clear();

        // based on user role
        // user is allowed to create a message
        // only 'faculty' user type are able to send messages
        if(smartDB.getUserRole().equalsIgnoreCase(Constants.student)){

            menu.clear();
            // student is not allowed to send message
        }
        else {
            // do nothing
            //other than students, everyone is allowed to send message
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //this.newMessage = menu;

        getMenuInflater().inflate(R.menu.msg_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.createNew) {

            // Navigate to new message screen
            // other users except student can send a new message to students
            //Intent newMessageIntent = new Intent(getApplicationContext(), NewMessageActivity.class);
            Intent newMessageIntent = new Intent(getApplicationContext(), MessageNewComposeActivity.class);

            // start activity to get back result
            startActivityForResult(newMessageIntent, 200);
/*


            final AlertDialog.Builder menuAlert = new AlertDialog.Builder(MessagesActivity.this);
            final String[] menuList = {"New message", "New group"};
            menuAlert.setTitle("Select to create");
            menuAlert.setItems(menuList, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        // options selected
                        case 0:
                            // Navigate to new message screen
                            // other users except student can send a new message to students
                            Intent newMessageIntent = new Intent(getApplicationContext(), NewMessageActivity.class);
                            startActivity(newMessageIntent);

                            break;

                            // Navigate to new group screen
                        case 1:

                            // other users except student can send a new message to students
                            Intent newGroupIntent = new Intent(getApplicationContext(), CreateMessageGroupActivity.class);
                            startActivity(newGroupIntent);

                            break;
                    }
                }


            });
            AlertDialog menuDrop = menuAlert.create();
            menuDrop.show();
*/


            return true;
        }

       /* if(id == R.id.msgGroups){

            // navigate to msg groups activity
            Intent msgGroupsIntent = new Intent(getApplicationContext(), MessageGroupsActivity.class);
            startActivity(msgGroupsIntent);
        }*/

        return super.onOptionsItemSelected(item);
    }

/*
    */
/**
     * Get Messages for the given filter parameters
     *//*

    class GetMessages extends AsyncTask<String, Float, String>{

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

                // get messages specific to user
                if(smartDB.getUser().get(Constants.role).toString().contentEquals(Constants.student)) {

                    // get mapping for the list of objectIds
                    issuesQuery.whereEqualTo(Constants.year, params[0]);
                    issuesQuery.whereEqualTo(Constants.branch, params[1]);
                    issuesQuery.whereEqualTo(Constants.semester, params[2]);
                    issuesQuery.whereEqualTo(Constants.to, Constants.student);
                }
                else if(smartDB.getUser().get(Constants.role).toString().contentEquals(Constants.faculty)){

                    issuesQuery.whereEqualTo(Constants.branch, params[1]);
                    issuesQuery.whereEqualTo(Constants.to, Constants.faculty);

                }
                else {

                    issuesQuery.whereEqualTo(Constants.branch, params[1]);

                }

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

                                        // set the values of the wall post into object
                                        message.setCreatedAt(parseObjects.get(i).getCreatedAt());
                                        message.setBranch(parseObjects.get(i).get(Constants.branch).toString());
                                        message.setYear(parseObjects.get(i).get(Constants.year).toString());
                                        message.setSemester(parseObjects.get(i).get(Constants.semester).toString());
                                        message.setMessage(parseObjects.get(i).get(Constants.message).toString());
                                        message.setName(parseObjects.get(i).get(Constants.userName).toString());
                                        message.setUserObjectId(parseObjects.get(i).get(Constants.userObjectId).toString());
                                        message.setTo(parseObjects.get(i).get(Constants.to).toString());
                                        message.setIsGroup(parseObjects.get(i).getBoolean(Constants.isGroup));
                                        message.setGroupId(parseObjects.get(i).getString(Constants.groupId));
                                        messagesList.add(message);

                                        adapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }catch (Exception ex){
                                    Log.e(Constants.appName, ex.getMessage());
                                }
                            }

                            else {

                                progressBar.setVisibility(View.GONE);
                                emptyElementMessages.setText(R.string.emptyMessagesText);

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

                    */
/**
                     * Updating parsed JSON data into ListView
                     * *//*


                    adapter.updateItems(messagesList);
                    // notify the adapter
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    emptyElementMessages.setText(R.string.emptyMessagesText);

                }
            });
        }
    }
*/

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    Log.v(Constants.appName, "Checking"+1);
    if(requestCode == 200){

        // check if the post is created
        if(resultCode == 1){

            // update the backIntent to display new post on wall
            Messages message = new Messages();

            // set the values of the wall post into object
            message.setMessageId(data.getStringExtra(Constants.messageId));
            message.setMessageType(data.getStringExtra(Constants.messageType));
            message.setMessage(data.getStringExtra(Constants.message));
            message.setFromUserObjectId(data.getStringExtra(Constants.fromUserObjectId));
            message.setToUserObjectId(data.getStringExtra(Constants.toUserObjectId));
            message.setGroupId(data.getStringExtra(Constants.groupId));
            message.setYear(data.getIntExtra(Constants.year, 0));
            message.setBranch(data.getStringExtra(Constants.branch));
            message.setSemester(data.getIntExtra(Constants.semester, 0));
            message.setDepartment(data.getStringExtra(Constants.department));
            message.setCreatedAt(data.getStringExtra(Constants.createdAt));
            message.setUpdatedAt(data.getStringExtra(Constants.updatedAt));
            message.setMediaCount(data.getIntExtra(Constants.mediaCount, 0));
            message.setMedia(data.getStringExtra(Constants.media));
            message.setGroupId(data.getStringExtra(Constants.groupId));

            message.setUsername(data.getStringExtra(Constants.userName));
            message.setFacultyBranch(data.getStringExtra(Constants.facultyBranch)); // get branch of faculty


            // add the object to list at top and notify adapter
            messagesList.add(message);
            adapter.notifyDataSetChanged();

            // send notification
            new Notification_BGService().execute(Routes.createNewMessage_Notify, message.getMessageId());

        }
        // check if the post is not created
        else{

        }
    }
}


    /**
     * This background task will fetch messages in two categories
     * sent and received
     *
     * As these are Broadcast messages, differentiate each message based on role
     * as 'Student' cannot sent messages, all messages are tagged as received messages
     * for 'Faculty' messages will be tagged as received/sent messages based on fromUserObjectId
     */
    private class GetBroadcastMessages extends AsyncTask<String, Void, Void>{

        private String Content = "";
        private String Error = null;
        String data = "";

        @Override
        protected void onPreExecute() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }

        @Override
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader = null;

            // Send data
            try {

                // Set Request parameter
                data += "?&" + URLEncoder.encode(Constants.KEY, "UTF-8") + "=" + Constants.key
                        + "&" + URLEncoder.encode(Constants.fromUserObjectId, "UTF-8") + "=" + (urls[1])
                        //+ "&" + URLEncoder.encode(Constants.year, "UTF-8") + "=" + year
                        //+ "&" + URLEncoder.encode(Constants.branch, "UTF-8") + "=" + branch
                        //+ "&" + URLEncoder.encode(Constants.semester, "UTF-8") + "=" + semester
                        + "&" + URLEncoder.encode(Constants.department, "UTF-8") + "=" + smartDB.getUser().get(Constants.department)
                        + "&" + URLEncoder.encode(Constants.limit, "UTF-8") + "=" + urls[2];

                Log.v(Constants.appName, urls[0]+data);

                // Defined URL  where to send data
                URL url = new URL(urls[0]+data);

                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                //conn.setDoInput(true);
                //OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                //wr.write(data);
                //wr.flush();
                conn.setConnectTimeout(15000);

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();

                // close the reader
                //reader.close();

            } catch (Exception ex) {

                ex.printStackTrace();
                Error = ex.getMessage();


            } finally {

                try {

                    reader.close();

                } catch (Exception ex) {
                    Error = ex.getMessage();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // hide the progress
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Server unreachable! Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            if (Error != null) {

                Log.i("Connection", Error);

            } else {

                //Log.i("Connection", Content);
                /****************** Start Parse Response JSON Data *************/


                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);


                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    status = jsonResponse.getInt(Constants.status);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // check the status and proceed with the logic
                            switch (status){

                                // exception occurred
                                case -3:

                                    progressBar.setVisibility(View.GONE);
                                    emptyElementMessages.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // key mismatch
                                case -2:

                                    progressBar.setVisibility(View.GONE);
                                    emptyElementMessages.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // Reached end, no more to display
                                case 0:

                                    progressBar.setVisibility(View.GONE);
                                    emptyElementMessages.setVisibility(View.VISIBLE);
                                    emptyElementMessages.setText(R.string.noData);
                                    //Toast.makeText(getApplicationContext(), R.string.noData, Toast.LENGTH_SHORT).show();
                                    break;

                                // data found
                                case 1:

                                    try {

                                        // get JSON Array of 'details'
                                        JSONArray jsonArray = jsonResponse.getJSONArray(Constants.details);

                                        for(int i=0; i<jsonArray.length(); i++){

                                            // get the JSON object inside Array
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                            Messages message = new Messages();

                                            // set the values of the wall post into object
                                            message.setMessageId(jsonObject.getString(Constants.messageId));
                                            message.setMessageType(jsonObject.getString(Constants.messageType));
                                            message.setMessage(jsonObject.getString(Constants.message));
                                            message.setFromUserObjectId(jsonObject.getString(Constants.fromUserObjectId));
                                            message.setToUserObjectId(jsonObject.getString(Constants.toUserObjectId));
                                            message.setGroupId(jsonObject.getString(Constants.groupId));
                                            message.setYear(jsonObject.getInt(Constants.year));
                                            message.setBranch(jsonObject.getString(Constants.branch));
                                            message.setSemester(jsonObject.getInt(Constants.semester));
                                            message.setDepartment(jsonObject.getString(Constants.department));
                                            message.setCreatedAt(jsonObject.getString(Constants.createdAt));
                                            message.setUpdatedAt(jsonObject.getString(Constants.updatedAt));
                                            message.setMediaCount(jsonObject.getInt(Constants.mediaCount));
                                            message.setMedia(jsonObject.getString(Constants.media));
                                            message.setGroupId(jsonObject.getString(Constants.groupId));
                                            message.setUsername(jsonObject.getString(Constants.userName));

                                            // get the user image
                                            JSONObject userObject = (JSONObject) jsonObject.get(Constants.fromuser);
                                            int mediaCount = userObject.getInt(Constants.mediaCount);
                                            if(mediaCount > 0){
                                                message.setUserimage(userObject.getString(Constants.media).substring(0, (userObject.getString(Constants.media).length()-2)));
                                            }
                                            else{
                                                message.setUserimage(Constants.null_indicator);
                                            }
                                            message.setFacultyBranch(userObject.getString(Constants.branch)); // get branch of faculty


                                            // add the object to list
                                            messagesList.add(message);
                                        }

                                        // update the items
                                        adapter.updateItems(messagesList);
                                        adapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.GONE);

                                        // detect the end of scrolling
                                        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                            @Override
                                            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                                                int threshold = 1;
                                                int count = listView.getCount();

                                                if (scrollState == SCROLL_STATE_IDLE) {
                                                    if (listView.getLastVisiblePosition() >= count
                                                            - threshold) {

                                                        // show loading
                                                        progressBar.setVisibility(View.VISIBLE);

                                                        // increment the skip counter
                                                        skipCounter = skipCounter + 10;
                                                        // Fetch additional posts skipping existing posts
                                                        new GetBroadcastMessages().execute(Routes.getBroadcastMessages, smartDB.getUser().get(Constants.userObjectId).toString(), String.valueOf(skipCounter));
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                                            }
                                        });

                                    }
                                    catch(Exception e){

                                        Log.e(Constants.error, e.getMessage());
                                        Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();

                                    }

                                    break;

                            }
                        }
                    });





                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();

                }

            }
        }
    }
}
