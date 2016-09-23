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
import model.Wall;
import model.Messages;
import utils.ConnectionDetector;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 4/17/2015.
 */
public class MessagesActivity extends AppCompatActivity {

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

    // toolbar for action bar
    Toolbar toolbar;

    // skip counter
    int skipCounter = 0;

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
        emptyElementMessages = (TextView) findViewById(R.id.emptyElementMessages);

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
        adapter = new MessagesAdapter(MessagesActivity.this);
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

                // get the wall posts from database
                new GetMessages().execute(smartDB.getUser().get(Constants.userObjectId).toString(), year, branch, semester, String.valueOf(skipCounter));

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
        if(smartDB.getUserRole().contentEquals(Constants.student)){

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
            Intent newMessageIntent = new Intent(getApplicationContext(), NewMessageActivity.class);
            startActivity(newMessageIntent);
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




    /**
     * This background task will fetch messages in two categories
     * sent and received
     *
     * accordingly fill the respective list views with messages to display
     *
     */
    private class GetMessages extends AsyncTask<String, Void, Void>{

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
                        + "&" + URLEncoder.encode(Constants.branch, "UTF-8") + "=" + (urls[2])
                        + "&" + URLEncoder.encode(Constants.year, "UTF-8") + "=" + (urls[3])
                        + "&" + URLEncoder.encode(Constants.semester, "UTF-8") + "=" + (urls[4])
                        + "&" + URLEncoder.encode(Constants.limit, "UTF-8") + "=" + (urls[5]);

                Log.v(Constants.appName, urls[0]);

                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request
                URLConnection conn = url.openConnection();
                //conn.setDoOutput(true);
                conn.setDoInput(true);
                //OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                //wr.write(data);
                //wr.flush();

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

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // key mismatch
                                case -2:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // Reached end, no more to display
                                case 0:

                                    Toast.makeText(getApplicationContext(), "No messages yet", Toast.LENGTH_SHORT).show();
                                    break;

                                // data found
                                case 1:

                                    try {

                                        // get JSON Array of 'details'
                                        JSONArray jsonArray = jsonResponse.getJSONArray(Constants.details);

                                        for(int i=0; i<jsonArray.length(); i++){

                                            // get the JSON object inside Array
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                            // get the data and store in internal db
                                            Wall wall = new Wall();

                                            wall.setWallId(jsonObject.getString(Constants.wallId));
                                            wall.setWallType(jsonObject.getString(Constants.wallType)); // secretCode.getText().toString()
                                            wall.setUserObjectId(jsonObject.getString(Constants.userObjectId));
                                            wall.setPostDescription(jsonObject.getString(Constants.postDescription));
                                            wall.setCreatedAt(jsonObject.getString(Constants.createdAt));
                                            wall.setUpdatedAt(jsonObject.getString(Constants.updatedAt));
                                            wall.setLikes(jsonObject.getInt(Constants.likes));
                                            wall.setDislikes(jsonObject.getInt(Constants.dislikes));
                                            wall.setComments(jsonObject.getInt(Constants.comments));
                                            wall.setMediaCount(jsonObject.getInt(Constants.mediaCount));
                                            wall.setMedia(jsonObject.getString(Constants.media));
                                            wall.setIsActive(jsonObject.getInt(Constants.isActive));
                                            wall.setUserName(jsonObject.getString(Constants.userName));

                                            Log.v(Constants.appName, "Comment count:" +jsonObject.getInt(Constants.comments));

                                            // get the user image
                                            JSONObject userObject = (JSONObject) jsonObject.get(Constants.fromuser);
                                            int mediaCount = userObject.getInt(Constants.mediaCount);
                                            if(mediaCount > 0){
                                                wall.setUserImage(userObject.getString(Constants.media).substring(0, (userObject.getString(Constants.media).length()-2)));
                                            }
                                            else{
                                                wall.setUserImage(Constants.null_indicator);
                                            }


                                            // add the object to list
                                            //collegeWallPostsList.add(collegeWall);
                                        }

                                        // update the items
                                        //adapter.updateItems(collegeWallPostsList);
                                        adapter.notifyDataSetChanged();

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
