package svecw.smartcampus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import adapters.StudentDirectoryAdapter;
import model.User;
import utils.Constants;
import utils.Routes;
import utils.Snippets;

/**
 * Created by Pavan on 5/21/15.
 */
public class SearchStudent extends AppCompatActivity {

    // toolbar for action bar
    Toolbar toolbar;

    // views
    EditText searchTerm;
    ListView usersListView;
    ImageView searchBtn;
    RadioGroup searchTypeSelection;
    RadioButton radioButton;
    String searchType, str_searchTerm;

    // list of users
    ArrayList<User> usersList;

    // progress bar
    ProgressBar progressBar;

    // adapter
    StudentDirectoryAdapter userAdapter;

    LayoutInflater layoutInflater;
    JSONObject jsonResponse;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminpanel_selectuser_activity);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.searchStudent));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        progressBar = (ProgressBar) findViewById(R.id.searchTermProgress);

        // get views from activity
        usersListView = (ListView) findViewById(R.id.usersListView);
        searchTerm = (EditText) findViewById(R.id.searchTerm);
        searchBtn = (ImageView) findViewById(R.id.searchBtn);
        searchTypeSelection = (RadioGroup) findViewById(R.id.searchTypeSelection);

        // object for users list
        usersList = new ArrayList<User>();

        // adapter
        userAdapter = new StudentDirectoryAdapter(SearchStudent.this);
        usersListView.setAdapter(userAdapter);


        // search for the given username
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchTerm.getText().length() >= 2) {

                    str_searchTerm = searchTerm.getText().toString(); // get the searchTerm
                    // show the progress
                    progressBar.setVisibility(View.VISIBLE);

                    // clear the previous search user list
                    usersList.clear();

                    // get the search type
                    // get selected radio button from radioGroup
                    int selectedId = searchTypeSelection.getCheckedRadioButtonId();

                    // find the radio button by returned id
                    radioButton = (RadioButton) findViewById(selectedId);
                    searchType = radioButton.getTag().toString();

                    // get the users and populate listView
                    new SearchUsers().execute(Routes.searchStudents, searchType, Snippets.escapeURIPathParam(str_searchTerm));

                    // get the users to populate listView
                    //new GetRoleUsers().execute(searchType, searchTerm.getText().toString());

                } else {
                    // hide the list view
                    usersListView.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Enter atleast 2 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // clear the searchTerm on change of searchType
        searchTypeSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                searchTerm.setText("");
            }
        });
/*
        // navigate to next activity after selecting one of the user from the list view
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // getting values from selected ListItem
                String selectUserObjectId = ((TextView) view.findViewById(R.id.selectUser_ObjectId)).getText().toString();

                // Starting new intent
                Intent selectPrivilegeIntent = new Intent(getApplicationContext(), AdminPanel_SelectPrivileges.class);

                // parse through List of users and pass the selected card details
                // to the next activity through intent
                for (int i = 0; i < usersList.size(); i++) {

                    if (usersList.get(i).getObjectId().equals(selectUserObjectId)) {

                        selectPrivilegeIntent.putExtra(Constants.objectId, usersList.get(i).getObjectId());
                        selectPrivilegeIntent.putExtra(Constants.userName, usersList.get(i).getUserName());
*//*

                        selectPrivilegeIntent.putExtra(Constants.branch, usersList.get(i).getBranch());
                        selectPrivilegeIntent.putExtra(Constants.role, usersList.get(i).getRole());
*//*

                    }

                }

                //selectPrivilegeIntent.putExtra("otherUserImage", puserimage);

                // starting new activity and expecting some response back
                startActivity(selectPrivilegeIntent);
            }
        });*/

    }
/*

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //menu.clear();

        // based on the network connection we will display the menu
        //
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


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/



    /**
     * Search users with role as 'Student'
     * with the given searchTerm and the searchFactor
     */
    private class SearchUsers extends AsyncTask<String, Void, Void>{

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
                        + "&" + URLEncoder.encode(Constants.searchType, "UTF-8") + "=" + (urls[1])
                        + "&" + URLEncoder.encode(Constants.searchTerm, "UTF-8") + "=" + (urls[2]);

                // Defined URL
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

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // key mismatch
                                case -2:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // no data found
                                case -1:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // session exists
                                case 0:

                                    Toast.makeText(getApplicationContext(), "No users found", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    break;

                                // data founc
                                case 1:

                                    try {

                                        // get JSON Array of 'details'
                                        JSONArray jsonArray = jsonResponse.getJSONArray(Constants.details);

                                        for(int i=0; i<jsonArray.length(); i++){

                                            // get the JSON object inside Array
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                            User userList = new User();

                                            userList.setObjectId(jsonObject.getString(Constants.userObjectId));
                                            userList.setUserName(jsonObject.getString(Constants.userName));
                                            userList.setBranch(jsonObject.getString(Constants.branch));
                                            userList.setCollegeId(jsonObject.getString(Constants.collegeId));
                                            userList.setPhoneNumber(jsonObject.getString(Constants.phoneNumber));
                                            userList.setMediaCount(jsonObject.getInt(Constants.mediaCount));
                                            userList.setMedia(jsonObject.getString(Constants.media));

                                            usersList.add(userList);
                                            //userAdapter.notifyDataSetChanged();
                                        }


                                                /**
                                                 * Updating parsed JSON data into ListView
                                                 * */

                                                userAdapter.updateItems(usersList);
                                                // notify the adapter
                                                userAdapter.notifyDataSetChanged();
                                                progressBar.setVisibility(View.GONE);
                                                usersListView.setVisibility(View.VISIBLE);

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