package svecw.smartcampus;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import adapters.DirectorySearchAdapter;
import internaldb.SmartCampusDB;
import utils.Constants;
import utils.Snippets;

/**
 * Created by Pavan on 1/17/16.
 */
public class CreateMessageGroupActivity extends AppCompatActivity {

    // views from activity
    EditText groupName, searchName;
    TextView searchProgressText;
    ImageView searchNameBtn;
    ListView searchListView, selectedListView;

    // list of selected names
    List<String> selectedNames = new ArrayList<>();
    List<String> selectedContacts = new ArrayList<>();
    List<String> selectedCollegeIds = new ArrayList<>();
    List<String> selectedDepartments = new ArrayList<>();

    // list of searched names
    List<String> searchedNames = new ArrayList<>();
    List<String> searchedContacts = new ArrayList<>();
    List<String> searchedCollegeIds = new ArrayList<>();
    List<String> searchedDepartments = new ArrayList<>();

    // complete list of names in db
    List<String> namesList = new ArrayList<>();
    List<String> contactsList = new ArrayList<>();
    List<String> collegeIdsList = new ArrayList<>();
    List<String> departmentsList = new ArrayList<>();

    // dummyList to update list
    List<String> dummyList = new ArrayList<>();

    DirectorySearchAdapter directorySearchAdapter, directorySearchAdapter1;
    ArrayAdapter<String> arrayAdapter;

    // unique groupId for mapping selected users
    String groupId, groupNameString;
    String searchTerm;

    SmartCampusDB smartCampusDB = new SmartCampusDB(this);
    byte[] b = Constants.null_indicator.getBytes();

    // progress dialog
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_message_group_layout);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.newGroup));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title


        // get the faculty data
        getFacultyData();


        groupName = (EditText) findViewById(R.id.groupName);
        searchName = (EditText) findViewById(R.id.searchName);
        searchListView = (ListView) findViewById(R.id.searchListView);
        selectedListView = (ListView) findViewById(R.id.selectedListView);
        searchNameBtn = (ImageView) findViewById(R.id.searchNameBtn);
        searchProgressText = (TextView) findViewById(R.id.searchProgressText);

        // get the instance of adapter
        //directorySearchAdapter = new DirectorySearchAdapter(CreateMessageGroupActivity.this);
        //directorySearchAdapter1 = new DirectorySearchAdapter(CreateMessageGroupActivity.this);
        // set the adapter to listView
        searchListView.setAdapter(directorySearchAdapter);

        // set adapter for selectedListView
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, android.R.id.text1, selectedNames);
        selectedListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

/*
        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // when user is searching for other names, hide the selectedListView and show
                // the searchedListView
                selectedListView.setVisibility(View.GONE);
                searchListView.setVisibility(View.VISIBLE);

                // clear the searchNames before adding fresh search results
                searchedNames.clear();
                searchedContacts.clear();
                searchedCollegeIds.clear();
                searchedDepartments.clear();

                directorySearchAdapter.updateItems(searchedNames, searchedContacts, searchedCollegeIds, searchedDepartments);
                directorySearchAdapter.notifyDataSetChanged();

                // get the searched charSequence and check whether that is present in any of the names
                // parse through the names list
                for (int j = 0; j < namesList.size(); j++) {

                    // compare the search term in the names list
                    if (namesList.get(j).toLowerCase().contains(charSequence.toString().toLowerCase())) {

                        searchedNames.add(namesList.get(j));
                        searchedContacts.add(contactsList.get(j));
                        searchedCollegeIds.add(collegeIdsList.get(j));
                        searchedDepartments.add(departmentsList.get(j));

                    }
                }

                // get the instance of adapter
                //directorySearchAdapter.updateItems(searchedNames, searchedContacts, searchedCollegeIds, searchedDepartments);
                directorySearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });*/

        searchNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchProgressText.setVisibility(View.VISIBLE);

                // check if name is entered in search and it should be more than 3 letters
                if (searchName.getText().toString().length() > 3) {

                    searchTerm = searchName.getText().toString();


                    // when user is searching for other names, hide the selectedListView and show
                    // the searchedListView
                    selectedListView.setVisibility(View.GONE);
                    searchListView.setVisibility(View.VISIBLE);

                    // get the searched charSequence and check whether that is present in any of the names
                    // parse through the names list
                    for (int j = 0; j < namesList.size(); j++) {

                        // compare the search term in the names list
                        if (namesList.get(j).toLowerCase().contains(searchTerm.toLowerCase())) {

                            searchedNames.add(namesList.get(j));
                            searchedContacts.add(contactsList.get(j));
                            searchedCollegeIds.add(collegeIdsList.get(j));
                            searchedDepartments.add(departmentsList.get(j));

                        }
                    }


                    // get the instance of adapter
                    directorySearchAdapter = new DirectorySearchAdapter(CreateMessageGroupActivity.this);
                    searchListView.setAdapter(directorySearchAdapter);
                    directorySearchAdapter.updateItems(searchedNames, searchedContacts, searchedCollegeIds, searchedDepartments);
                    directorySearchAdapter.notifyDataSetChanged();

                    // check if there are any results found
                    if (searchedNames.size() == 0) {

                        Toast.makeText(CreateMessageGroupActivity.this, "No match found to display", Toast.LENGTH_SHORT).show();
                        // show the selected list view and hide the search list
                        selectedListView.setVisibility(View.VISIBLE);
                        searchListView.setVisibility(View.GONE);
                    }


                } else {

                    Toast.makeText(CreateMessageGroupActivity.this, "Enter more than 3 characters", Toast.LENGTH_SHORT).show();
                }

                searchProgressText.setVisibility(View.GONE);
            }
        });


        // on click of searchListView
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // add the selected names from searchListView
                selectedNames.add(searchedNames.get(i));
                selectedContacts.add(searchedContacts.get(i));
                selectedCollegeIds.add(searchedCollegeIds.get(i));
                selectedDepartments.add(searchedDepartments.get(i));

                arrayAdapter.notifyDataSetChanged();


                // clear the searchNames before adding fresh search results
                searchedNames.clear();
                searchedContacts.clear();
                searchedCollegeIds.clear();
                searchedDepartments.clear();

                // get the instance of adapter
                //directorySearchAdapter.updateItems(searchedNames, searchedContacts, searchedCollegeIds, searchedDepartments);
                //directorySearchAdapter.notifyDataSetChanged();

                // show the dummyList in the searchResult initially
                directorySearchAdapter1 = new DirectorySearchAdapter(CreateMessageGroupActivity.this);
                searchListView.setAdapter(directorySearchAdapter1);
                directorySearchAdapter1.updateItems(searchedNames, searchedContacts, searchedCollegeIds, searchedDepartments);
                directorySearchAdapter1.notifyDataSetChanged();

                // show the selected list view and hide the search list
                selectedListView.setVisibility(View.VISIBLE);
                searchListView.setVisibility(View.GONE);


            }
        });

        // on change of searchText hide and display the listView
        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // if the user clears the search text
                // then clear the search listView and show him selectedUsersListView

                if(charSequence.length() == 0){
                    // clear the searchNames before adding fresh search results
                    searchedNames.clear();
                    searchedContacts.clear();
                    searchedCollegeIds.clear();
                    searchedDepartments.clear();

                    // get the instance of adapter
                    //directorySearchAdapter.updateItems(searchedNames, searchedContacts, searchedCollegeIds, searchedDepartments);
                    //directorySearchAdapter.notifyDataSetChanged();

                    // show the dummyList in the searchResult initially
                    directorySearchAdapter1 = new DirectorySearchAdapter(CreateMessageGroupActivity.this);
                    searchListView.setAdapter(directorySearchAdapter1);
                    directorySearchAdapter1.updateItems(searchedNames, searchedContacts, searchedCollegeIds, searchedDepartments);
                    directorySearchAdapter1.notifyDataSetChanged();

                    // show the selected list view and hide the search list
                    selectedListView.setVisibility(View.VISIBLE);
                    searchListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.new_group_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.createNew) {

            // check if groupName is entered
            if(groupName.getText().toString().length() > 0){

                // check if there are selected names
                if(selectedNames.size() > 0){

                    Log.i(Constants.appName, "Size : "+selectedNames.size());

                    // get the groupName
                    groupNameString = groupName.getText().toString();

                    // create new group
                    new CreateMessageGroup().execute();



                }
                else{

                    Toast.makeText(CreateMessageGroupActivity.this, "Add members to this group", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                Toast.makeText(CreateMessageGroupActivity.this, "Enter group name", Toast.LENGTH_SHORT).show();
            }



            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * Create Group
     */
    class CreateMessageGroup extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(CreateMessageGroupActivity.this);
            progressDialog.setMessage("Creating group ..");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // get an unique groupId to place for all selected group members
                groupId = Snippets.getUniqueSessionId();
                Log.i(Constants.appName, "GroupId : "+groupId);

                Log.i(Constants.appName, "Size2 : "+selectedNames.size());
                // save the selected members one by one for the given groupId
                for(int j=0; j<selectedNames.size(); j++){

                    // save the selected users in the group to db
                    ParseObject parseObject = new ParseObject(Constants.MessageGroupsUserMapping);

                    Log.i(Constants.appName, "Checking "+j);
                    parseObject.put(Constants.groupId, groupId);
                    parseObject.put(Constants.groupName, groupNameString);
                    parseObject.put(Constants.userName, selectedNames.get(j));
                    parseObject.put(Constants.collegeId, selectedCollegeIds.get(j));
                    parseObject.put(Constants.phoneNumber, selectedContacts.get(j));
                    parseObject.put(Constants.role, Constants.participant);

                    Log.i(Constants.appName, "Details : " + ", " + groupId + ", " + selectedNames.get(j) + ", " + selectedCollegeIds.get(j) + ", " + selectedContacts.get(j));

                    // save the selected members
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.i(Constants.appName, "Success : ");
                        }
                    });
                }

                // save the selected users in the group to db
                ParseObject parseObject = new ParseObject(Constants.MessageGroupsUserMapping);

                // add the current user to this group as he is the admin
                parseObject.put(Constants.groupId, groupId);
                parseObject.put(Constants.groupName, groupNameString);
                parseObject.put(Constants.userName, smartCampusDB.getUser().get(Constants.userName));
                parseObject.put(Constants.collegeId, smartCampusDB.getUser().get(Constants.collegeId));
                parseObject.put(Constants.phoneNumber, smartCampusDB.getUser().get(Constants.phoneNumber));
                parseObject.put(Constants.role, Constants.admin);

                parseObject.saveInBackground();
                // save the selected members
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        Log.i(Constants.appName, "Successful : ");
                    }
                });

                finish();

            } catch (Exception e) {

                e.printStackTrace();
                Log.e(Constants.appName, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }

    /**
     * Create Group
     */
    class SearchMembers extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            searchProgressText.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // search for the given search username
                ParseQuery<ParseObject> searchQuery = ParseQuery.getQuery(Constants.users);
                searchQuery.whereContains(Constants.userName, searchTerm);

                searchQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {

                        // if no exception
                        if (e == null) {

                            if (list.size() > 0) {

                                Log.i(Constants.appName, "Yes"+list.size());
                                for (int i = 0; i < list.size(); i++) {

                                    // get the names and contact numbers of the searched users
                                    searchedNames.add(list.get(i).getString(Constants.userName));
                                    searchedContacts.add(list.get(i).getString(Constants.phoneNumber));
                                    searchedDepartments.add(list.get(i).getString(Constants.branch));
                                    searchedCollegeIds.add(list.get(i).getObjectId());

                                }

                                directorySearchAdapter.notifyDataSetChanged();
                            } else {

                                Toast.makeText(getApplicationContext(), "No users found with the given search term", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                // get the instance of adapter
                directorySearchAdapter.updateItems(searchedNames, searchedContacts, searchedCollegeIds, searchedDepartments);
                directorySearchAdapter.notifyDataSetChanged();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        searchProgressText.setVisibility(View.GONE);
                    }
                });


            } catch (Exception e) {

                Log.e(Constants.appName, e.getMessage());
            }

            return null;
        }
    }


    /**
     * Get Faculty data
     */
    public void getFacultyData(){

        // get the raw files
        InputStream names_is = getResources().openRawResource(R.raw.facultynames);
        InputStream contacts_is = getResources().openRawResource(R.raw.contacts);
        InputStream designations_is = getResources().openRawResource(R.raw.ids);
        InputStream departments_is = getResources().openRawResource(R.raw.departments);

        // reader
        BufferedReader names_reader = new BufferedReader(new InputStreamReader(names_is));
        BufferedReader contacts_reader = new BufferedReader(new InputStreamReader(contacts_is));
        BufferedReader designations_reader = new BufferedReader(new InputStreamReader(designations_is));
        BufferedReader departments_reader = new BufferedReader(new InputStreamReader(departments_is));

        String line1, line2, line3, line4;
        String[] rowData1, rowData2, rowData3, rowData4 = null;

        try {
            while ((line1 = names_reader.readLine()) != null) {

                line2 = contacts_reader.readLine();
                line3 = designations_reader.readLine();
                line4 = departments_reader.readLine();


                rowData1 = line1.split("\n");
                rowData2 = line2.split("\n");
                rowData3 = line3.split("\n");
                rowData4 = line4.split("\n");

                namesList.add(rowData1[0]);
                contactsList.add(rowData2[0]);
                collegeIdsList.add(rowData3[0]);
                departmentsList.add(rowData4[0]);
            }
        }catch (Exception ex){
            Log.v(Constants.appName, ex.getMessage());
        }

    }

}

